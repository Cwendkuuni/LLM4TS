
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;
    
    @Before
    public void setUp() {
        eventHandler = new EventHandler();
        eventHandler.setName("test_name");
        eventHandler.setActive(true);
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("eventHandlerJson");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("eventHandlerJson");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("eventHandlerJson");
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlerList = new ArrayList<>();
        eventHandlerList.add(eventHandler);
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlerList);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(handlers.size(), 1);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlerList = new ArrayList<>();
        eventHandlerList.add(eventHandler);
        when(jedisProxy.smembers(anyString())).thenReturn(eventHandlerList);
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler.getEvent(),true);
        assertEquals(handlers.size(), 1);
    }
}
