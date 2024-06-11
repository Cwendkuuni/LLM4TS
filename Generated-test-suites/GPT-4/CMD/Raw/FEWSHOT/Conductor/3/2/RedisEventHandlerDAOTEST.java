package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxyMock;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        jedisProxyMock = mock(JedisProxy.class);
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties redisProperties = new RedisProperties();
        objectMapper = new ObjectMapper();

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, objectMapper, conductorProperties, redisProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        
        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn("something");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testAddEventHandler_Success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxyMock, times(1)).hset(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName(), redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        String name = "testHandler";

        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), name)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testRemoveEventHandler_Success() {
        String name = "testHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        String json = redisEventHandlerDAO.toJson(eventHandler);

        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), name)).thenReturn(json);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxyMock, times(1)).hdel(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), name);
    }
    
    // Test remaining methods here
}