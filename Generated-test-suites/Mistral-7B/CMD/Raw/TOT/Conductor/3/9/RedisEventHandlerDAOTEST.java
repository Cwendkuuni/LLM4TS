package com.netflix.conductor.redis.dao;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq("testEventHandler"), Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertEquals("testEventHandler", retrievedEventHandler.getName());
        assertEquals("testEvent", retrievedEventHandler.getEvent());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(2)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setIsActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setIsActive(false);

        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(Lists.newLinkedList("testEventHandler1", "testEventHandler2"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(1, eventHandlers.size());
        assertEquals("testEventHandler1", eventHandlers.get(0).getName());
        assertEquals("testEvent", eventHandlers.get(0).getEvent());
        assertTrue(eventHandlers.get(0).isActive());

        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(2, eventHandlersForEvent.size());
        assertEquals("testEventHandler1", eventHandlersForEvent.get(0).getName());
        assertEquals("testEvent", eventHandlersForEvent.get(0).getEvent());
        assertFalse(eventHandlersForEvent.get(0).isActive());
        assertEquals("testEventHandler2", eventHandlersForEvent.get(1).getName());
        assertEquals("testEvent", eventHandlersForEvent.get(1).getEvent());
        assertFalse(eventHandlersForEvent.get(1).isActive());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler("testEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setIsActive(true);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("updatedTestEvent");
        updatedEventHandler.setIsActive(false);

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(toJson(existingEventHandler));
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq("testEventHandler"), Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertEquals("testEventHandler", retrievedEventHandler.getName());
        assertEquals("updatedTestEvent", retrievedEventHandler.getEvent());
        assertFalse(retrievedEventHandler.isActive());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.eq("testEventHandler"));
        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
        assertNull(redisEventHandlerDAO.getEventHandler("testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("testEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setIsActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setIsActive(false);

        Map<String, String> all = new LinkedHashMap<>();
        all.put("testEventHandler1", toJson(eventHandler1));
        all.put("testEventHandler2", toJson(eventHandler2));

        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertEquals("testEventHandler1", eventHandlers.get(0).getName());
        assertEquals("testEvent", eventHandlers.get(0).getEvent());
        assertTrue(eventHandlers.get(0).isActive());
        assertEquals("testEventHandler2", eventHandlers.get(1).getName());
        assertEquals("testEvent", eventHandlers.get(1).getEvent());
        assertFalse(eventHandlers.get(1).isActive());
    }
}