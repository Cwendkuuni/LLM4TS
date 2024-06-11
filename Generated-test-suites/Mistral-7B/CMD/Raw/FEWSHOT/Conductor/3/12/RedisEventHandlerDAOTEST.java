package com.netflix.conductor.redis.dao;

import static com.netflix.conductor.common.metadata.events.EventHandler.EventStatus.ACTIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.common.metadata.events.EventHandler.EventStatus;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisProperties.Namespace;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    private RedisProperties redisProperties;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private EventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisProperties = new RedisProperties();
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("foo", "bar", "baz", "qux", EventStatus.ACTIVE);
        String json = JsonUtils.toJson(eventHandler);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        when(jedisProxy.hset(nameCaptor.capture(), valueCaptor.capture(), eq(json)))
                .thenReturn(true);
        when(objectMapper.writeValueAsString(eq(eventHandler))).thenReturn(json);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals("foo", nameCaptor.getValue());
        assertEquals(json, valueCaptor.getValue());
        assertEquals("EventHandler added", redisEventHandlerDAO.getLastLogMessage());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        String name = "foo";
        EventHandler eventHandler = new EventHandler(name, "bar", "baz", "qux", EventStatus.ACTIVE);
        String json = JsonUtils.toJson(eventHandler);

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(json);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // ConflictException should be thrown in the addEventHandler method
    }

    @Test
    public void testGetEventHandler() {
        String name = "foo";
        EventHandler eventHandler = new EventHandler(name, "bar", "baz", "qux", EventStatus.ACTIVE);
        String json = JsonUtils.toJson(eventHandler);

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(json);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(name);

        assertSame(eventHandler, retrievedEventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientFailure() {
        String name = "foo";

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler(name);

        // TransientException should be thrown
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        String name = "foo";

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(null);

        redisEventHandlerDAO.getEventHandler(name);

        // NotFoundException should be thrown
    }

    @Test
    public void testUpdateEventHandler() {
        String name = "foo";
        String newEvent = "newEvent";
        EventHandler eventHandler = new EventHandler(name, "bar", "baz", "qux", EventStatus.ACTIVE);
        String json = JsonUtils.toJson(eventHandler);

        EventHandler updatedEventHandler = new EventHandler(name, newEvent, "baz", "qux", EventStatus.ACTIVE);

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(json);
        when(objectMapper.readValue(eq(json), eq(EventHandler.class)))
                .thenReturn(eventHandler);
        when(jedisProxy.hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name), eq(JsonUtils.toJson(updatedEventHandler))))
                .thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertEquals("EventHandler updated", redisEventHandlerDAO.getLastLogMessage());
    }

    @Test(expected = ConflictException.class)
    public void testUpdateEventHandlerConflict() {
        String name = "foo";
        EventHandler eventHandler = new EventHandler(name, "bar", "baz", "qux", EventStatus.ACTIVE);
        String json = JsonUtils.toJson(eventHandler);
        EventHandler updatedEventHandler = new EventHandler(name, "newEvent", "baz", "qux", EventStatus.ACTIVE);

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(json);
        when(objectMapper.readValue(eq(json), eq(EventHandler.class)))
                .thenReturn(eventHandler);
        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(JsonUtils.toJson(updatedEventHandler));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // ConflictException should be thrown in the updateEventHandler method
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "foo";
        EventHandler eventHandler = new EventHandler(name, "bar", "baz", "qux", EventStatus.ACTIVE);
        String json = JsonUtils.toJson(eventHandler);

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(json);

        redisEventHandlerDAO.removeEventHandler(name);

        assertEquals("EventHandler removed", redisEventHandlerDAO.getLastLogMessage());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "foo";

        when(jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(name)))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);

        // NotFoundException should be thrown
    }

    @Test
    public void testGetAllEventHandlers() {
        Namespace ns = Namespace.CONDUCTOR;
        Map<String, String> all = ImmutableSet.of("handler1", "handler2").asMap();

        when(jedisProxy.hgetAll(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)))).thenReturn(all);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        List<EventHandler> expectedHandlers = new LinkedList<>();
        expectedHandlers.add(new EventHandler("handler1", "event1", "handler1Class", "handler1Config", ACTIVE));
        expectedHandlers.add(new EventHandler("handler2", "event2", "handler2Class", "handler2Config", ACTIVE));

        assertEquals(expectedHandlers, handlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "event";
        List<EventHandler> eventHandlers = Arrays.asList(
                new EventHandler("handler1", event, "handler1Class", "handler1Config", ACTIVE),
                new EventHandler("handler2", event, "handler2Class", "handler2Config", ACTIVE)
        );
        Set<String> names = Collections.newHashSet(eventHandlers.stream().map(EventHandler::getName).collect(Collectors.toList()));

        when(jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(eventHandlers, handlers);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "event";
        List<EventHandler> eventHandlers = Arrays.asList(
                new EventHandler("handler1", event, "handler1Class", "handler1Config", ACTIVE),
                new EventHandler("handler2", event, "handler2Class", "handler2Config", EventStatus.INACTIVE)
        );
        Set<String> names = Collections.newHashSet(eventHandlers.stream().filter(e -> e.isActive()).map(EventHandler::getName).collect(Collectors.toList()));

        when(jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(eventHandlers.stream().filter(EventHandler::isActive).collect(Collectors.toList()), handlers);
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        String event = "event";

        when(jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(Collections.emptySet());

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertTrue(handlers.isEmpty());
    }
}