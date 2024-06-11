package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO dao;
    private JedisProxy jedisProxy;

    @Before
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        RedisProperties redisProperties = Mockito.mock(RedisProperties.class);
        dao = new RedisEventHandlerDAO(jedisProxy, null, conductorProperties, redisProperties);
    }

    @After
    public void tearDown() {
        dao = null;
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("something");
        dao.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("something");
        dao.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("something");
        dao.removeEventHandler("test");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.removeEventHandler("test");
    }

    @Test
    public void testGetAllEventHandlers() {
        String eventHandlerStr = "{\"name\":\"test\"}";
        Map<String, String> handlers = new HashMap<>();
        handlers.put("test", eventHandlerStr);
        when(jedisProxy.hgetAll(anyString())).thenReturn(handlers);
        List<EventHandler> result = dao.getAllEventHandlers();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetEventHandlerForEvent() {
        Set<String> names = new HashSet<>();
        names.add("test");
        when(jedisProxy.smembers(anyString())).thenReturn(names);
        List<EventHandler> result = dao.getEventHandlersForEvent("test", false);
        assertEquals(1, result.size());
    }
}