package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties properties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        properties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_event_handler");

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            fail("Expected ConflictException");
        } catch (ConflictException e) {
            // Expected
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_event_handler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "test_event";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_event_handler");

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        String name = "test_event";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.removeEventHandler(name);
            fail("Expected NotFoundException");
        } catch (NotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = new LinkedList<>();
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(handlers, result);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "test_event";
        boolean activeOnly = true;
        when(jedisProxy.smembers(anyString())).thenReturn(null);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertNotNull(result);
    }

    @Test
    public void testGetEventHandler() {
        String name = "test_event";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("event_handler_json");

        EventHandler result = redisEventHandlerDAO.getEventHandler(name);

        assertNotNull(result);
    }

    @Test
    public void testGetEventHandlerTransientException() {
        String name = "test_event";
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException("Error"));

        try {
            redisEventHandlerDAO.getEventHandler(name);
            fail("Expected TransientException");
        } catch (Exception e) {
            // Expected
        }
    }
}