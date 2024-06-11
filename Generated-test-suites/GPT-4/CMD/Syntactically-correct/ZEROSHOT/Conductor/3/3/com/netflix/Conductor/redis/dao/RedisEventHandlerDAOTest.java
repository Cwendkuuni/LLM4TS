package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxyMock;
    private EventHandler eventHandlerMock;

    @Before
    public void setUp() {
        jedisProxyMock = mock(JedisProxy.class);
        eventHandlerMock = mock(EventHandler.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, null, null, null);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        when(eventHandlerMock.getName()).thenReturn("TestHandler");
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("TestHandler");
        redisEventHandlerDAO.addEventHandler(eventHandlerMock);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        when(eventHandlerMock.getName()).thenReturn("TestHandler");
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandlerMock);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("TestHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxyMock.hgetAll(anyString())).thenReturn(null);
        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNameNotFound() {
        when(jedisProxyMock.smembers(anyString())).thenReturn(null);
        redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", false);
    }
}
