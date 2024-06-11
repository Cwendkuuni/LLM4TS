package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jedisProxy).sadd(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jedisProxy).sadd(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");

        redisEventHandlerDAO.removeEventHandler("name");

        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jedisProxy).srem(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(handlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event", true);

        assertNotNull(handlers);
    }
}