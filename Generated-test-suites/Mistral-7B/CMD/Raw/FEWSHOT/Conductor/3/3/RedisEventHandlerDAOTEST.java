package com.netflix.conductor.redis.dao;

import static com.netflix.conductor.redis.dao.RedisEventHandlerDAO.EVENT_HANDLERS;
import static com.netflix.conductor.redis.dao.RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseTest;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

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
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        String name = UUID.randomUUID().toString();
        String event = "event";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(event);
        eventHandler.setActive(true);

        Preconditions.checkNotNull(name, "Missing Name");

        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("eventHandlerJson");

        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq(name), Mockito.eq("eventHandlerJson")))
                .thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertThat(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).get(name), is("eventHandlerJson"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        String name = UUID.randomUUID().toString();
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(name);
        existingEventHandler.setEvent("existingEvent");
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn("existingEventHandlerJson");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(name);
        newEventHandler.setEvent("newEvent");

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        String name = UUID.randomUUID().toString();
        String oldEvent = "oldEvent";
        String newEvent = "newEvent";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(oldEvent);
        eventHandler.setActive(true);

        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("eventHandlerJson");

        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq(name), Mockito.eq("eventHandlerJson")))
                .thenReturn(1);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertThat(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).get(name), is("eventHandlerJson"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent("event");
        eventHandler.setActive(true);

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testUpdateEventHandlerWithExistingNameButDifferentEvent() {
        String name = UUID.randomUUID().toString();
        String oldEvent = "oldEvent";
        String newEvent = "newEvent";
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(name);
        existingEventHandler.setEvent(oldEvent);
        existingEventHandler.setActive(true);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn("existingEventHandlerJson");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(name);
        newEventHandler.setEvent(newEvent);
        newEventHandler.setActive(true);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = UUID.randomUUID().toString();

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn("handlerJson");

        redisEventHandlerDAO.removeEventHandler(name);

        assertThat(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS)).get(name), is(nullValue()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        String name = UUID.randomUUID().toString();

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("name1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("name2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(true);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(
                Map.of("name1", "handler1Json", "name2", "handler2Json"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertThat(eventHandlers.size(), is(2));
        assertThat(eventHandlers.get(0), is(eventHandler1));
        assertThat(eventHandlers.get(1), is(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "event";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("name1");
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("name2");
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        Set<String> eventHandlerNames = Set.of("name1", "name2");

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(eventHandlerNames);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertThat(eventHandlers.size(), is(1));
        assertThat(eventHandlers.get(0), is(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventWithInactiveHandlers() {
        String event = "event";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("name1");
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("name2");
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);

        Set<String> eventHandlerNames = Set.of("name1", "name2");

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(eventHandlerNames);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertThat(eventHandlers.size(), is(2));
        assertThat(eventHandlers.get(0), is(eventHandler1));
        assertThat(eventHandlers.get(1), is(eventHandler2));
    }

    @Test
    public void testGetEventHandler() {
        String name = UUID.randomUUID().toString();
        String json = "eventHandlerJson";

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn(json);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(name);

        assertThat(eventHandler, is(readValue(json, EventHandler.class)));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNonExistingName() {
        String name = UUID.randomUUID().toString();

        redisEventHandlerDAO.getEventHandler(name);
    }
}