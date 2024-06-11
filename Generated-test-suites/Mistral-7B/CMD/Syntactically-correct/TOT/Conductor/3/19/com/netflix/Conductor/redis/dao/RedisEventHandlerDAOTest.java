package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.RedisTestUtil;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private RedisProperties redisProperties;

    private static final String NS_KEY_EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String NS_KEY_EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String EVENT_HANDLER_JSON = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}";

    @Before
    public void setup() {
        when(jedisProxy.hget(any(String.class), any(String.class))).thenReturn(EVENT_HANDLER_JSON);
        when(jedisProxy.hgetAll(any(String.class))).thenReturn(RedisTestUtil.getMapWithKeys(NS_KEY_EVENT_HANDLERS,
                NS_KEY_EVENT_HANDLERS_BY_EVENT));
        when(jedisProxy.sadd(any(String.class), any(String.class))).thenReturn(1L);
        when(jedisProxy.srem(any(String.class), any(String.class))).thenReturn(1L);
        when(objectMapper.readValue(any(String.class), eq(EventHandler.class))).then(args -> {
            EventHandler eventHandler = new EventHandler();
            Map<String, String> json = (Map<String, String>) args.getArguments()[0];
            eventHandler.setName(json.get("name"));
            eventHandler.setEvent(json.get("event"));
            eventHandler.setActive(Boolean.parseBoolean(json.get("active")));
            return eventHandler;
        });
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(EVENT_HANDLER_NAME, redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME).getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.addEventHandler(new EventHandler());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        assertNull(redisEventHandlerDAO.getEventHandler("nonExistentEventHandlerName"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.updateEventHandler(null);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerDifferentEvent() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent("differentEvent");
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
        assertNull(redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(true);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("eventHandler3");
        eventHandler3.setEvent("testEvent2");
        eventHandler3.setActive(false);

        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS)).thenReturn(
                RedisTestUtil.getMapWithKeys(NS_KEY_EVENT_HANDLERS, NS_KEY_EVENT_HANDLERS_BY_EVENT));
        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":testEvent1"))
                .thenReturn(RedisTestUtil.getSetWithElements("eventHandler1", "eventHandler2"));
        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":testEvent2"))
                .thenReturn(RedisUtil.getEmptySet());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(3, eventHandlers.size());
        assertEquals("eventHandler1", eventHandlers.get(0).getName());
        assertEquals("eventHandler2", eventHandlers.get(1).getName());
        assertEquals("eventHandler3", eventHandlers.get(2).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("eventHandler3");
        eventHandler3.setEvent("testEvent2");
        eventHandler3.setActive(true);

        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS)).thenReturn(
                RedisTestUtil.getMapWithKeys(NS_KEY_EVENT_HANDLERS, NS_KEY_EVENT_HANDLERS_BY_EVENT));
        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":testEvent1"))
                .thenReturn(RedisTestUtil.getSetWithElements("eventHandler1", "eventHandler2"));
        when(jedisProxy.hgetAll(NS_KEY_EVENT_HANDLERS_BY_EVENT + ":testEvent2"))
                .thenReturn(Collections.emptySet());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(2, eventHandlers.size());
        assertEquals("eventHandler1", eventHandlers.get(0).getName());
        assertEquals("eventHandler2", eventHandlers.get(1).getName());

        List<EventHandler> eventHandlersActive = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);

        assertEquals(1, eventHandlersActive.size());
        assertEquals("eventHandler1", eventHandlersActive.get(0).getName());
    }
}