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
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("testName"), anyString());
        verify(jedisProxy, times(2)).sadd(anyString(), eq(eventHandler.getName()));
        verify(jedisProxy).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler("existingName", "testEvent");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        verify(jedisProxy).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("existingName"), anyString());

        EventHandler newEventHandler = new EventHandler("existingName", "testEvent");
        redisEventHandlerDAO.addEventHandler(newEventHandler);
        verify(jedisProxy, never()).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("existingName"), anyString());
        verify(jedisProxy).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("newEventHandlerName"), anyString());
        verify(jedisProxy).recordRedisDaoRequests(eq("addEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("existingName", "testEvent");
        String json = toJson(existingEventHandler);
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("existingName"))).thenReturn(json);

        EventHandler updatedEventHandler = new EventHandler("existingName", "updatedEvent");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisProxy).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("existingName"), eq(toJson(updatedEventHandler)));
        verify(jedisProxy, times(2)).sadd(anyString(), eq(updatedEventHandler.getName()));
        verify(jedisProxy, never()).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("existingName"), anyString());
        verify(jedisProxy).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler updatedEventHandler = new EventHandler("nonExistentName", "updatedEvent");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisProxy, never()).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("nonExistentName"), anyString());
        verify(jedisProxy).hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("nonExistentName"));
        verify(jedisProxy).recordRedisDaoRequests(eq("updateEventHandler"));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        redisEventHandlerDAO.removeEventHandler("testName");
        verify(jedisProxy).hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("testName"));
        verify(jedisProxy).recordRedisDaoRequests(eq("removeEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        redisEventHandlerDAO.removeEventHandler("nonExistentName");
        verify(jedisProxy).hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("nonExistentName"));
        verify(jedisProxy).hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("nonExistentName"));
        verify(jedisProxy).recordRedisDaoRequests(eq("removeEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("eventHandler1", "event1");
        EventHandler eventHandler2 = new EventHandler("eventHandler2", "event2");
        when(jedisProxy.hgetAll(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)))).thenReturn(mockMap(
                "eventHandler1", toJson(eventHandler1),
                "eventHandler2", toJson(eventHandler2)));

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));
        verify(jedisProxy).recordRedisDaoRequests(eq("getAllEventHandlers"));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("eventHandler1", "event1");
        EventHandler eventHandler2 = new EventHandler("eventHandler2", "event1");
        EventHandler eventHandler3 = new EventHandler("eventHandler3", "event2");

        when(jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "event1"))))
                .thenReturn(Arrays.asList("eventHandler1", "eventHandler2"));
        when(jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "event2"))))
                .thenReturn(Arrays.asList("eventHandler3"));

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));

        List<EventHandler> activeHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        assertEquals(1, activeHandlers.size());
        assertEquals(eventHandler1, activeHandlers.get(0));

        List<EventHandler> inactiveHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event2", false);
        assertEquals(1, inactiveHandlers.size());
        assertEquals(eventHandler3, inactiveHandlers.get(0));

        verify(jedisProxy).recordRedisDaoEventRequests("getEventHandler", "event1");
        verify(jedisProxy).recordRedisDaoEventRequests("getEventHandler", "event2");
    }

    @Test
    public void testGetEventHandlerWithNonExistentName() {
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("nonExistentName")))
                .thenThrow(new NotFoundException("EventHandler with name nonExistentName not found!"));

        try {
            redisEventHandlerDAO.getEventHandler("nonExistentName");
            fail();
        } catch (NotFoundException e) {
            // Expected
        }

        verify(jedisProxy).hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq("nonExistentName"));
    }

    private String nsKey(String key) {
        return "namespace:" + redisProperties.getNamespace() + ":" + key;
    }

    private Map<String, String> mockMap(String... keyValuePairs) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String pair : keyValuePairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}