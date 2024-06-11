package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy = Mockito.mock(JedisProxy.class);
    
    private RedisEventHandlerDAO redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            fail("Expected a ConflictException to be thrown");
        } catch (ConflictException e) {
            assertEquals("EventHandler with name testEvent already exists!", e.getMessage());
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            fail("Expected a NotFoundException to be thrown");
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name testEvent not found!", e.getMessage());
        }
    }

    @Test
    public void testRemoveEventHandler() {
        try {
            redisEventHandlerDAO.removeEventHandler("testEvent");
            fail("Expected a NotFoundException to be thrown");
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name testEvent not found!", e.getMessage());
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.isEmpty());
    }
    
    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("testEvent");
        assertNull(eventHandler);
    }
}