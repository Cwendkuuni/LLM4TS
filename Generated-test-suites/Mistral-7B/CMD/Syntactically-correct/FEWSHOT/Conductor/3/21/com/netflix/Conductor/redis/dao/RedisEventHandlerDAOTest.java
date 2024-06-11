package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandlerName", "testHandlerEvent", ImmutableList.of(), true);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerThrowsConflictException() {
        EventHandler eventHandler = new EventHandler("testHandlerName", "testHandlerEvent", ImmutableList.of(), true);
        EventHandler existingEventHandler = new EventHandler("testHandlerName", "testHandlerEvent", ImmutableList.of(), true);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()))).thenReturn(JsonUtils.toJson(existingEventHandler));
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()), anyString());
        verify(jedisProxy, never()).hset(eq(nsKey(EVENT_HANDLERS)), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        String oldJson = JsonUtils.toJson(new EventHandler("oldName", "oldEvent", ImmutableList.of(), true));
        String newJson = JsonUtils.toJson(new EventHandler("newName", "newEvent", ImmutableList.of(), true));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(oldJson);
        when(objectMapper.readValue(anyString(), EventHandler.class)).thenReturn(new EventHandler("newName", "newEvent", ImmutableList.of(), true));
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.updateEventHandler(new EventHandler("newName", "newEvent", ImmutableList.of(), true));
        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("newName"), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerThrowsNotFoundException() {
        String oldJson = JsonUtils.toJson(new EventHandler("oldName", "oldEvent", ImmutableList.of(), true));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(oldJson);
        when(objectMapper.readValue(anyString(), EventHandler.class)).thenThrow(new IllegalStateException());
        redisEventHandlerDAO.updateEventHandler(new EventHandler("newName", "newEvent", ImmutableList.of(), true));
        verify(jedisProxy, never()).hset(eq(nsKey(EVENT_HANDLERS)), eq("newName"), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandlerName", "testHandlerEvent", ImmutableList.of(), true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(JsonUtils.toJson(eventHandler));
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.removeEventHandler("testHandlerName");
        verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandlerName"));
        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq("testHandlerName"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerThrowsNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("testHandlerName");
        verify(jedisProxy, never()).hdel(eq(nsKey(EVENT_HANDLERS)), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("eventHandler1", "event1", ImmutableList.of(), true);
        EventHandler eventHandler2 = new EventHandler("eventHandler2", "event2", ImmutableList.of(), true);
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("eventHandler1", JsonUtils.toJson(eventHandler1), "eventHandler2", JsonUtils.toJson(eventHandler2)));
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("eventHandler1", "event1", ImmutableList.of(), true);
        EventHandler eventHandler2 = new EventHandler("eventHandler2", "event1", ImmutableList.of(), true);
        EventHandler eventHandler3 = new EventHandler("eventHandler3", "event2", ImmutableList.of(), true);
        when(jedisProxy.smembers(anyString())).thenReturn(Arrays.asList("eventHandler1", "eventHandler2", "eventHandler3"));
        when(jedisProxy.hget(anyString(), eq("eventHandler1"))).thenReturn(JsonUtils.toJson(eventHandler1));
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        EventHandler eventHandler1 = new EventHandler("eventHandler1", "event1", ImmutableList.of(), true);
        EventHandler eventHandler2 = new EventHandler("eventHandler2", "event1", ImmutableList.of(), false);
        EventHandler eventHandler3 = new EventHandler("eventHandler3", "event2", ImmutableList.of(), true);
        when(jedisProxy.smembers(anyString())).thenReturn(Arrays.asList("eventHandler1", "eventHandler2", "eventHandler3"));
        when(jedisProxy.hget(anyString(), eq("eventHandler1"))).thenReturn(JsonUtils.toJson(eventHandler1));
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventThrowsNotFoundException() {
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.emptyList());
        redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        verify(jedisProxy, never()).hget(anyString(), anyString());
    }
}