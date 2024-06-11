
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy mockJedisProxy;

    @Mock
    private ConductorProperties mockConductorProperties;

    @Mock
    private RedisProperties mockRedisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(mockJedisProxy, new Json(), mockConductorProperties, mockRedisProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn("Existing Handler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(mockJedisProxy, times(1)).hset(any(String.class), any(String.class), any(String.class));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn("Existing Handler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(mockJedisProxy, times(1)).hset(any(String.class), any(String.class), any(String.class));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFound() {
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("testHandler");
    }

    @Test
    public void testRemoveEventHandler() {
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn("Existing Handler");
        redisEventHandlerDAO.removeEventHandler("testHandler");
        verify(mockJedisProxy, times(1)).hdel(any(String.class), any(String.class));
    }

    @Test
    public void testGetAllEventHandlers() {
        HashMap<String, String> allHandlers = new HashMap<>();
        allHandlers.put("handler1", "{}");
        allHandlers.put("handler2", "{}");
        when(mockJedisProxy.hgetAll(any(String.class))).thenReturn(allHandlers);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> handlerNames = new HashSet<>();
        handlerNames.add("handler1");
        handlerNames.add("handler2");
        when(mockJedisProxy.smembers(any(String.class))).thenReturn(handlerNames);
        when(mockJedisProxy.hget(any(String.class), any(String.class))).thenReturn("Existing Handler");
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(2, handlers.size());
    }
}
