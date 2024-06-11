package com.netflix.conductor.redis.dao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(any())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("testHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        //... Implement your test logic here
    }

    @Test
    public void testGetEventHandlersForEvent() {
        //... Implement your test logic here
    }
}