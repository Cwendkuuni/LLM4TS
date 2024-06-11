package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        JedisProxy jedisProxy = new JedisProxy();
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties redisProperties = new RedisProperties();
        
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("testEvent"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventUpdate");
        eventHandler.setEvent("testUpdate");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setEvent("updatedEvent");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("testEventUpdate");

        assertEquals("updatedEvent", updatedEventHandler.getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventRemove");
        eventHandler.setEvent("testRemove");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventRemove");

        assertNull(redisEventHandlerDAO.getEventHandler("testEventRemove"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEvent1");
        eventHandler1.setEvent("test1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEvent2");
        eventHandler2.setEvent("test2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEvent1");
        eventHandler1.setEvent("testEvent");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEvent2");
        eventHandler2.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertEquals(2, eventHandlers.size());
    }
}