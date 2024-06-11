package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");

        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("testEvent");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(handlers);
        assertEquals(0, handlers.size());

        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(null);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("test", true);

        assertNotNull(handlers);
        assertEquals(0, handlers.size());

        verify(jedisProxy, times(1)).smembers(anyString());
    }

}