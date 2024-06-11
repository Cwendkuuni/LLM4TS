// Sample JUnit 4 test cases for RedisEventHandlerDAO

package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import java.util.List;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        JedisProxy jedisProxy = new JedisProxy();
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties properties = new RedisProperties();

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("TestHandler");
        newEventHandler.setEvent("TestEvent");
        
        redisEventHandlerDAO.addEventHandler(newEventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("TestHandler");
        
        assertEquals(newEventHandler, retrievedEventHandler);
        
        // Test duplicate add
        try {
            redisEventHandlerDAO.addEventHandler(newEventHandler);
            fail("Expected ConflictException was not thrown.");
        } catch (ConflictException e) {
            // Expected
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler testEventHandler = new EventHandler();
        testEventHandler.setName("UpdateHandler");
        testEventHandler.setEvent("UpdateEvent");
        
        redisEventHandlerDAO.addEventHandler(testEventHandler);
        
        testEventHandler.setEvent("UpdatedEvent");
        redisEventHandlerDAO.updateEventHandler(testEventHandler);
        
        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("UpdateHandler");
        
        assertEquals("UpdatedEvent", updatedEventHandler.getEvent());
        
        // Test update non-existent handler
        EventHandler nonExistentHandler = new EventHandler();
        nonExistentHandler.setName("NonExistentHandler");
        
        try {
            redisEventHandlerDAO.updateEventHandler(nonExistentHandler);
            fail("Expected NotFoundException was not thrown.");
        } catch (NotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler testEventHandler = new EventHandler();
        testEventHandler.setName("RemoveHandler");
        testEventHandler.setEvent("RemoveEvent");
        
        redisEventHandlerDAO.addEventHandler(testEventHandler);
        
        redisEventHandlerDAO.removeEventHandler("RemoveHandler");
        EventHandler removedEventHandler = redisEventHandlerDAO.getEventHandler("RemoveHandler");
        
        assertNull(removedEventHandler);
        
        // Test remove non-existent handler
        try {
            redisEventHandlerDAO.removeEventHandler("NonExistentHandler");
            fail("Expected NotFoundException was not thrown.");
        } catch (NotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testgetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("Handler1");
        eventHandler1.setEvent("Event1");
        
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("Handler2");
        eventHandler2.setEvent("Event2");
        
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertEquals(2, allEventHandlers.size());
        assertTrue(allEventHandlers.contains(eventHandler1));
        assertTrue(allEventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("Handler1");
        eventHandler1.setEvent("Event1");
        
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("Handler2");
        eventHandler2.setEvent("Event1");
        
        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("Handler3");
        eventHandler3.setEvent("Event2");
        
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        
        List<EventHandler> event1Handlers = redisEventHandlerDAO.getEventHandlersForEvent("Event1", true);
        
        assertEquals(2, event1Handlers.size());
        assertTrue(event1Handlers.contains(eventHandler1));
        assertTrue(event1Handlers.contains(eventHandler2));
        
        List<EventHandler> event2Handlers = redisEventHandlerDAO.getEventHandlersForEvent("Event2", true);
        
        assertEquals(1, event2Handlers.size());
        assertTrue(event2Handlers.contains(eventHandler3));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler testEventHandler = new EventHandler();
        testEventHandler.setName("TestHandler");
        testEventHandler.setEvent("TestEvent");
        
        redisEventHandlerDAO.addEventHandler(testEventHandler);
        
        EventHandler retrievedHandler = redisEventHandlerDAO.getEventHandler("TestHandler");
        
        assertEquals(testEventHandler, retrievedHandler);
        
        // Test non-existent handler
        EventHandler nonExistentHandler = redisEventHandlerDAO.getEventHandler("NonExistentHandler");
        
        assertNull(nonExistentHandler);
    }
}