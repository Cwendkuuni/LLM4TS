
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO dao;
    private JedisProxy jedisProxy;
    private EventHandler handler1;
    
    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        handler1 = new EventHandler();
        handler1.setName("test1");
        handler1.setActive(true);
        handler1.setEvent("event1");
    }
    
    @Test
    public void addEventHandlerTest() {
        dao.addEventHandler(handler1);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test(expected = ConflictException.class)
    public void addEventHandlerConflictTest() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Something");
        dao.addEventHandler(handler1);
    }
    
    @Test
    public void updateEventHandlerTest() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Something");
        dao.updateEventHandler(handler1);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }
    
    @Test(expected = NotFoundException.class)
    public void updateEventHandlerNotFoundTest() {
        dao.updateEventHandler(handler1);
    }
    
    @Test
    public void removeEventHandlerTest() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Something");
        dao.removeEventHandler("test1");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }
    
    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFoundTest() {
        dao.removeEventHandler("test1");
    }
    
    @Test
    public void getAllEventHandlersTest() {
        Map<String, String> all = mock(Map.class);
        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        List<EventHandler> result = dao.getAllEventHandlers();
        assertNotNull(result);
    }
    
    @Test
    public void getEventHandlersForEventTest() {
        when(jedisProxy.smembers(anyString())).thenReturn(anySet());
        List<EventHandler> result = dao.getEventHandlersForEvent("event1", true);
        assertNotNull(result);
    }
    
}
