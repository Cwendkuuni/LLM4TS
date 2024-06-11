
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
    @InjectMocks
    private RedisEventHandlerDAO redisDao;
    
    @Mock
    private JedisProxy jedisProxy;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("test");
        handler.setActive(true);
        handler.setEvent("dummyEvent");
        
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisDao.addEventHandler(handler);
        
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExists() {
        EventHandler handler = new EventHandler();
        handler.setName("test");
        handler.setActive(true);
        handler.setEvent("dummyEvent");
        
        when(jedisProxy.hget(any(), any())).thenReturn("dummyData");
        redisDao.addEventHandler(handler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("test");
        handler.setActive(true);
        handler.setEvent("dummyEvent");
        
        when(jedisProxy.hget(any(), any())).thenReturn("dummyData");
        redisDao.removeEventHandler("test");

        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFound() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisDao.removeEventHandler("test");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("test");
        handler.setActive(true);
        handler.setEvent("dummyEvent");
        
        String jsonString = "{...}";
        when(jedisProxy.hget(any(), any())).thenReturn(jsonString);
        redisDao.updateEventHandler(handler);

        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler handler = new EventHandler();
        handler.setName("test");
        handler.setActive(true);
        handler.setEvent("dummyEvent");
        
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisDao.updateEventHandler(handler);
    }

    @Test
    public void getAllEventHandlersTest() {
        when(jedisProxy.hgetAll(any())).thenReturn(DummyData);
        
        List<EventHandler> result = redisDao.getAllEventHandlers();

        assertNotNull(result);
        assertEquals(result.size(), DummyData.size());
    }

    @Test
    public void getEventHandlersForEventTest() {
        String event = "dummyEvent";
        Boolean activeOnly = true;

        when(jedisProxy.smembers(any())).thenReturn(dummySet);
        when(jedisProxy.hget(any(), any())).thenReturn(dummyData);
        
        List<EventHandler> result = redisDao.getEventHandlersForEvent(event, activeOnly);

        assertNotNull(result);
    }
}
 