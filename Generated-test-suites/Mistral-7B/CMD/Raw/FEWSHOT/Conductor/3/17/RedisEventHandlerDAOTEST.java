package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseRedisTest;
import com.netflix.conductor.redis.test.TestObjects;
import com.netflix.conductor.redis.util.RedisTestUtils;
import com.netflix.conductor.redis.util.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.type.TypeReference;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseRedisTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = Mockito.spy(new ObjectMapper());
        conductorProperties = Mockito.spy(new ConductorProperties());
        redisProperties = Mockito.spy(new RedisProperties());
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandlerSuccess() {
        EventHandler eventHandler = TestObjects.createEventHandler("test");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = TestObjects.createEventHandler("test");
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("test"))).thenReturn("test");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("test"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerNullName() {
        EventHandler eventHandler = TestObjects.createEventHandler(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verifyNoInteractions(jedisProxy);
        verify(objectMapper, times(1)).writeValueAsString(eventHandler);
    }

    @Test
    public void testGetAllEventHandlersSuccess() {
        EventHandler eventHandler1 = TestObjects.createEventHandler("test1");
        EventHandler eventHandler2 = TestObjects.createEventHandler("test2");

        Map<String, String> all = Map.of(
                "test1", TestUtils.toJson(eventHandler1),
                "test2", TestUtils.toJson(eventHandler2)
        );

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(all);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));
    }

    @Test
    public void testUpdateEventHandlerSuccess() {
        EventHandler existingEventHandler = TestObjects.createEventHandler("test");
        EventHandler updatedEventHandler = TestObjects.createEventHandler("test_updated");
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("test"))).thenReturn(TestUtils.toJson(existingEventHandler));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("test"));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("test"), eq(TestUtils.toJson(updatedEventHandler)));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler updatedEventHandler = TestObjects.createEventHandler("test_updated");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("test"))).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("test"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNullName() {
        EventHandler updatedEventHandler = TestObjects.createEventHandler(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verifyNoInteractions(jedisProxy);
    }

    @Test
    public void testRemoveEventHandlerSuccess() {
        String name = "test";
        EventHandler eventHandler = TestObjects.createEventHandler(name);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(name));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "test";

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(name));
    }

    @Test
    public void testGetEventHandlersForEventSuccess() {
        String event = "test_event";
        List<EventHandler> eventHandlers = Arrays.asList(
                TestObjects.createEventHandler("test1_event"),
                TestObjects.createEventHandler("test2_event")
        );

        Set<String> names = RedisTestUtils.createSet(eventHandlers);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertEquals(2, handlers.size());
        assertTrue(handlers.containsAll(eventHandlers));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        String event = "test_event";

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(Collections.emptySet());

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertTrue(handlers.isEmpty());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlySuccess() {
        String event = "test_event";
        List<EventHandler> eventHandlers = Arrays.asList(
                TestObjects.createEventHandlerWithStatus("test1_event", true),
                TestObjects.createEventHandlerWithStatus("test2_event", false)
        );

        Set<String> names = RedisTestUtils.createSet(eventHandlers);

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertEquals(1, handlers.size());
        assertTrue(handlers.contains(TestObjects.createEventHandlerWithStatus("test1_event", true)));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyEmpty() {
        String event = "test_event";

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(Collections.emptySet());

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertTrue(handlers.isEmpty());
    }
}