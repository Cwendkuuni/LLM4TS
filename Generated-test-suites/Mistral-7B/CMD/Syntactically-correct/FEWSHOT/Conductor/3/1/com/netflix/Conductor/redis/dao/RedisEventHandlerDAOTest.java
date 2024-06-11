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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

public class RedisEventHandlerDAOTest {
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler("test1", "testEvent", "testVersion");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(ArgumentMatchers.eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)),
                ArgumentMatchers.eq(eventHandler.getName()), ArgumentMatchers.anyString());
        verify(objectMapper, times(1)).writeValueAsString(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithDuplicateName() throws Exception {
        EventHandler eventHandler1 = new EventHandler("test1", "testEvent", "testVersion1");
        EventHandler eventHandler2 = new EventHandler("test1", "testEvent", "testVersion2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        redisEventHandlerDAO.addEventHandler(eventHandler2);

        verify(jedisProxy, times(2)).hset(ArgumentMatchers.eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)),
                ArgumentMatchers.eq(eventHandler1.getName()), ArgumentMatchers.anyString());
        verify(objectMapper, times(2)).writeValueAsString(eventHandler1);
        verify(jedisProxy, times(1)).hset(ArgumentMatchers.eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)),
                ArgumentMatchers.eq(eventHandler2.getName()), ArgumentMatchers.anyString());
        verify(objectMapper, times(1)).writeValueAsString(eventHandler2);
        verify(jedisProxy, times(2)).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), ArgumentMatchers.eq(eventHandler1.getName()));
        verify(jedisProxy, times(1)).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), ArgumentMatchers.eq(eventHandler2.getName()));
    }

    @Test
    public void testGetEventHandler() throws Exception {
        String eventHandlerJson = "{\"name\": \"test1\", \"event\": \"testEvent\", \"version\": \"testVersion\"}";
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1")).thenReturn(eventHandlerJson);
        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class)))
                .then(invocation -> invocation.getArguments()[0].equals(eventHandlerJson)
                        ? new EventHandler("test1", "testEvent", "testVersion") : null);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("test1");

        assertEquals("test1", eventHandler.getName());
        assertEquals("testEvent", eventHandler.getEvent());
        assertEquals("testVersion", eventHandler.getVersion());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNotFound() throws Exception {
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1")).thenReturn(null);

        redisEventHandlerDAO.getEventHandler("test1");
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerWithException() throws Exception {
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1"))
                .thenThrow(new RuntimeException("Test Exception"));

        redisEventHandlerDAO.getEventHandler("test1");
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler("test1", "testEvent", "testVersion1");
        String eventHandlerJson = "{\"name\": \"test1\", \"event\": \"testEvent\", \"version\": \"testVersion2\"}";

        redisEventHandlerDAO.addEventHandler(eventHandler);

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1")).thenReturn(eventHandlerJson);
        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class)))
                .then(invocation -> invocation.getArguments()[0].equals(eventHandlerJson)
                        ? new EventHandler("test1", "testEvent", "testVersion2") : null);

        EventHandler updatedEventHandler = new EventHandler("test1", "testEvent", "testVersion3");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1");
        verify(objectMapper, times(1)).readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class));
        verify(jedisProxy, times(1)).hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1",
                ArgumentMatchers.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFound() throws Exception {
        EventHandler eventHandler = new EventHandler("test1", "testEvent", "testVersion1");
        String eventHandlerJson = "{\"name\": \"test2\", \"event\": \"testEvent\", \"version\": \"testVersion\"}";

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1")).thenReturn(null);
        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class)))
                .then(invocation -> invocation.getArguments()[0].equals(eventHandlerJson)
                        ? new EventHandler("test2", "testEvent", "testVersion") : null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler("test1", "testEvent", "testVersion");

        redisEventHandlerDAO.removeEventHandler("test1");

        verify(jedisProxy, times(1)).hdel(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "test1");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFound() throws Exception {
        redisEventHandlerDAO.removeEventHandler("test1");
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = new EventHandler("test1", "testEvent1", "testVersion1");
        EventHandler eventHandler2 = new EventHandler("test2", "testEvent2", "testVersion2");
        String eventHandlerJson1 = "{\"name\": \"test1\", \"event\": \"testEvent1\", \"version\": \"testVersion1\"}";
        String eventHandlerJson2 = "{\"name\": \"test2\", \"event\": \"testEvent2\", \"version\": \"testVersion2\"}";

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(Map.ofEntries(
                Map.entry("test1", eventHandlerJson1), Map.entry("test2", eventHandlerJson2)));
        when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class)))
                .then(invocation -> invocation.getArguments()[0].equals(eventHandlerJson1)
                        ? new EventHandler("test1", "testEvent1", "testVersion1")
                        : invocation.getArguments()[0].equals(eventHandlerJson2)
                                ? new EventHandler("test2", "testEvent2", "testVersion2") : null);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertEquals("test1", allEventHandlers.get(0).getName());
        assertEquals("testEvent1", allEventHandlers.get(0).getEvent());
        assertEquals("testVersion1", allEventHandlers.get(0).getVersion());
        assertEquals("test2", allEventHandlers.get(1).getName());
        assertEquals("testEvent2", allEventHandlers.get(1).getEvent());
        assertEquals("testVersion2", allEventHandlers.get(1).getVersion());
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = new EventHandler("test1", "testEvent1", "testVersion1");
        EventHandler eventHandler2 = new EventHandler("test2", "testEvent1", "testVersion2");
        EventHandler eventHandler3 = new EventHandler("test3", "testEvent1", "testVersion3");

        String key = nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent1");

        Set<String> names = Arrays.asList("test1", "test2", "test3").stream().map(String::valueOf).collect(Collectors.toSet());
        when(jedisProxy.smembers(key)).thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(3, eventHandlers.size());
        assertEquals("test1", eventHandlers.get(0).getName());
        assertEquals("testEvent1", eventHandlers.get(0).getEvent());
        assertEquals("testVersion1", eventHandlers.get(0).getVersion());
        assertEquals("test2", eventHandlers.get(1).getName());
        assertEquals("testEvent1", eventHandlers.get(1).getEvent());
        assertEquals("testVersion2", eventHandlers.get(1).getVersion());
        assertEquals("test3", eventHandlers.get(2).getName());
        assertEquals("testEvent1", eventHandlers.get(2).getEvent());
        assertEquals("testVersion3", eventHandlers.get(2).getVersion());

        List<EventHandler> eventHandlersActiveOnly = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);

        assertEquals(1, eventHandlersActiveOnly.size());
        assertEquals("test1", eventHandlersActiveOnly.get(0).getName());
        assertEquals("testEvent1", eventHandlersActiveOnly.get(0).getEvent());
        assertEquals("testVersion1", eventHandlersActiveOnly.get(0).getVersion());
    }

    @Test
    public void testGetEventHandlersForEventWithEmptySet() throws Exception {
        String key = nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent1");

        Set<String> names = Collections.emptySet();

        when(jedisProxy.smembers(key)).thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(0, eventHandlers.size());
    }
}