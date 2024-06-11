package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy = mock(JedisProxy.class);
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private RedisProperties properties = mock(RedisProperties.class);

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, null, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn("{}");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestEvent";
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenReturn("{}");
        redisEventHandlerDAO.removeEventHandler(eventName);
        verify(jedisProxy, times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"))).thenReturn(null);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        String key = redisEventHandlerDAO.nsKey("EVENT_HANDLERS_BY_EVENT", eventName);
        when(jedisProxy.smembers(key)).thenReturn(null);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "TestEvent";
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenReturn("{}");
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventName);
        assertNotNull(eventHandler);
        assertEquals(eventName, eventHandler.getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistingEventHandler() {
        String eventName = "TestEvent";
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(eventName);
    }

    @Test
    public void testGetEventHandlerNullJSON() {
        String eventName = "TestEvent";
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenReturn(null);
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventName);
        assertNull(eventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        String eventName = "TestEvent";
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenThrow(new RuntimeException());
        redisEventHandlerDAO.getEventHandler(eventName);
    }
}