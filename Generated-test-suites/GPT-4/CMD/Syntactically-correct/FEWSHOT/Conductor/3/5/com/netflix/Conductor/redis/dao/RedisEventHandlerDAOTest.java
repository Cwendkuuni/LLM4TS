package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RedisEventHandlerDAOTest {
    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        assertNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        assertNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
        redisEventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(eventHandler.getName()));

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        assertNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(Arrays.asList(eventHandler1, eventHandler2), redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler activeEventHandler = new EventHandler();
        activeEventHandler.setName("TestActiveEventHandler");
        activeEventHandler.setActive(true);
        activeEventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(activeEventHandler);

        assertEquals(Arrays.asList(activeEventHandler), redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true));
    }
}