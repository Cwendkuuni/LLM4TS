/*
 * Copyright 2022 Netflix, Inc.
 */
package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO underTest;
    private JedisProxy jedisProxy;

    @Before
    public void setup() {
        jedisProxy = Mockito.mock(JedisProxy.class);

        underTest = new RedisEventHandlerDAO(jedisProxy, null, null, new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("TestHandler");

        underTest.addEventHandler(handler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("TestHandler");

        underTest.updateEventHandler(handler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNonExistent() {
        underTest.removeEventHandler("TestHandler");
    }

    @Test
    public void testRemoveEventHandlerExisting() {
        EventHandler handler = new EventHandler();
        handler.setName("TestHandler");

        underTest.addEventHandler(handler);

        underTest.removeEventHandler("TestHandler");

        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlersEmpty() {
        underTest.getAllEventHandlers();

        Mockito.verify(jedisProxy).hgetAll(Mockito.anyString());
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        underTest.getEventHandlersForEvent("TestEvent", false);

        Mockito.verify(jedisProxy).smembers(Mockito.anyString());
    }

}