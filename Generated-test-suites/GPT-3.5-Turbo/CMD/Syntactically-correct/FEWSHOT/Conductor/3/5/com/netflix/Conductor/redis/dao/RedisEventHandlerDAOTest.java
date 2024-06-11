package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"testEvent\"}");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testRemoveEventHandler() {
        String name = "testEvent";
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"testEvent\"}");
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
        
        redisEventHandlerDAO.removeEventHandler(name);
        
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }
    
    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlersMap = Map.of(
            "event1", "{\"name\":\"event1\"}",
            "event2", "{\"name\":\"event2\"}"
        );
        
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlersMap);
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertNotNull(eventHandlers);
        assertEquals(2, eventHandlers.size());
        
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        boolean activeOnly = true;
        
        Set<String> eventHandlerNames = Set.of("event1", "event2");
        
        when(jedisProxy.smembers(anyString())).thenReturn(eventHandlerNames);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"event1\",\"event\":\"testEvent\",\"active\":true}");
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);
        
        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
        
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }
    
    @Test
    public void testGetEventHandler() {
        String name = "testEvent";
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"testEvent\"}");
        
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(name);
        
        assertNotNull(eventHandler);
        assertEquals("testEvent", eventHandler.getName());
        
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }
    
}