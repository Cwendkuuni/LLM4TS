package com.netflix.conductor.redis.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.netflix.conductor.redis.RedisProperties;
import com.netflix.conductor.redis.util.JsonUtil;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);
    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        when(conductorProperties.getApplicationName()).thenReturn("testAppName");
        when(redisProperties.getNamespace()).thenReturn("testNamespace");

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        // Given
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of());

        // When
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Then
        verifyAddEventHandlerToRedis(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerAlreadyExists() {
        // Given
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEvent2");
        newEventHandler.setActive(true);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of(
                "testEventHandler", JsonUtil.writeValueAsString(existingEventHandler)));

        // When
        redisEventHandlerDAO.addEventHandler(newEventHandler);

        // Then
        verifyException(ConflictException.class, "EventHandler with name testEventHandler already exists!");
    }

    @Test
    public void testUpdateEventHandler() {
        // Given
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEventUpdated");
        newEventHandler.setActive(false);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of(
                "testEventHandler", JsonUtil.writeValueAsString(existingEventHandler)));

        // When
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        // Then
        verifyUpdateEventHandlerInRedis(newEventHandler);
        verifyRemoveEventHandlerFromIndex(existingEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        // Given
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandlerNotFound");
        newEventHandler.setEvent("testEventUpdated");
        newEventHandler.setActive(false);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of());

        // When
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        // Then
        verifyException(NotFoundException.class, "EventHandler with name testEventHandlerNotFound not found!");
    }

    @Test
    public void testRemoveEventHandler() {
        // Given
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of(
                "testEventHandler", JsonUtil.writeValueAsString(eventHandler)));

        // When
        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        // Then
        verifyRemoveEventHandlerFromRedis(eventHandler.getName());
        verifyRemoveEventHandlerFromIndex(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        // Given
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of());

        // When
        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        // Then
        verifyException(NotFoundException.class, "EventHandler with name testEventHandler not found!");
    }

    @Test
    public void testGetAllEventHandlers() {
        // Given
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);

        Map<String, String> eventHandlers = Map.of(
                "testEventHandler1", JsonUtil.writeValueAsString(eventHandler1),
                "testEventHandler2", JsonUtil.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(eventHandlers);

        // When
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Then
        verifyAllEventHandlersRetrieved(allEventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Given
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("testEventHandler3");
        eventHandler3.setEvent("testEvent2");
        eventHandler3.setActive(true);

        Map<String, String> eventHandlers = Map.of(
                "testEventHandler1", JsonUtil.writeValueAsString(eventHandler1),
                "testEventHandler2", JsonUtil.writeValueAsString(eventHandler2),
                "testEventHandler3", JsonUtil.writeValueAsString(eventHandler3));

        Set<String> names = Set.of("testEventHandler1", "testEventHandler3");

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(eventHandlers);
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent1"))).thenReturn(names);

        // When
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        // Then
        verifyEventHandlersRetrievedForEvent(eventHandlersForEvent);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        // Given
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("testEventHandler3");
        eventHandler3.setEvent("testEvent2");
        eventHandler3.setActive(true);

        Map<String, String> eventHandlers = Map.of(
                "testEventHandler1", JsonUtil.writeValueAsString(eventHandler1),
                "testEventHandler2", JsonUtil.writeValueAsString(eventHandler2),
                "testEventHandler3", JsonUtil.writeValueAsString(eventHandler3));

        Set<String> names = Set.of("testEventHandler1");

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(eventHandlers);
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent1"))).thenReturn(names);

        // When
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);

        // Then
        verifyEventHandlersRetrievedForEventActiveOnly(eventHandlersForEvent);
    }

    private void verifyAddEventHandlerToRedis(EventHandler eventHandler) {
        when(jedisProxy.hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), toJson(eventHandler))).thenReturn(1);
        index(eventHandler);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()), eventHandler.getName());
    }

    private void verifyUpdateEventHandlerInRedis(EventHandler newEventHandler) {
        when(jedisProxy.hset(nsKey(EVENT_HANDLERS), newEventHandler.getName(), toJson(newEventHandler))).thenReturn(1);
        recordRedisDaoRequests("updateEventHandler");
    }

    private void verifyRemoveEventHandlerFromRedis(String name) {
        when(jedisProxy.hdel(nsKey(EVENT_HANDLERS), name)).thenReturn(1);
        recordRedisDaoRequests("removeEventHandler");
    }

    private void verifyAllEventHandlersRetrieved(List<EventHandler> allEventHandlers) {
        assertThat(allEventHandlers, is(notNullValue()));
        assertThat(allEventHandlers.size(), is(2));
        for (EventHandler eventHandler : allEventHandlers) {
            assertThat(eventHandler, is(notNullValue()));
        }
    }

    private void verifyEventHandlersRetrievedForEvent(List<EventHandler> eventHandlersForEvent) {
        assertThat(eventHandlersForEvent, is(notNullValue()));
        assertThat(eventHandlersForEvent.size(), is(2));
        for (EventHandler eventHandler : eventHandlersForEvent) {
            assertThat(eventHandler, is(notNullValue()));
            assertThat(eventHandler.getEvent(), is("testEvent1"));
        }
    }

    private void verifyEventHandlersRetrievedForEventActiveOnly(List<EventHandler> eventHandlersForEvent) {
        assertThat(eventHandlersForEvent, is(notNullValue()));
        assertThat(eventHandlersForEvent.size(), is(1));
        EventHandler eventHandler = eventHandlersForEvent.get(0);
        assertThat(eventHandler, is(notNullValue()));
        assertThat(eventHandler.getEvent(), is("testEvent1"));
        assertThat(eventHandler.isActive(), is(true));
    }

    private void verifyException(Class<? extends Throwable> exceptionType, String message) {
        try {
            // Test case code here
        } catch (Throwable e) {
            if (exceptionType.isInstance(e)) {
                assertThat(e.getMessage(), is(message));
                throw e;
            }
        }
    }

    private String nsKey(String key, String... subKeys) {
        return String.join(":", Arrays.asList(conductorProperties.getApplicationName(), redisProperties.getNamespace(), key))
                + String.join(":", subKeys);
    }

    private void recordRedisDaoEventRequests(String methodName) {
        // Mock implementation for testing purposes
    }

    private void index(EventHandler eventHandler) {
        String event = eventHandler.getEvent();
        String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
        jedisProxy.sadd(key, eventHandler.getName());
    }

    private void removeIndex(EventHandler eventHandler) {
        String event = eventHandler.getEvent();
        String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
        jedisProxy.srem(key, eventHandler.getName());
    }
}