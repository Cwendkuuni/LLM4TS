package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();

        jedisProxy = mock(JedisProxy.class);
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties redisProperties = new RedisProperties();

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy,
                objectMapper, conductorProperties, redisProperties);
    }
    
    @Test
    public void testAddEventHandler() {
    	EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");

        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn("{\"name\":\"test\"}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
    
    @Test
    public void testUpdateEventHandler() {
    	EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setEvent("eventType");
        
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn("{\"name\":\"test\",\"event\":\"eventType\"}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }
    
    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn("{\"name\":\"test\"}");

        redisEventHandlerDAO.removeEventHandler("test");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("test");
    }
    
    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(
                new HashMap<String, String>() {{
                    put("test", "{\"name\":\"test\"}");
                }});

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        Assert.assertEquals(1, handlers.size());
        Assert.assertEquals("test", handlers.get(0).getName());
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton("test"));
        when(jedisProxy.hget(anyString(), anyString()))
        .thenReturn("{\"name\":\"test\", \"event\":\"eventType\", \"active\":true}");

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("eventType", true);

        Assert.assertEquals(1, handlers.size());
        Assert.assertEquals("test", handlers.get(0).getName());
    }
}
