package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

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

    @After
    public void teardown() {
        reset(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        String eventHandlerName = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler");
        eventHandler.setName(eventHandlerName);

        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testStringValue");
        when(jedisProxy.hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName), eq("testStringValue")))
                .thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName), eq("testStringValue"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        String existingEventHandlerName = UUID.randomUUID().toString();
        EventHandler existingEventHandler = new EventHandler("testEvent", "testHandler");
        existingEventHandler.setName(existingEventHandlerName);

        EventHandler newEventHandler = new EventHandler("testEvent", "testHandler2");

        when(objectMapper.writeValueAsString(existingEventHandler)).thenReturn("existingStringValue");
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(existingEventHandlerName)))
                .thenReturn("existingStringValue");

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        verify(jedisProxy, never())
                .hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), anyString(), anyString());
        verify(jedisProxy, times(1))
                .hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(existingEventHandlerName));
    }

    @Test
    public void testUpdateEventHandler() {
        String eventHandlerName = UUID.randomUUID().toString();
        EventHandler existingEventHandler = new EventHandler("testEvent", "testHandler");
        existingEventHandler.setName(eventHandlerName);

        EventHandler updatedEventHandler = new EventHandler("testEvent", "testHandlerUpdated");
        updatedEventHandler.setName(eventHandlerName);

        when(objectMapper.writeValueAsString(existingEventHandler)).thenReturn("existingStringValue");
        when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn("updatedStringValue");
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName)))
                .thenReturn("existingStringValue");
        when(jedisProxy.hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName), eq("updatedStringValue")))
                .thenReturn(1);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1))
                .hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName), eq("updatedStringValue"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        String nonExistingEventHandlerName = UUID.randomUUID().toString();
        EventHandler updatedEventHandler = new EventHandler("testEvent", "testHandlerUpdated");
        updatedEventHandler.setName(nonExistingEventHandlerName);

        when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn("updatedStringValue");
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(nonExistingEventHandlerName)))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, never())
                .hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), anyString(), anyString());
        verify(jedisProxy, times(1))
                .hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(nonExistingEventHandlerName));
    }

    @Test
    public void testRemoveEventHandler() {
        String eventHandlerName = UUID.randomUUID().toString();

        when(jedisProxy.hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName)))
                .thenReturn(1);

        redisEventHandlerDAO.removeEventHandler(eventHandlerName);

        verify(jedisProxy, times(1)).hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerName));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        String nonExistingEventHandlerName = UUID.randomUUID().toString();

        when(jedisProxy.hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(nonExistingEventHandlerName)))
                .thenReturn(0);

        redisEventHandlerDAO.removeEventHandler(nonExistingEventHandlerName);

        verify(jedisProxy, never())
                .hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), anyString());
        verify(jedisProxy, times(1))
                .hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(nonExistingEventHandlerName));
    }

    @Test
    public void testGetAllEventHandlers() {
        String eventHandler1Name = UUID.randomUUID().toString();
        String eventHandler1Json = "{\"name\": \"eventHandler1\", \"event\": \"testEvent1\"}";

        String eventHandler2Name = UUID.randomUUID().toString();
        String eventHandler2Json = "{\"name\": \"eventHandler2\", \"event\": \"testEvent2\"}";

        when(jedisProxy.hgetAll(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))))
                .thenReturn(Map.of(eventHandler1Name, eventHandler1Json, eventHandler2Name, eventHandler2Json));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1Name, eventHandlers.get(0).getName());
        assertEquals(eventHandler1Json, JsonUtils.toJson(eventHandlers.get(0)));
        assertEquals(eventHandler2Name, eventHandlers.get(1).getName());
        assertEquals(eventHandler2Json, JsonUtils.toJson(eventHandlers.get(1)));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event1 = "testEvent1";
        String event2 = "testEvent2";

        String eventHandler1Name = UUID.randomUUID().toString();
        String eventHandler1Json = "{\"name\": \"eventHandler1\", \"event\": \"testEvent1\", \"active\": true}";

        String eventHandler2Name = UUID.randomUUID().toString();
        String eventHandler2Json = "{\"name\": \"eventHandler2\", \"event\": \"testEvent1\", \"active\": false}";

        String eventHandler3Name = UUID.randomUUID().toString();
        String eventHandler3Json = "{\"name\": \"eventHandler3\", \"event\": \"testEvent2\"}";

        when(jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT), eq(event1)))).thenReturn(
                Set.of(eventHandler1Name, eventHandler2Name));
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler1Name))).thenReturn(eventHandler1Json);
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler2Name))).thenReturn(eventHandler2Json);
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler3Name))).thenReturn(eventHandler3Json);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event1, false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1Name, eventHandlers.get(0).getName());
        assertEquals(eventHandler1Json, JsonUtils.toJson(eventHandlers.get(0)));
        assertEquals(eventHandler2Name, eventHandlers.get(1).getName());
        assertEquals(eventHandler2Json, JsonUtils.toJson(eventHandlers.get(1)));

        List<EventHandler> eventHandlersActive = redisEventHandlerDAO.getEventHandlersForEvent(event1, true);

        assertEquals(1, eventHandlersActive.size());
        assertEquals(eventHandler1Name, eventHandlersActive.get(0).getName());
        assertEquals(eventHandler1Json, JsonUtils.toJson(eventHandlersActive.get(0)));
    }
}