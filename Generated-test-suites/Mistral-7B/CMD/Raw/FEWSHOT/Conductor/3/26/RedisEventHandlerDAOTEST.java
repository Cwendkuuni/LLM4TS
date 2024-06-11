package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisTestUtils;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    private RedisProperties properties;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

    @Before
    public void setup() {
        properties = mock(RedisProperties.class);
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);

        when(properties.getRedisNamespace()).thenReturn("namespace");
        when(conductorProperties.getRedisProperties()).thenReturn(properties);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(RedisTestUtils.nsKey(EVENT_HANDLERS), "testEventHandler", Mockito.anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(2)).hset(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler", Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler", Mockito.eq(Mockito.anyString()));
        throw new ConflictException("EventHandler with name testEventHandler already exists!");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testUpdatedEvent");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(2)).hset(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler", Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler", Mockito.eq(Mockito.anyString()));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testUpdatedEvent");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(0)).hset(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler", Mockito.anyString());
        throw new NotFoundException("EventHandler with name testEventHandler not found!");
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy, Mockito.times(2)).hget(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler");
        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy, Mockito.times(0)).hdel(RedisTestUtils.nsKey(EVENT_Handlers), "testEventHandler");
        throw new NotFoundException("EventHandler with name testEventHandler not found!");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");

        when(jedisProxy.hgetAll(RedisTestUtils.nsKey(EVENT_Handlers))).thenReturn(Map.ofEntries(
                Map.entry("testEventHandler1", "testHandler1Json"),
                Map.entry("testEventHandler2", "testHandler2Json")));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals("testEventHandler1", eventHandlers.get(0).getName());
        assertEquals("testEventHandler2", eventHandlers.get(1).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("testEventHandler3");
        eventHandler3.setEvent("testEvent2");
        eventHandler3.setActive(true);

        when(jedisProxy.smembers(RedisTestUtils.nsKey(EVENT_Handlers_BY_EVENT, "testEvent1")))
                .thenReturn(Arrays.asList("testEventHandler1", "testEventHandler2"));
        when(jedisProxy.smembers(RedisTestUtils.nsKey(EVENT_Handlers_BY_EVENT, "testEvent2")))
                .thenReturn(Arrays.asList("testEventHandler3"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(2, eventHandlers.size());
        assertEquals("testEventHandler1", eventHandlers.get(0).getName());
        assertEquals("testEventHandler2", eventHandlers.get(1).getName());

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);

        assertEquals(1, activeEventHandlers.size());
        assertEquals("testEventHandler1", activeEventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() {
        when(jedisProxy.smembers(RedisTestUtils.nsKey(EVENT_Handlers_BY_EVENT, "testEvent1")))
                .thenReturn(Collections.emptySet());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(0, eventHandlers.size());
    }
}