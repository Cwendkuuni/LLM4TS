package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import java.util.*;

public class RedisEventHandlerDAOTest {
    
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    
    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("Test", "TestEvent", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("TestEventJson");
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), eq("TestEventJson"));
    }
    
    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler("Test", "TestEvent", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingEventJson");
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
    
    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("Test", "TestEvent", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingEventJson");
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("TestEventJson");
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), eq("TestEventJson"));
    }
    
    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler("Test", "TestEvent", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }
    
    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingEventJson");
        
        redisEventHandlerDAO.removeEventHandler("Test");
        
        verify(jedisProxy, times(1)).hdel(anyString(), eq("Test"));
    }
    
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.removeEventHandler("Test");
    }
    
    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = new HashMap<>();
        all.put("Test1", "{\"name\":\"Test1\",\"event\":\"TestEvent1\",\"active\":true}");
        all.put("Test2", "{\"name\":\"Test2\",\"event\":\"TestEvent2\",\"active\":false}");
        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertNotNull(handlers);
        assertEquals(2, handlers.size());
    }
    
    @Test
    public void testIndex() {
        EventHandler eventHandler = new EventHandler("Test", "TestEvent", true);
        
        redisEventHandlerDAO.index(eventHandler);
        
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
    }
    
    @Test
    public void testRemoveIndex() {
        EventHandler eventHandler = new EventHandler("Test", "TestEvent", true);
        
        redisEventHandlerDAO.removeIndex(eventHandler);
        
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> names = new HashSet<>();
        names.add("Test1");
        names.add("Test2");
        when(jedisProxy.smembers(anyString())).thenReturn(names);
        when(jedisProxy.hget(anyString(), eq("Test1"))).thenReturn("{\"name\":\"Test1\",\"event\":\"TestEvent\",\"active\":true}");
        when(jedisProxy.hget(anyString(), eq("Test2"))).thenReturn(null);
        
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);
        
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
    }
    
    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        Set<String> names = new HashSet<>();
        names.add("Test1");
        when(jedisProxy.smembers(anyString())).thenReturn(names);
        when(jedisProxy.hget(anyString(), eq("Test1"))).thenReturn("{\"name\":\"Test1\",\"event\":\"TestEvent\",\"active\":true}");
        
        redisEventHandlerDAO.getEventHandlersForEvent("TestEventNotFound", true);
    }
    
    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(anyString(), eq("Test"))).thenReturn("{\"name\":\"Test\",\"event\":\"TestEvent\",\"active\":true}");
        
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("Test");
        
        assertNotNull(eventHandler);
        assertEquals("Test", eventHandler.getName());
        assertEquals("TestEvent", eventHandler.getEvent());
        assertTrue(eventHandler.isActive());
    }
    
    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(anyString(), eq("Test"))).thenThrow(new RuntimeException());
        
        redisEventHandlerDAO.getEventHandler("Test");
    }
}