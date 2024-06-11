
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.LinkedList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;

    @Before
    public void setUp() {
        eventHandler = new EventHandler();
        eventHandler.setName("Test Name");
        eventHandler.setEvent("Test Event");
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing EventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing EventHandler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing EventHandler");
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        LinkedList<String> handlers = new LinkedList<String>();
        handlers.add("test handler 1");
        handlers.add("test handler 2");
        when(jedisProxy.hgetAll(anyString())).thenReturn(handlers);
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        //Implement this method based on your specific needs
    }

}
