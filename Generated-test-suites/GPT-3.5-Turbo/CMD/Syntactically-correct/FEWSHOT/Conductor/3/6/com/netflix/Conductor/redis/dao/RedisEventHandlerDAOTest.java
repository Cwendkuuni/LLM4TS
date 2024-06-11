package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties properties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper();
        conductorProperties = new ConductorProperties();
        properties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(jedisProxy.hget("namespace", "EVENT_HANDLERS")).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset("namespace", "EVENT_HANDLERS", "{\"name\":\"testEvent\"}");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(jedisProxy.hget("namespace", "EVENT_HANDLERS")).thenReturn("{\"name\":\"testEvent\"}");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset("namespace", "EVENT_HANDLERS", "{\"name\":\"testEvent\"}");
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget("namespace", "EVENT_HANDLERS")).thenReturn("{\"name\":\"testEvent\"}");
        redisEventHandlerDAO.removeEventHandler("testEvent");
        verify(jedisProxy, times(1)).hdel("namespace", "EVENT_HANDLERS");
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> allHandlers = Map.of("testEvent", "{\"name\":\"testEvent\"}");
        when(jedisProxy.hgetAll("namespace", "EVENT_HANDLERS")).thenReturn(allHandlers);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
        assertEquals("testEvent", handlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> eventHandlers = Set.of("testEvent");
        when(jedisProxy.smembers("namespace", "EVENT_HANDLERS_BY_EVENT", "testEvent")).thenReturn(eventHandlers);
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(jedisProxy.hget("namespace", "EVENT_HANDLERS", "testEvent")).thenReturn("{\"name\":\"testEvent\"}");
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
        assertEquals("testEvent", handlers.get(0).getName());
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget("namespace", "EVENT_HANDLERS", "testEvent")).thenReturn("{\"name\":\"testEvent\"}");
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("testEvent");
        assertNotNull(eventHandler);
        assertEquals("testEvent", eventHandler.getName());
    }

}