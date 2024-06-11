package com.netflix.conductor.redis.dao.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.testutils.TestUtils;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler");
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nsKeyCaptor = ArgumentCaptor.forClass(String.class);
        when(jedisProxy.hset(nsKeyCaptor.capture(), eq(name), eq("{}"))).thenReturn(1L);
        when(jedisProxy.sadd(ImmutableSet.of(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")).get(0), name))
                .thenReturn(1L);

        redisEventHandlerDAO.recordRedisDaoRequests("addEventHandler");

        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertEquals(1, jedisProxy.scard(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")));
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        String name = UUID.randomUUID().toString();
        String newEvent = "newEvent";
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler");
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        ArgumentCaptor<String> oldNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> newNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> oldValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> newValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nsKeyCaptor = ArgumentCaptor.forClass(String.class);
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), oldNameCaptor.capture())).thenReturn("{}");
        when(jedisProxy.hset(nsKey(EVENT_HANDLERS), newNameCaptor.capture(), newValueCaptor.capture()))
                .thenReturn(1L);
        when(jedisProxy.srem(ImmutableSet.of(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")).get(0), name))
                .thenReturn(1L);
        when(jedisProxy.sadd(ImmutableSet.of(nsKey(EVENT_HANDLERS_BY_EVENT, "newEvent")).get(0), name))
                .thenReturn(1L);

        redisEventHandlerDAO.recordRedisDaoRequests("updateEventHandler");

        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertEquals(1, jedisProxy.scard(nsKey(EVENT_HANDLERS_BY_EVENT, newEvent)));
        assertEquals(name, oldNameCaptor.getValue());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() throws Exception {
        String name = "existingName";
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() throws Exception {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String name = UUID.randomUUID().toString();
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler(name);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        when(jedisProxy.hdel(nsKey(EVENT_HANDLERS), name)).thenReturn(1L);
        when(jedisProxy.srem(ImmutableSet.of(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")).get(0), name))
                .thenReturn(1L);

        redisEventHandlerDAO.recordRedisDaoRequests("removeEventHandler");

        assertEquals(0, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertEquals(0, jedisProxy.scard(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() throws Exception {
        String name = UUID.randomUUID().toString();

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = new EventHandler("testEvent1", "testHandler1");
        when(objectMapper.writeValueAsString(eventHandler1)).thenReturn("{}");

        EventHandler eventHandler2 = new EventHandler("testEvent2", "testHandler2");
        when(objectMapper.writeValueAsString(eventHandler2)).thenReturn("{}");

        Map<String, String> eventHandlers = ImmutableMap.of("testHandler1Name", "{}", "testHandler2Name", "{}");

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(eventHandlers);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertEquals("testHandler1", allEventHandlers.get(0).getHandler());
        assertEquals("testHandler2", allEventHandlers.get(1).getHandler());
    }

    @Test
    public void testGetEventHandlerForEventWithActiveHandler() throws Exception {
        String event = "testEvent";
        String handlerName1 = "handler1";
        String handlerName2 = "handler2";

        EventHandler eventHandler1 = new EventHandler(event, handlerName1);
        EventHandler eventHandler2 = new EventHandler(event, handlerName2);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Arrays.asList(handlerName1, handlerName2).toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlerForEventWithInactiveHandler() throws Exception {
        String event = "testEvent";
        String handlerName1 = "handler1";
        String handlerName2 = "handler2";

        EventHandler eventHandler1 = new EventHandler(event, handlerName1);
        EventHandler eventHandler2 = new EventHandler(event, handlerName2);

        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Arrays.asList(handlerName1, handlerName2).toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertFalse(eventHandler2.isActive());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerForNonExistentEvent() throws Exception {
        String event = "nonExistentEvent";

        redisEventHandlerDAO.getEventHandlersForEvent(event, false);
    }

    private static String nsKey(String... keys) {
        return TestUtils.nsKey(RedisEventHandlerDAO.class, keys);
    }

    private static <T> T readValue(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new TransientException(String.format("Unable to parse value of type %s from JSON: %s", valueType.getSimpleName(), json), e);
        }
    }
}