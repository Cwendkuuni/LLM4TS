package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        // Assuming a default Objectmapper and properties are provided
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset(anyString(), eq("testEvent"), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(redisEventHandlerDAO.getEventHandler("testEvent")).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");
        when(redisEventHandlerDAO.getEventHandler("testEvent")).thenReturn(eventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy).hset(anyString(), eq("testEvent"), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(redisEventHandlerDAO.getEventHandler("testEvent")).thenReturn(eventHandler);
        redisEventHandlerDAO.removeEventHandler("testEvent");
        verify(jedisProxy).hdel(anyString(), eq("testEvent"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        redisEventHandlerDAO.removeEventHandler("testEvent");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(new HashMap<>());
        assertTrue(redisEventHandlerDAO.getAllEventHandlers().isEmpty());
    }

    // Continue with remaining tests for other methods
}