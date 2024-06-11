package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseRedisTest;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseRedisTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisEventHandlerDAO dao;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String NS_KEY = "namespace:1";

    private EventHandler eventHandler1;
    private EventHandler eventHandler2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventHandler1 = new EventHandler();
        eventHandler1.setName("handler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        eventHandler2 = new EventHandler();
        eventHandler2.setName("handler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Maps.newHashMap());
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "handler1")).thenReturn("{}");
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "handler2")).thenReturn("{}");
        when(jedisProxy.sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")), anyString())).thenAnswer(i -> true);
        when(jedisProxy.srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")), anyString())).thenAnswer(i -> true);
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event1"))).thenReturn(Sets.newHashSet("handler1", "handler2"));
    }

    @After
    public void tearDown() {
        reset(jedisProxy, conductorProperties, properties, objectMapper);
    }

    @Test
    public void testAddEventHandler() {
        dao.addEventHandler(eventHandler1);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eq("handler1"), anyString());
        verify(jedisProxy, times(2)).hget(nsKey(EVENT_HANDLERS), eq("handler1"));
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eq("event1")), eq("handler1"));
        verifyZeroInteractions(jedisProxy).hdel(anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        dao.addEventHandler(eventHandler1);
        dao.addEventHandler(eventHandler1);

        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eq("handler1"), anyString());
        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), eq("handler1")).twice();
        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), eq("handler1"));
        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eq("handler1"), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        dao.addEventHandler(eventHandler1);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("handler1");
        updatedEventHandler.setEvent("event1");
        updatedEventHandler.setActive(false);

        dao.updateEventHandler(updatedEventHandler);

        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), eq("handler1"));
        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), eq("handler1"));
        verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eq("handler1"), anyString());
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eq("event1")), eq("handler1"));
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eq("event1")), eq("handler1"));
    }

    @Test
    public void testRemoveEventHandler() {
        dao.addEventHandler(eventHandler1);

        dao.removeEventHandler("handler1");

        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), eq("handler1"));
        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), eq("handler1"));
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eq("event1")), eq("handler1"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        dao.removeEventHandler("handler3");

        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), eq("handler3"));
        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), eq("handler3"));
        verify(jedisProxy).srem(nsKey(EVENT_HANDLERS_BY_EVENT, eq("event1")), eq("handler3"));
    }

    @Test
    public void testGetAllEventHandlers() {
        setupGetAllEventHandlers();

        List<EventHandler> eventHandlers = dao.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        setupGetEventHandlersForEvent();

        List<EventHandler> eventHandlers = dao.getEventHandlersForEvent("event1", false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        setupGetEventHandlersForEvent();

        List<EventHandler> eventHandlers = dao.getEventHandlersForEvent("event1", true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @Test
    public void testGetEventHandlerNotFound() {
        reset(jedisProxy);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "handler3")).thenReturn(null);

        assertThrows(NotFoundException.class, () -> dao.getEventHandler("handler3"));

        verify(jedisProxy).hget(nsKey(EVENT_HANDLERS), "handler3");
    }

    private void setupGetAllEventHandlers() {
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Maps.newHashMap());
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "handler1")).thenReturn("{}");
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "handler2")).thenReturn("{}");
    }

    private void setupGetEventHandlersForEvent() {
        setupGetAllEventHandlers();
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event1"))).thenReturn(Sets.newHashSet("handler1", "handler2"));
    }
}