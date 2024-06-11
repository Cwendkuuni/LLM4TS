package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO dao;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper();
        conductorProperties = new ConductorProperties();
        redisProperties = new RedisProperties();
        dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        dao.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existingEventHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(0L);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");

        dao.addEventHandler(existingEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("testEventHandler");

        dao.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler nonExistingEventHandler = new EventHandler();
        nonExistingEventHandler.setName("nonExistingEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        dao.updateEventHandler(nonExistingEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testEventHandler";

        dao.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String nonExistingName = "nonExistingEventHandler";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        dao.removeEventHandler(nonExistingName);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");

        when(jedisProxy.hgetAll(anyString())).thenReturn(
                Map.of("eventHandler1", "eventHandler1JSON", "eventHandler2", "eventHandler2JSON"));

        List<EventHandler> eventHandlers = dao.getAllEventHandlers();

        assertNotNull(eventHandlers);
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("eventHandler1", "eventHandler2"));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("eventHandler1JSON", "eventHandler2JSON");

        List<EventHandler> eventHandlers = dao.getEventHandlersForEvent(event, true);

        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        String event = "nonExistingEvent";

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of());

        dao.getEventHandlersForEvent(event, true);
    }

    @Test
    public void testGetEventHandler() {
        String name = "testEventHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("testEventHandlerJSON");

        EventHandler retrievedEventHandler = dao.getEventHandler(name);

        assertNotNull(retrievedEventHandler);
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        String name = "testEventHandler";

        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException("Test Exception"));

        dao.getEventHandler(name);
    }
}