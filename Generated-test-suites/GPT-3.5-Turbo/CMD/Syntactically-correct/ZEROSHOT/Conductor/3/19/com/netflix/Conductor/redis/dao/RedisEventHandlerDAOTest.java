package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
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
    public void init() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        doReturn(null).when(jedisProxy).hget(anyString(), anyString());
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        doReturn("{}").when(jedisProxy).hget(anyString(), anyString());
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        doReturn("{}").when(jedisProxy).hget(anyString(), anyString());
        doNothing().when(jedisProxy).hdel(anyString(), anyString());
        redisEventHandlerDAO.removeEventHandler("TestEvent");
    }

    @Test
    public void testGetAllEventHandlers() {
        doReturn(null).when(jedisProxy).hgetAll(anyString());
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        doReturn(null).when(jedisProxy).smembers(anyString());
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);
        assertNotNull(eventHandlers);
    }
}