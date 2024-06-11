package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;


public class RedisEventHandlerDAOTest {
    
    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private RedisProperties redisProperties;
    
    private RedisEventHandlerDAO dao;
    
    @Before
    public void setup() {
        dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Handler");
        dao.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Handler");
        dao.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Handler");
        dao.removeEventHandler("TestHandler");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.removeEventHandler("TestHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<String> list = new LinkedList<>();
        list.add("Existing Handler");
        
        when(jedisProxy.hgetAll(anyString())).thenReturn(Collections.singletonMap("TestHandler", "Existing Handler"));
        List<EventHandler> handlers = dao.getAllEventHandlers();
        assertTrue(handlers.size() == 1);
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }
    
    // similar tests can be written for other methods depending on the logic
}