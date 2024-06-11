package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private final JedisProxy jedisProxy = mock(JedisProxy.class); 
    private final RedisEventHandlerDAO redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventExistingHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget(any(), any())).thenReturn("exists");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(any())).thenReturn(null);
        List<EventHandler> list = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(list);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("test");
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(any(), any())).thenReturn("exists");
        redisEventHandlerDAO.removeEventHandler("test");
        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget(any(), any())).thenReturn("exists");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(any())).thenReturn(new LinkedList<>());
        List<EventHandler> list = redisEventHandlerDAO.getEventHandlersForEvent("test", true);
        assertNotNull(list);
    }
}