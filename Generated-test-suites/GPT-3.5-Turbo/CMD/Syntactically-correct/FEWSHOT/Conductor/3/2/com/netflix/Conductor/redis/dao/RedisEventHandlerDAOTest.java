package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {
    
    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private RedisProperties redisProperties;
    
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("TestName", "TestEvent", true);
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testAddEventHandlerWithExistingHandler() {
        EventHandler eventHandler = new EventHandler("TestName", "TestEvent", true);
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        
        assertThrows(ConflictException.class, () -> {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        });
    }
    
    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("TestName", "TestEvent", true);
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testUpdateEventHandlerWithNoExistingHandler() {
        EventHandler eventHandler = new EventHandler("TestName", "TestEvent", true);
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
        });
    }
    
    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestName";
        EventHandler eventHandler = new EventHandler(eventName, "TestEvent", true);
        
        when(jedisProxy.hget(anyString(), eq(eventName))).thenReturn("{}");
        
        redisEventHandlerDAO.removeEventHandler(eventName);
        
        verify(jedisProxy, times(1)).hdel(anyString(), eq(eventName));
    }
    
    @Test
    public void testRemoveEventHandlerWithNoExistingHandler() {
        String eventName = "TestName";
        
        when(jedisProxy.hget(anyString(), eq(eventName))).thenReturn(null);
        
        assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.removeEventHandler(eventName);
        });
    }
    
    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(new java.util.HashMap<String, String>());
        
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertNotNull(handlers);
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        
        when(jedisProxy.smembers(anyString())).thenReturn(new java.util.HashSet<String>());
        
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);
        
        assertNotNull(handlers);
    }
}