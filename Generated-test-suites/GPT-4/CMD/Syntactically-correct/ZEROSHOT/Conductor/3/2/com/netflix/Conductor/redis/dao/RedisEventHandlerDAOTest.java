package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

public class RedisEventHandlerDAOTest {
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private EventHandler eventHandler;
    
    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), 
                                                        new ConductorProperties(), new RedisProperties());
        eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");
        eventHandler.setActive(true);
        eventHandler.setEvent("Test");
    }
    
    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
    
    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }
    
    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingHandler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("TestHandler");
    }
    
    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingHandler");
        redisEventHandlerDAO.removeEventHandler("TestHandler");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }
    
    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.getAllEventHandlers();
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        redisEventHandlerDAO.getEventHandlersForEvent("Test", true);
        verify(jedisProxy, times(1)).smembers(anyString());
    }
}
