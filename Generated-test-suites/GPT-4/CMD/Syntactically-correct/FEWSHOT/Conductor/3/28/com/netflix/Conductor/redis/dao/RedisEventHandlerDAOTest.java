package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.config.ConductorProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ConductorProperties conductorProperties;
    private ObjectMapper objectMapper;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;


    @Before
    public void setup() {

        jedisProxy = mock(JedisProxy.class);
        conductorProperties = mock(ConductorProperties.class);
        objectMapper = mock(ObjectMapper.class);
        redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();

        eventHandler.setName("EventHandlerName");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventDuplicateHandler() {
        EventHandler eventHandler = new EventHandler();

        eventHandler.setName("EventHandlerName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();

        eventHandler.setName("EventHandlerName");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setEvent("UpdatedEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(2)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistentEventHandler() {
        EventHandler eventHandler = new EventHandler();

        eventHandler.setName("EventHandlerName");
        eventHandler.setEvent("UpdatedEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();

        eventHandler.setName("EventHandlerName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("EventHandlerName");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistentEventHandler() {
        redisEventHandlerDAO.removeEventHandler("NonExistentHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = new LinkedList<>();

        assertEquals(handlers, redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler = new EventHandler();

        eventHandler.setName("EventHandlerName");
        eventHandler.setEvent("Event");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("Event", true);

        assertNotNull(handlers);
    }
}