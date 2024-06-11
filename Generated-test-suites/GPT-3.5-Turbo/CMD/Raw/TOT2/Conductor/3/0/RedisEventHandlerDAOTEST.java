package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redisd.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redisd.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        when(jedisProxy.hget(any(), any())).thenReturn("{}");
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(any(), any())).thenReturn("{}");
        
        redisEventHandlerDAO.removeEventHandler("testEvent");

        verify(jedisProxy).hdel(any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        
        redisEventHandlerDAO.removeEventHandler("testEvent");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(any())).thenReturn(Map.of("testEvent", "{}"));

        redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy).hgetAll(any());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(any())).thenReturn(Set.of("testEvent"));
        when(jedisProxy.hget(any(), any())).thenReturn("{ \"event\" : \"test\" }");

        redisEventHandlerDAO.getEventHandlersForEvent("test", true);

        verify(jedisProxy).smembers(any());
        verify(jedisProxy).hget(any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        when(jedisProxy.smembers(any())).thenReturn(Set.of("testEvent"));
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.getEventHandlersForEvent("test", false);
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(any(), any())).thenReturn("{ \"event\" : \"test\" }");

        redisEventHandlerDAO.getEventHandler("testEvent");

        verify(jedisProxy).hget(any(), any());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(any(), any())).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler("testEvent");
    }
}