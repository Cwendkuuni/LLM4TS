
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;

    @Before
    public void setup() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void addEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            assertNotNull(eventHandler);
            
        } catch (ConflictException ex) {
            fail("Conflict exception should not have been thrown");
        }
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void updateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertNotNull(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void removeEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        try {
            redisEventHandlerDAO.getEventHandler(eventHandler.getName());
            fail("Not Found exception should have been thrown");
        } catch (NotFoundException ex) {
            
        }
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("NonExistingName");
    }

    @Test
    public void getAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(eventHandlers.isEmpty());

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, eventHandlers.size());
        assertEquals("testHandler", eventHandlers.get(0).getName());
    }
    
    // Additional test methods for the rest of the RedisEventHandlerDAO methods will be similar
}
