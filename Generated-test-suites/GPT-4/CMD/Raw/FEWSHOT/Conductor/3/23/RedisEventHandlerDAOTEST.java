
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"test-handler\"}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-handler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonexistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("nonexisting-handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Collections.singletonMap("test-handler", "{\"name\":\"test-handler\"}"));

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, allEventHandlers.size());
        assertEquals("test-handler", allEventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton("test-handler"));

        List<EventHandler> handlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("test-event", true);

        assertTrue(handlersForEvent.isEmpty());
    }
}
