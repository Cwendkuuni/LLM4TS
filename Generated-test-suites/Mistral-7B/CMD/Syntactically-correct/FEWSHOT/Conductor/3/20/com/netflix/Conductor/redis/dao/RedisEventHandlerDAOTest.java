package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

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

    private String nsKey(String... keys) {
        return String.join(":", Arrays.asList(keys).toArray(new String[0]));
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert event handler to JSON", e);
        }
    }

    private EventHandler readValue(String json, Class<? extends EventHandler> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to event handler", e);
        }
    }

    private EventHandler createEventHandler() {
        EventHandler eventHandler = mock(EventHandler.class);
        Preconditions.checkState(eventHandler.getName() != null);
        Preconditions.checkState(eventHandler.getEvent() != null);
        Preconditions.checkState(eventHandler.isActive() != false);
        return eventHandler;
    }

    private void setupMocks() {
        when(jedisProxy.hget(anyString(), anyString())).thenAnswer(invocation -> invocation.getArgument(1));
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);
        when(jedisProxy.sadd(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.smembers(anyString())).thenReturn( Arrays.asList("handler1", "handler2").iterator());
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("handler1", "{}"));
    }

    @Before
    public void setup() {
        setupMocks();
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), toJson(eventHandler));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() throws Exception {
        String eventHandlerName = UUID.randomUUID().toString();
        EventHandler existingEventHandler = createEventHandler();
        EventHandler newEventHandler = createEventHandler();
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandlerName))).thenReturn(toJson(existingEventHandler));
        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler existingEventHandler = createEventHandler();
        String name = UUID.randomUUID().toString();
        EventHandler updatedEventHandler = createEventHandler();
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), name, toJson(updatedEventHandler));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws Exception {
        String name = UUID.randomUUID().toString();
        EventHandler updatedEventHandler = createEventHandler();
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), name);
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws Exception {
        String name = UUID.randomUUID().toString();
        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        Map<String, String> all = Map.of("handler1", "{}", "handler2", "{}");
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, handlers.size());
        verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerWithError() throws Exception {
        String name = UUID.randomUUID().toString();
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenThrow(new RuntimeException("Error"));
        try {
            redisEventHandlerDAO.getEventHandler(name);
        } catch (NotFoundException e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
    }

    @Test
    public void testGetEventHandler() throws Exception {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = createEventHandler();
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn(toJson(eventHandler));
        EventHandler gottenEventHandler = redisEventHandlerDAO.getEventHandler(name);
        assertEquals(eventHandler, gottenEventHandler);
        verify(jedisProxy, times(1)).hget(nsKey(EVENT_HANDLERS), name);
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        String event = "testEvent";
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event)))
                .thenReturn(Arrays.asList("handler1", "handler2").iterator());
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(2, handlers.size());

        List<EventHandler> handlersActiveOnly = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(1, handlersActiveOnly.size());

        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        verifyNoMoreInteractions(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEventEmpty() throws Exception {
        String event = "testEvent";
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Collections.emptyIterator());
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(0, handlers.size());

        List<EventHandler> handlersActiveOnly = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(0, handlersActiveOnly.size());

        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        verifyNoMoreInteractions(jedisProxy);
    }
}