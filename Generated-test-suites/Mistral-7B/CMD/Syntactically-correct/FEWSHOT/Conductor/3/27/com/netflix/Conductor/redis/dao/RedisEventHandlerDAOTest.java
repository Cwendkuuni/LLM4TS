package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.RedisUtil;
import com.netflix.conductor.test.TestUtils;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private Logger logger;

    @InjectMocks
    private RedisEventHandlerDAO dao;

    private RedisUtil redisUtil;

    @Before
    public void setup() {
        redisUtil = new RedisUtil(jedisProxy);
        when(jedisProxy.hgetAll(anyString())).thenReturn(ImmutableMap.of());
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn((String) null);
        when(jedisProxy.sadd(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.srem(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.smembers(anyString())).thenReturn(ImmutableSet.of("handler1", "handler2"));

        when(conductorProperties.getString("redis.namespace")).thenReturn("testNamespace");
        when(redisProperties.getString("redis.entityNamespace")).thenReturn("testEntityNamespace");

        when(objectMapper.readValue(anyString(), EventHandler.class)).thenAnswer(invocation -> {
            String json = invocation.getArgument(0);
            return TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true);
        });
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true);
        dao.addEventHandler(eventHandler);

        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true);
        dao.addEventHandler(existingEventHandler);
        dao.addEventHandler(TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true);
        dao.addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = TestUtils.createEventHandler("testHandlerName", "testHandlerEventUpdated", false);
        dao.updateEventHandler(updatedEventHandler);

        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertNotNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testHandlerName"));
        assertEquals(updatedEventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), "testHandlerName"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler updatedEventHandler = TestUtils.createEventHandler("testHandlerNameNonExistent", "testHandlerEventUpdated", false);
        dao.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true);
        dao.addEventHandler(eventHandler);
        dao.removeEventHandler("testHandlerName");

        assertEquals(0, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        dao.removeEventHandler("testHandlerNameNonExistent");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = Arrays.asList(
                TestUtils.createEventHandler("handler1", "event1", true),
                TestUtils.createEventHandler("handler2", "event2", false)
        );

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(ImmutableMap.of(
                "handler1", toJson(TestUtils.createEventHandler("handler1", "event1", true)),
                "handler2", toJson(TestUtils.createEventHandler("handler2", "event2", false))
        ));

        List<EventHandler> result = dao.getAllEventHandlers();

        assertSame(eventHandlers, result);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = Arrays.asList(
                TestUtils.createEventHandler("handler1", "event1", true),
                TestUtils.createEventHandler("handler2", "event1", false),
                TestUtils.createEventHandler("handler3", "event2", true)
        );

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event1"))).thenReturn(ImmutableSet.of("handler1", "handler2"));

        List<EventHandler> result = dao.getEventHandlersForEvent("event1", false);

        assertSame(Arrays.asList(TestUtils.createEventHandler("handler1", "event1", true), TestUtils.createEventHandler("handler2", "event1", false)), result);
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        List<EventHandler> eventHandlers = Arrays.asList(
                TestUtils.createEventHandler("handler1", "event1", true),
                TestUtils.createEventHandler("handler2", "event1", false),
                TestUtils.createEventHandler("handler3", "event2", true)
        );

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event1"))).thenReturn(ImmutableSet.of("handler1"));

        List<EventHandler> result = dao.getEventHandlersForEvent("event1", true);

        assertSame(Arrays.asList(TestUtils.createEventHandler("handler1", "event1", true)), result);
    }

    @Test
    public void testGetEventHandlerByName() {
        EventHandler eventHandler = TestUtils.createEventHandler("testHandlerName", "testHandlerEvent", true);
        String json = toJson(eventHandler);

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testHandlerName")).thenReturn(json);

        EventHandler result = dao.getEventHandler("testHandlerName");

        assertSame(eventHandler, result);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerByNameWithTransientException() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testHandlerName")).thenThrow(new RuntimeException());

        dao.getEventHandler("testHandlerName");
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerByNameWithNotFoundException() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testHandlerName"))
                .thenReturn(null);

        dao.getEventHandler("testHandlerName");
    }

    private String nsKey(String... keys) {
        return redisUtil.namespaceKey(Arrays.asList(Arrays.asList("testNamespace", "testEntityNamespace"), Arrays.asList(keys)));
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}