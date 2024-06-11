/*
 * Copyright 2022 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.config.ConductorProperties;

import java.util.Arrays;

public class RedisEventHandlerDAOTest {
    @Mock
    JedisProxy jedisProxy;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEventHandler() {

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");

        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("EventHandler JSON string");
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
      
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(2)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("EventHandler JSON string");
        doNothing().when(jedisProxy).hdel(anyString(), anyString());
      
        redisEventHandlerDAO.removeEventHandler("name");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {

        when(jedisProxy.hgetAll(anyString())).thenReturn(Arrays.asList("event1", "event2", "event3"));
        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());

        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Arrays.asList("eventHandler1", "eventHandler2", "eventHandler3"));
        assertNotNull(redisEventHandlerDAO.getEventHandlersForEvent("event", true));

        verify(jedisProxy, times(1)).smembers(anyString());
    }
}