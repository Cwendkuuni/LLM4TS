package com.netflix.conductor.redis.dao;

import static com.netflix.conductor.common.metadata.events.EventHandler.Builder.newEventHandler;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.netflix.conductor.redis.util.RedisTestUtil;
import com.netflix.conductor.redis.util.TestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
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
        EventHandler eventHandler = newEventHandler().withName(name).build();

        when(jedisProxy.hset(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name, null)).thenReturn(1);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("eventHandlerJson");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        when(jedisProxy.hset(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), nameCaptor.capture(), jsonCaptor.capture()))
                .thenReturn(1);

        assertThat(jedisProxy.hgetAll(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), is(RedisTestUtil.mapOf("name", name, "value", "eventHandlerJson")));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        String name = UUID.randomUUID().toString();
        EventHandler existingEventHandler = newEventHandler().withName(name).build();
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = newEventHandler().withName(name).build();

        when(jedisProxy.hget(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn("existingEventHandlerJson");

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testGetEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = newEventHandler().withName(name).build();

        when(jedisProxy.hget(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn("eventHandlerJson");

        EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandler(name);

        assertThat(returnedEventHandler, is(eventHandler));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNonExistentName() {
        String name = UUID.randomUUID().toString();

        when(jedisProxy.hget(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn(null);

        redisEventHandlerDAO.getEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = Arrays.asList(
                newEventHandler().withName("eh1").build(),
                newEventHandler().withName("eh2").build()
        );

        when(jedisProxy.hgetAll(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(RedisTestUtil.mapOf("name1", "eventHandler1Json", "name2", "eventHandler2Json"));

        List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        TestUtils.assertListEquals(eventHandlers, returnedEventHandlers);
    }

    @Test
    public void testUpdateEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = newEventHandler().withName(name).build();
        String newJson = "newEventHandlerJson";

        when(jedisProxy.hget(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn("eventHandlerJson");
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("eventHandlerJson");
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn(newJson);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        when(jedisProxy.hset(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), nameCaptor.capture(), jsonCaptor.capture()))
                .thenReturn(1);

        assertThat(jedisProxy.hgetAll(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), is(RedisTestUtil.mapOf("name", name, "value", newJson)));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = newEventHandler().withName(name).build();

        when(jedisProxy.hget(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = newEventHandler().withName(name).build();

        when(jedisProxy.hget(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn("eventHandlerJson");

        redisEventHandlerDAO.removeEventHandler(name);

        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        when(jedisProxy.hdel(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), nameCaptor.capture())).thenReturn(1);

        assertThat(jedisProxy.hgetAll(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), is(emptyMap()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        String name = UUID.randomUUID().toString();

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "event";
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();

        when(jedisProxy.smembers(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Arrays.asList(name1, name2).stream().map(n -> n.toString()).collect(Collectors.toList()));

        List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        TestUtils.assertListEquals(Arrays.asList(newEventHandler().withName(name1).build(), newEventHandler().withName(name2).build()), returnedEventHandlers);

        List<EventHandler> returnedEventHandlersActive = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        TestUtils.assertListEquals(Arrays.asList(newEventHandler().withName(name1).build()), returnedEventHandlersActive);
    }

    @Test
    public void testGetEventHandlersForEventWithEmptySet() {
        String event = "event";

        when(jedisProxy.smembers(RedisTestUtil.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Collections.emptyList());

        List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertNull(returnedEventHandlers);
    }
}