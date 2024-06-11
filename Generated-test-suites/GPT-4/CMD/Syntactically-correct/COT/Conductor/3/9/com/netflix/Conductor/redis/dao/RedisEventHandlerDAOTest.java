package com.netflix.conductor.redis.dao;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO dao;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConductorProperties properties = new ConductorProperties();
        RedisProperties redisProperties = new RedisProperties();
        objectMapper = new ObjectMapper();

        dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, properties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        String eventHandlerName = "test";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        String eventHandlerName = "test";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(objectMapper.writeValueAsString(eventHandler));
        dao.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        String eventHandlerName = "test";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(objectMapper.writeValueAsString(eventHandler));
        dao.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        String eventHandlerName = "test";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String eventHandlerName = "test";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(objectMapper.writeValueAsString(eventHandler));
        dao.removeEventHandler(eventHandlerName);
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }
    
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String eventHandlerName = "test";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.removeEventHandler(eventHandlerName);
    }

    @Test
    public void testGetAllEventHandlers() {
        String eventHandlerName = "test";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventHandlerName);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(objectMapper.writeValueAsString(eventHandler));
        List<EventHandler> handlers = dao.getAllEventHandlers();
        
        assertEquals(1, handlers.size());
        assertEquals(eventHandlerName, handlers.get(0).getName());
    }
    
    @Test
    public void testNoEventHandlersFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        List<EventHandler> handlers = dao.getAllEventHandlers();
        assertTrue(handlers.isEmpty());
    }

    @Test
    public void testNoEventHandlersForSpecificEvent() {
        String eventName = "testEvent";
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton(eventName));
        
        List<EventHandler> handlers = dao.getEventHandlersForEvent(eventName, false);
        assertTrue(handlers.isEmpty());
    }
}