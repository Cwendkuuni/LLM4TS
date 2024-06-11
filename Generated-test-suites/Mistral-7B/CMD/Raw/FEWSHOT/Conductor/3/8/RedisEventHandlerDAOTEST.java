package com.netflix.conductor.redis.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import com.netflix.conductor.redis.dao.BaseDynoDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

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
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

        when(redisProperties.getNamespace()).thenReturn("test");
        when(conductorProperties.getEventHandlersNamespace()).thenReturn("EVENT_HANDLERS");
    }

    @Test
    public void testAddEventHandler() throws ConflictException, JsonProcessingException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

        String json = toJson(eventHandler);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn(json);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq("testEventHandler"), json);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() throws ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws ConflictException, JsonProcessingException, NotFoundException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        String json = toJson(existingEventHandler);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(json);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("testEventUpdated");
        updatedEventHandler.setActive(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq("testEventHandler"), toJson(updatedEventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName()
            throws ConflictException, JsonProcessingException, NotFoundException {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandlerUpdated");
        updatedEventHandler.setEvent("testEventUpdated");
        updatedEventHandler.setActive(true);

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandlerUpdated")).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        String json = toJson(eventHandler);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(json);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq("testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() throws NotFoundException {
        redisEventHandlerDAO.removeEventHandler("testEventHandlerNonExistent");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");
        eventHandler2.setActive(false);

        String json1 = toJson(eventHandler1);
        String json2 = toJson(eventHandler2);

        Map<String, String> all = Map.of("testEventHandler1", json1, "testEventHandler2", json2);
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, handlers.size());
        assertThat(handlers, is(notNullValue()));
        for (EventHandler handler : handlers) {
            if (handler.getName().equals("testEventHandler1")) {
                assertEquals("testEvent1", handler.getEvent());
                assertTrue(handler.isActive());
            } else if (handler.getName().equals("testEventHandler2")) {
                assertEquals("testEvent2", handler.getEvent());
                assertFalse(handler.isActive());
            }
        }
    }

    @Test
    public void testGetEventHandlerForEvent() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        String json = toJson(eventHandler);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Arrays.asList("testEventHandler"));
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(json);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        assertEquals(1, handlers.size());
        assertThat(handlers, is(notNullValue()));
        assertEquals("testEventHandler", handlers.get(0).getName());
        assertEquals("testEvent", handlers.get(0).getEvent());
        assertTrue(handlers.get(0).isActive());
    }

    @Test
    public void testGetEventHandlerForEventWithActiveOnlyTrue() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(false);

        String json = toJson(eventHandler);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Arrays.asList("testEventHandler"));
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(json);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertEquals(0, handlers.size());
    }

    @Test
    public void testGetEventHandlerForEventWithNonExistentEvent() {
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEventNonExistent"))).thenReturn(Collections.emptyList());

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEventNonExistent", false);

        assertEquals(0, handlers.size());
    }

    private String nsKey(String key, String... additionalKeys) {
        return String.format("%s:%s%s", redisProperties.getNamespace(), EVENT_HANDLERS, String.join(":", Arrays.asList(key, additionalKeys)));
    }

    private String toJson(EventHandler eventHandler) throws JsonProcessingException {
        return objectMapper.writeValueAsString(eventHandler);
    }
}