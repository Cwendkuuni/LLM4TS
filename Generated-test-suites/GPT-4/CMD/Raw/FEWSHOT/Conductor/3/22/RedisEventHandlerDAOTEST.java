package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget("EVENT_HANDLERS", "test")).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset("EVENT_HANDLERS", "test", toJson(eventHandler));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget("EVENT_HANDLERS", "test")).thenReturn("existing");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");

        when(jedisProxy.hget("EVENT_HANDLERS", "test")).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy).hset("EVENT_HANDLERS", "test", toJson(eventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");

        when(jedisProxy.hget("EVENT_HANDLERS", "test")).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "test";

        when(jedisProxy.hget("EVENT_HANDLERS", name)).thenReturn("existing");
        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisProxy).hdel("EVENT_HANDLERS", name);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        String name = "test";

        when(jedisProxy.hget("EVENT_HANDLERS", name)).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetEventHandler() {
        String name = "test";
        String json = toJson(new EventHandler());

        when(jedisProxy.hget("EVENT_HANDLERS", name)).thenReturn(json);
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(name);
        assertNotNull(eventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandler_Exception() {
        String name = "test";

        doThrow(new Exception()).when(jedisProxy).hget("EVENT_HANDLERS", name);
        redisEventHandlerDAO.getEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        String name = "test";
        String json = toJson(new EventHandler());

        when(jedisProxy.hgetAll("EVENT_HANDLERS")).thenReturn(Collections.singletonMap(name, json));
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, handlers.size());
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setEvent("testEvent");
        String json = toJson(eventHandler);
        when(jedisProxy.hget("EVENT_HANDLERS", eventHandler.getName())).thenReturn(json);
        
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler.getEvent(), false);
        assertTrue(handlers.size() > 0);
        assertTrue(handlers.contains(eventHandler));
    }
}