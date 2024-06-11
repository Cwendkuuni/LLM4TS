package com.netflix.conductor.redis.dao;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
   
    @InjectMocks
    RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    JedisProxy jedisProxy;
    
    @Mock
    ObjectMapper objectMapper;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    RedisProperties properties;

    @Mock
    EventHandler eventHandler;

    @Test
    public void testAddEventHandler() {
        when(eventHandler.getName()).thenReturn("handleName");
        when(redisEventHandlerDAO.getEventHandler("handleName")).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        when(eventHandler.getName()).thenReturn("handleName");
        when(redisEventHandlerDAO.getEventHandler("handleName")).thenReturn(eventHandler);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);
        
        redisEventHandlerDAO.removeEventHandler("handleName");
        
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.getAllEventHandlers();
        
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>(Arrays.asList("handleName")));
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.getEventHandlersForEvent("eventName", true);
        
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(redisEventHandlerDAO, times(1)).getEventHandler(anyString());
    }
}