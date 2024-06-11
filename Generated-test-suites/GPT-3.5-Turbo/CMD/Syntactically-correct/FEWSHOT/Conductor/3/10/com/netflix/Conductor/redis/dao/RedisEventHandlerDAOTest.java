package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("Test Event JSON");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "testEvent";

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("Test Event JSON");

        redisEventHandlerDAO.removeEventHandler(eventName);

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");

        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(Map.of("testEvent", "Test Event JSON"));

        List<EventHandler> allHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(allHandlers);
        assertEquals(1, allHandlers.size());
        assertEquals("testEvent", allHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "test";
        boolean activeOnly = true;

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent(event);
        eventHandler.setActive(true);

        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(Set.of("testEvent"));
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("Test Event JSON");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
        assertEquals("testEvent", eventHandlers.get(0).getName());
    }
}