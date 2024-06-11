package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseTest;
import com.netflix.conductor.test.Eventhandlers;
import com.netflix.conductor.test.TestEventhandlers;
import com.netflix.conductor.test.TestEvents;
import com.netflix.conductor.test.TestProperties;
import com.netflix.conductor.test.TestRedisProperties;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private EventHandlerDAO redisEventHandlerDAO;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = new TestProperties();
        redisProperties = new TestRedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = Eventhandlers.getEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals(TestEventhandlers.getEventHandlerName(), eventHandler.getName());
        assertEquals(TestEventhandlers.getEventHandlerEvent(), eventHandler.getEvent());
        assertEquals(TestEventhandlers.getEventHandlerClass(), eventHandler.getClass().getName());
        assertNotNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = Eventhandlers.getEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler existingEventHandler = Eventhandlers.getEventHandler();
        redisEventHandlerDAO.addEventHandler(existingEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = Eventhandlers.getEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler updatedEventHandler = new EventHandler(
                "updatedName", eventHandler.getEvent(), eventHandler.getClass());
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        assertEquals("updatedName", jedisProxy.hget(nsKey(EVENT_HANDLERS), "updatedName"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler updatedEventHandler = new EventHandler("nonExistentName", TestEvents.EVENT_NAME, Eventhandlers.getEventHandlerClass());
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = Eventhandlers.getEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        redisEventHandlerDAO.removeEventHandler("nonExistentName");
    }

    @Test
    public void testGetAllEventHandlers() {
        for (EventHandler eventHandler : Eventhandlers.getEventHandlers()) {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        }
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        for (EventHandler eventHandler : eventHandlers) {
            assertEquals(eventHandler.getClass().getName(), eventHandler.getClass().getName());
        }
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("handler1", TestEvents.EVENT_NAME, Eventhandlers.getEventHandlerClass());
        EventHandler eventHandler2 = new EventHandler("handler2", TestEvents.EVENT_NAME, Eventhandlers.getEventHandlerClass());
        EventHandler eventHandler3 = new EventHandler("handler3", "otherEvent", Eventhandlers.getEventHandlerClass());

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TestEvents.EVENT_NAME, false);
        assertEquals(2, eventHandlers.size());
        eventHandlers.forEach(eventHandler -> assertEquals(TestEvents.EVENT_NAME, eventHandler.getEvent()));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TestEvents.EVENT_NAME, true);
        assertEquals(1, activeEventHandlers.size());
        assertEquals(eventHandler1.getName(), activeEventHandlers.get(0).getName());
        assertTrue(eventHandler1.isActive());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = Eventhandlers.getEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNonExistentName() {
        redisEventHandlerDAO.getEventHandler("nonExistentName");
    }

    @Test
    public void testIndexEventHandler() {
        EventHandler eventHandler = new EventHandler("handlerName", TestEvents.EVENT_NAME, Eventhandlers.getEventHandlerClass());
        redisEventHandlerDAO.index(eventHandler);
        Set<String> names = jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, TestEvents.EVENT_NAME));
        assertEquals(1, names.size());
        assertEquals(eventHandler.getName(), names.iterator().next());
    }

    @Test
    public void testRemoveIndexEventHandler() {
        EventHandler eventHandler = new EventHandler("handlerName", TestEvents.EVENT_NAME, Eventhandlers.getEventHandlerClass());
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Set<String> names = jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, TestEvents.EVENT_NAME));
        assertEquals(1, names.size());
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        Set<String> namesAfterRemoval = jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, TestEvents.EVENT_NAME));
        assertEquals(0, namesAfterRemoval.size());
    }
}