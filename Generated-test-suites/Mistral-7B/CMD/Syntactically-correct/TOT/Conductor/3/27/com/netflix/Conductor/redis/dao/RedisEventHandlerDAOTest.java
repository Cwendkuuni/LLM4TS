package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.RedisTestBase;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends RedisTestBase {

    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(conductorProperties.getRedisNamespace()).thenReturn("test");
        when(redisProperties.getTimeout()).thenReturn(5000);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        String name = "testEventHandler";
        String event = "testEvent";
        EventHandler eventHandler = createEventHandler(name, event);
        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), eq(name), anyString())).thenReturn(true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(name), anyString());
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        String name = "testEventHandler";
        String event = "testEvent";
        EventHandler eventHandler = createEventHandler(name, event);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.addEventHandler(eventHandler);
        fail("Should throw ConflictException");
    }

    @Test
    public void testGetEventHandler() {
        String name = "testEventHandler";
        String event = "testEvent";
        EventHandler eventHandler = createEventHandler(name, event);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(toJson(eventHandler));
        EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler(name);
        assertEquals(eventHandler, actualEventHandler);
        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        String name = "testEventHandler";
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(null);
        redisEventHandlerDAO.getEventHandler(name);
        fail("Should throw NotFoundException");
    }

    @Test
    public void testUpdateEventHandler() {
        String name = "testEventHandler";
        String newEvent = "newTestEvent";
        EventHandler eventHandler = createEventHandler(name, "testEvent");
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(toJson(eventHandler));
        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), eq(name), anyString())).thenReturn(true);
        EventHandler updatedEventHandler = createEventHandler(name, newEvent);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisProxy, times(2)).hget(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(name), anyString());
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        String name = "testEventHandler";
        EventHandler updatedEventHandler = createEventHandler(name, "newTestEvent");
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        fail("Should throw NotFoundException");
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testEventHandler";
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(toJson(createEventHandler(name, "testEvent")));
        when(jedisProxy.hdel(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(1);
        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "testEventHandler";
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(name);
        fail("Should throw NotFoundException");
    }

    @Test
    public void testGetAllEventHandlers() {
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        EventHandler eventHandler1 = createEventHandler(name1, "event1");
        EventHandler eventHandler2 = createEventHandler(name2, "event2");
        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(createMockMap(name1, toJson(eventHandler1), name2, toJson(eventHandler2)));
        List<EventHandler> actualEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        List<EventHandler> expectedEventHandlers = Arrays.asList(eventHandler1, eventHandler2);
        assertEquals(expectedEventHandlers, actualEventHandlers);
        verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        EventHandler eventHandler1 = createEventHandler(name1, event);
        EventHandler eventHandler2 = createEventHandler(name2, "otherEvent");
        Set<String> names = new HashSet<>(Arrays.asList(name1, name2));
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names);
        List<EventHandler> actualEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        List<EventHandler> expectedEventHandlers = Arrays.asList(eventHandler1, eventHandler2);
        assertEquals(expectedEventHandlers, actualEventHandlers);
        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "testEvent";
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        EventHandler eventHandler1 = createEventHandler(name1, event, true);
        EventHandler eventHandler2 = createEventHandler(name2, event, false);
        Set<String> names = new HashSet<>(Arrays.asList(name1, name2));
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names);
        List<EventHandler> actualEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        List<EventHandler> expectedEventHandlers = Arrays.asList(eventHandler1);
        assertEquals(expectedEventHandlers, actualEventHandlers);
        verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        verifyNoMoreInteractions(jedisProxy);
    }

    private EventHandler createEventHandler(String name, String event) {
        return createEventHandler(name, event, true);
    }

    private EventHandler createEventHandler(String name, String event, boolean active) {
        return new EventHandler() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getEvent() {
                return event;
            }

            @Override
            public boolean isActive() {
                return active;
            }

            @Override
            public String toString() {
                return String.format("{name: '%s', event: '%s', isActive: %s}", name, event, active);
            }
        };
    }

    private Map<String, String, String> createMockMap(String... keysValues) {
        Map<String, String, String> map = ImmutableMap.of();
        for (String keyValue : keysValues) {
            String[] keyValueParts = StringUtils.split(keyValue, ":");
            map = map.put(keyValueParts[0], keyValueParts[1]);
        }
        return map;
    }
}