package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {
    private JedisProxy jedisProxy = mock(JedisProxy.class);
    private ConductorProperties conductorProperties = mock(ConductorProperties.class);
    private RedisProperties redisProperties = mock(RedisProperties.class);

    private RedisEventHandlerDAO eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, conductorProperties, redisProperties);

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setActive(true);
        eventHandler.setEvent("testEvent");

        ReflectionTestUtils.invokeMethod(eventHandlerDAO, "addEventHandler", eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setActive(true);
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"test\",\"event\":\"testEvent\"}");
        eventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setActive(true);
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"test\",\"event\":\"testEvent\"}");

        eventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(2)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

   @Test(expected = NotFoundException.class)
    public void testUpdateNonExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setActive(true);
        eventHandler.setEvent("testEvent");

        eventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistingEventHandler() {
        eventHandlerDAO.removeEventHandler("nonExisting");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton("test"));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"test\",\"event\":\"testEvent\"}");

        List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertFalse(eventHandlers.isEmpty());
        assertEquals(1, eventHandlers.size());
        assertEquals("test", eventHandlers.get(0).getName());
        assertEquals("testEvent", eventHandlers.get(0).getEvent());
    }
}