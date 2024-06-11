package com.netflix.conductor.redis.dao;

import static com.netflix.conductor.common.metadata.events.EventHandler.EventStatus.ACTIVE;
import static com.netflix.conductor.common.metadata.events.EventHandler.EventStatus.INACTIVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.common.metadata.events.EventHandler.EventStatus;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseDynoDAOTest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest extends BaseDynoDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    private EventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");
        eventHandler.setActive(true);

        String json = toJson(eventHandler);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn(json);
        when(jedisProxy.hset(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"), eq(json))).thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        ArgumentCaptor<String> eventHandlerNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eventHandlerNameCaptor.capture(), jsonCaptor.capture());
        assertEquals("testHandler", eventHandlerNameCaptor.getValue());
        assertEquals(json, jsonCaptor.getValue());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler("testHandler", "testEvent");
        EventHandler newEventHandler = new EventHandler("testHandler", "testEvent2");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("testHandler", "testEvent");
        EventHandler updatedEventHandler = new EventHandler("testHandler", "testEvent2");
        updatedEventHandler.setActive(true);

        String json = toJson(updatedEventHandler);
        when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn(json);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        ArgumentCaptor<String> eventHandlerNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eventHandlerNameCaptor.capture(), jsonCaptor.capture());
        assertEquals("testHandler", eventHandlerNameCaptor.getValue());
        assertEquals(json, jsonCaptor.getValue());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler updatedEventHandler = new EventHandler("testHandler2", "testEvent2");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler2"))).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testHandler";

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(toJson(new EventHandler(name, "testEvent")));

        redisEventHandlerDAO.removeEventHandler(name);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy).hdel(eq(nsKey(EVENT_HANDLERS)), nameCaptor.capture());
        assertEquals(name, nameCaptor.getValue());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "testHandler2";

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testHandler1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testHandler2", "testEvent2");
        EventHandler eventHandler3 = new EventHandler("testHandler3", "testEvent3");

        Map<String, String> all = ImmutableMap.of(
                "testHandler1", toJson(eventHandler1),
                "testHandler2", toJson(eventHandler2),
                "testHandler3", toJson(eventHandler3)
        );
        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(all);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(3, handlers.size());
        List<EventHandler> expectedHandlers = ImmutableList.of(eventHandler1, eventHandler2, eventHandler3);
        for (EventHandler handler : handlers) {
            assertTrue(expectedHandlers.contains(handler));
        }
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("testHandler1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testHandler2", "testEvent1");
        EventHandler eventHandler3 = new EventHandler("testHandler3", "testEvent2");

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent1"))))
                .thenReturn(ImmutableSet.of("testHandler1", "testHandler2"));
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent2"))))
                .thenReturn(ImmutableSet.of("testHandler3"));

        List<EventHandler> handlers1 = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);
        List<EventHandler> handlers2 = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
        List<EventHandler> handlers3 = redisEventHandlerDAO.getEventHandlersForEvent("testEvent2", true);

        assertEquals(2, handlers1.size());
        assertEquals(2, handlers2.size());
        assertEquals(1, handlers3.size());

        List<EventHandler> expectedHandlers1 = Arrays.asList(eventHandler1, eventHandler2);
        List<EventHandler> expectedHandlers2 = Arrays.asList(eventHandler1, eventHandler2);
        List<EventHandler> expectedHandlers3 = Arrays.asList(eventHandler3);

        for (EventHandler handler : handlers1) {
            assertTrue(expectedHandlers1.contains(handler));
        }

        for (EventHandler handler : handlers2) {
            assertTrue(expectedHandlers2.contains(handler));
        }

        for (EventHandler handler : handlers3) {
            assertTrue(expectedHandlers3.contains(handler));
        }
    }

    @Test
    public void testGetEventHandlerByName() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");
        eventHandler.setActive(true);

        String json = toJson(eventHandler);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn(json);

        EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandlerByName("testHandler");

        assertEquals(eventHandler, returnedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerByNameNotFound() {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn(null);

        redisEventHandlerDAO.getEventHandlerByName("testHandler");
    }
}