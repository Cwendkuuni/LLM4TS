package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event_handler");

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler")).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler", redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event_handler");

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler")).thenReturn(redisEventHandlerDAO.toJson(eventHandler));

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler", redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event_handler");

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler")).thenReturn(redisEventHandlerDAO.toJson(eventHandler));

        redisEventHandlerDAO.removeEventHandler("test_event_handler");

        verify(jedisProxy).hdel(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(null);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "test_event";
        boolean activeOnly = true;

        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event))).thenReturn(null);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertNull(eventHandlers);
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_event_handler");

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test_event_handler")).thenReturn(redisEventHandlerDAO.toJson(eventHandler));

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("test_event_handler");

        assertNotNull(retrievedEventHandler);
        assertEquals("test_event_handler", retrievedEventHandler.getName());
    }
}