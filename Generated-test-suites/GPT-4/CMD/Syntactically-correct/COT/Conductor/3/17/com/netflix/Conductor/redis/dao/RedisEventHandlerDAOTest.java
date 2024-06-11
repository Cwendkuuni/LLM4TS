package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper();

        // initialise class under test
        redisHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, new ConductorProperties(), new RedisProperties());
    }

    @Test(expected = NullPointerException.class)
    public void testAddEventHandlerNull() {
        redisHandlerDAO.addEventHandler(null);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        // assume getEventHandler returns a non-null value to simulate an existing event
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("someValue");
        redisHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        // EventHandler does not exist
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        // EventHandler does not exist
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerJedisFailure() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
        redisHandlerDAO.getEventHandlersForEvent("event1", true);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());
        redisHandlerDAO.getEventHandlersForEvent("event1", true);
    }
}
