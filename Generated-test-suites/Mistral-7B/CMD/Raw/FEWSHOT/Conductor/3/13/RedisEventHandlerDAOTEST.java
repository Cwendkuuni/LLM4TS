package com.netflix.conductor.redis.dao;

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
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

@Configuration
public class RedisEventHandlerDAOTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private RedisProperties redisProperties;
    private ConductorProperties conductorProperties;
    private EventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        redisProperties = mock(RedisProperties.class);
        conductorProperties = mock(ConductorProperties.class);

        when(redisProperties.getNamespace()).thenReturn("TEST_NAMESPACE");

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Bean
    @Primary
    public JedisProxy jedisProxyBean() {
        return jedisProxy;
    }

    @Bean
    public ObjectMapper objectMapperBean() {
        return objectMapper;
    }

    @Bean
    public RedisProperties redisPropertiesBean() {
        return redisProperties;
    }

    @Bean
    public ConductorProperties conductorPropertiesBean() {
        return conductorProperties;
    }

    @Test
    public void testAddEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent("testEvent");

        setupMocksForSuccess();

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verifyAddEventHandler();
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        String existingName = UUID.randomUUID().toString();
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(existingName);
        existingEventHandler.setEvent("testEvent");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(existingName);
        newEventHandler.setEvent("testEvent");

        setupMocksForConflictException();

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(name);
        existingEventHandler.setEvent("testEvent");

        String newEvent = "newTestEvent";
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(name);
        newEventHandler.setEvent(newEvent);

        setupMocksForSuccess();

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verifyUpdateEventHandler();
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        String nonExistingName = UUID.randomUUID().toString();
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(nonExistingName);
        newEventHandler.setEvent("testEvent");

        setupMocksForNotFoundException();

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);

        setupMocksForSuccess();

        redisEventHandlerDAO.removeEventHandler(name);

        verifyRemoveEventHandler();
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        String nonExistingName = UUID.randomUUID().toString();

        setupMocksForNotFoundException();

        redisEventHandlerDAO.removeEventHandler(nonExistingName);
    }

    @Test
    public void testGetAllEventHandlers() {
        String name1 = UUID.randomUUID().toString();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent("testEvent1");

        String name2 = UUID.randomUUID().toString();
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent("testEvent2");

        List<EventHandler> expectedHandlers = Arrays.asList(eventHandler1, eventHandler2);

        setupMocksForSuccess();

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(getHandlersAsMap());

        List<EventHandler> actualHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(expectedHandlers, actualHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";

        String name1 = UUID.randomUUID().toString();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        String name2 = UUID.randomUUID().toString();
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        List<EventHandler> expectedHandlers = Arrays.asList(eventHandler1, eventHandler2);

        setupMocksForSuccess();

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(getNamesAsSet());

        List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(expectedHandlers, actualHandlers);
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly() {
        String event = "testEvent";

        String name1 = UUID.randomUUID().toString();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName(name1);
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        String name2 = UUID.randomUUID().toString();
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName(name2);
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        List<EventHandler> expectedHandlers = Collections.singletonList(eventHandler1);

        setupMocksForSuccess();

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(getNamesAsSet());

        List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(expectedHandlers, actualHandlers);
    }

    private void setupMocksForSuccess() {
        when(objectMapper.readValue(anyString(), EqualityMatcher.EventHandler.class)).then(invocation -> invocation.getArguments()[0]);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
        when(jedisProxy.sadd(anyString(), anyString())).thenReturn(1L);
        when(jedisProxy.srem(anyString(), anyString())).thenReturn(1L);
        when(jedisProxy.hgetAll(anyString())).thenReturn(getHandlersAsMap());
        when(jedisProxy.smembers(anyString())).thenReturn(getNamesAsSet());
    }

    private void setupMocksForNotFoundException() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), anyString())).thenThrow(new NotFoundException("Handler not found"));
    }

    private void setupMocksForConflictException() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), anyString())).thenReturn(getHandlerJsonString("existingHandler"));
    }

    private Map<String, String> getHandlersAsMap() {
        LinkedList<EventHandler> handlers = new LinkedList<>();
        handlers.add(new EventHandler());
        handlers.add(new EventHandler());
        return mapHandlersToJsonStrings(handlers);
    }

    private Set<String> getNamesAsSet() {
        LinkedList<String> names = new LinkedList<>();
        names.add(UUID.randomUUID().toString());
        names.add(UUID.randomUUID().toString());
        return (Set<String>) names;
    }

    private String nsKey(String... keys) {
        return String.join(":", Arrays.asList(redisProperties.getNamespace()).toArray(new String[0]))
                + ":" + String.join(":", Arrays.asList(keys).toArray(new String[0]));
    }

    private String getHandlerJsonString(String name) {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        return toJson(eventHandler);
    }

    private Map<String, String> mapHandlersToJsonStrings(List<EventHandler> handlers) {
        Map<String, String> result = new HashMap<>();
        for (EventHandler handler : handlers) {
            result.put(handler.getName(), toJson(handler));
        }
        return result;
    }
}