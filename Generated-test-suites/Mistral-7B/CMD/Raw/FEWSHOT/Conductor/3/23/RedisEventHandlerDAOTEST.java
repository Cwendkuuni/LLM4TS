package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
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
import com.netflix.conductor.redis.jedis.JedisProxyFactory;
import com.netflix.conductor.redis.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String EVENT_1 = "event1";
    private static final String EVENT_2 = "event2";

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(NAME_1))).thenReturn("{}");
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(NAME_2))).thenReturn("{}");
        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Map.of(NAME_1, "{}", NAME_2, "{}"));
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_1)))).thenReturn(Set.of(NAME_1));
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_2)))).thenReturn(Set.of(NAME_2));

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_1);
        eventHandler.setEvent(EVENT_1);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verifyAddEventHandlerToRedis(NAME_1, EVENT_1, true);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_1);
        eventHandler.setEvent(EVENT_1);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // First call should add the event handler

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(NAME_1);
        eventHandler2.setEvent(EVENT_1);
        eventHandler2.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler2);

        // Second call should throw a ConflictException
        verifyAddEventHandlerToRedis(NAME_1, EVENT_1, true);
        verifyAddEventHandlerToRedis(NAME_1, EVENT_1, true);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_1);
        eventHandler.setEvent(EVENT_1);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(NAME_1);
        updatedEventHandler.setEvent(EVENT_2);
        updatedEventHandler.setActive(false);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verifyUpdateEventHandlerToRedis(NAME_1, EVENT_2, false);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_2);
        eventHandler.setEvent(EVENT_2);
        eventHandler.setActive(true);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        // First call should add the event handler

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(NAME_2);
        updatedEventHandler.setEvent(EVENT_1);
        updatedEventHandler.setActive(true);

        try {
            redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        } catch (NotFoundException e) {
            verifyAddEventHandlerToRedis(NAME_2, EVENT_1, true);
            throw e;
        }

        // Second call should throw a NotFoundException
        verifyAddEventHandlerToRedis(NAME_2, EVENT_1, true);
        verifyUpdateEventHandlerToRedis(NAME_2, EVENT_1, true);

        EventHandler nonexistentEventHandler = new EventHandler();
        nonexistentEventHandler.setName(UUID.randomUUID().toString());

        throw new NotFoundException("EventHandler with name %s not found!".formatted(nonexistentEventHandler.getName()));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_1);
        eventHandler.setEvent(EVENT_1);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(NAME_1);

        verifyRemoveEventHandlerFromRedis(NAME_1);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_2);
        eventHandler.setEvent(EVENT_2);
        eventHandler.setActive(false);

        redisEventHandlerDAO.removeEventHandler(NAME_2);

        // First call should remove the event handler

        EventHandler nonexistentEventHandler = new EventHandler();
        nonexistentEventHandler.setName(UUID.randomUUID().toString());

        throw new NotFoundException("EventHandler with name %s not found!".formatted(nonexistentEventHandler.getName()));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(NAME_1);
        eventHandler1.setEvent(EVENT_1);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(NAME_2);
        eventHandler2.setEvent(EVENT_2);
        eventHandler2.setActive(false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertSame(eventHandler1, eventHandlers.get(0));
        assertSame(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(NAME_1);
        eventHandler1.setEvent(EVENT_1);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(NAME_2);
        eventHandler2.setEvent(EVENT_1);
        eventHandler2.setActive(true);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName(NAME_2);
        eventHandler3.setEvent(EVENT_2);
        eventHandler3.setActive(false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_1, false);

        assertEquals(2, eventHandlers.size());
        assertSame(eventHandler1, eventHandlers.get(0));
        assertSame(eventHandler2, eventHandlers.get(1));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_1, true);

        assertEquals(1, activeEventHandlers.size());
        assertSame(eventHandler1, activeEventHandlers.get(0));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_1);
        eventHandler.setEvent(EVENT_1);
        eventHandler.setActive(true);

        String json = toJson(eventHandler);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(NAME_1))).thenReturn(json);

        EventHandler resultedEventHandler = redisEventHandlerDAO.getEventHandler(NAME_1);

        assertEquals(eventHandler, resultedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNotFound() {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(NAME_1))).thenReturn(null);

        throw new NotFoundException("EventHandler with name %s not found!".formatted(NAME_1));
    }

    @Test
    public void testRecordRedisDaoRequests() {
        String operation = "addEventHandler";

        redisEventHandlerDAO.recordRedisDaoRequests(operation);

        assertTrue( JedisProxy.REDIS_DAO_REQUESTS.containsKey(operation) );
        assertEquals(1, JedisProxy.REDIS_DAO_REQUESTS.get(operation).size());
    }

    private void verifyAddEventHandlerToRedis(String name, String event, boolean active) {
        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), eq(name), eq(toJson(new EventHandler().setName(name).setEvent(event).setActive(active))))).thenReturn(1);
    }

    private void verifyUpdateEventHandlerToRedis(String name, String event, boolean active) {
        String json = toJson(new EventHandler().setName(name).setEvent(event).setActive(active));

        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), eq(name), json)).thenReturn(1);
    }

    private void verifyRemoveEventHandlerFromRedis(String name) {
        when(jedisProxy.hdel(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(1);
    }

    private String nsKey(String key, Object... params) {
        return String.format(REDIS_NS_KEY, conductorProperties.getNamespace(), key, JsonUtil.toJson(params));
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}