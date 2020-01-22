/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.pool.ha.node;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

/**
 * This abstract class defines a listener to monitor the change of DataSource nodes.
 *
 * @author DigitalSonic
 * @see Observable
 */
public abstract class NodeListener extends Observable {
    private Properties properties = new Properties();
    private Date lastUpdateTime = null;
    private Observer observer = null;

    /**
     * The method implements the detail update logic.
     */
    public abstract List<NodeEvent> refresh();

    /**
     * Add the given PoolUpdater as the Observer.
     *
     * @see #setObserver(Observer)
     */
    public void init() {
        if (observer == null) {
            throw new RuntimeException("No Observer(such as PoolUpdater) specified.");
        }
        this.addObserver(observer);
    }

    /**
     * Fire the refresh() method and notify the Observer.
     *
     * @see #refresh()
     */
    public void update() {
        List<NodeEvent> events = refresh();
        if (events != null && !events.isEmpty()) {
            this.lastUpdateTime = new Date();
            NodeEvent[] arr = new NodeEvent[events.size()];
            for (int i = 0; i < events.size(); i++) {
                arr[i] = events.get(i);
            }
            this.setChanged();
            this.notifyObservers(arr);
        }
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
