
package com.netflix.conductor.redis.dao;

import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import java.util.List;

public class RedisEventHandlerDAOTest {
    
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = mock(EventHandler.class);
        JedisProxy jedisProxy = mock(JedisProxy.class); 

        when(eventHandler.getName()).thenReturn("SampleHandler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        dao.addEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = mock(EventHandler.class);
        JedisProxy jedisProxy = mock(JedisProxy.class); 

        when(eventHandler.getName()).thenReturn("SampleHandler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("ExistingHandler");

        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        dao.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = mock(EventHandler.class);
        JedisProxy jedisProxy = mock(JedisProxy.class); 

        when(eventHandler.getName()).thenReturn("SampleHandler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        dao.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String handlerName = "SampleHandler";
        JedisProxy jedisProxy = mock(JedisProxy.class); 

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        dao.removeEventHandler(handlerName);
    }
    
    @Test
    public void testGetAllEventHandlers() {
        JedisProxy jedisProxy = mock(JedisProxy.class); 
        
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        List<EventHandler> handlers = dao.getAllEventHandlers();

        Assert.assertNotNull(handlers);
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "SampleEvent";
        boolean activeOnly = false;
        JedisProxy jedisProxy = mock(JedisProxy.class); 
        
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        List<EventHandler> handlers = dao.getEventHandlersForEvent(eventName, activeOnly);

        Assert.assertNotNull(handlers);
    }
}
