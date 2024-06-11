package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
import com.netflix.conductor.redis.jedis.JedisProxy;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
    private static final String NAMESPACE = "test_namespace";
    private static final String NAME_A = "eventHandlerA";
    private static final String NAME_B = "eventHandlerB";
    private static final String NAME_C = "eventHandlerC";

    private final JedisProxy jedisProxy = Mockito.mock(JedisProxy.class);
    private final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private final ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
    private final RedisProperties redisProperties = Mockito.mock(RedisProperties.class);
    private final RedisEventHandlerDAO redisEventHandlerDAO = new RedisEventHandlerDAO(
            jedisProxy, objectMapper, conductorProperties, redisProperties);

    @Test
    public void testAddEventHandler() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_A);
        eventHandler.setEvent("eventA");
        eventHandler.setActive(true);

        Mockito.when(objectMapper.writeValueAsString(ArgumentMatchers.any()))
                .thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS),
                ArgumentMatchers.eq(NAME_A), ArgumentMatchers.eq("{}"));
        Mockito.verify(jedisProxy, Mockito.times(1)).sadd(ArgumentMatchers.eq(NAMESPACE + "_"
                + EVENT_HANDLERS_BY_EVENT + "_eventA"), ArgumentMatchers.eq(NAME_A));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        EventHandler eventHandlerA = new EventHandler();
        eventHandlerA.setName(NAME_A);
        eventHandlerA.setEvent("eventA");
        eventHandlerA.setActive(true);

        EventHandler eventHandlerB = new EventHandler();
        eventHandlerB.setName(NAME_A);
        eventHandlerB.setEvent("eventB");
        eventHandlerB.setActive(true);

        Mockito.when(objectMapper.writeValueAsString(ArgumentMatchers.any()))
                .thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandlerA);
        redisEventHandlerDAO.addEventHandler(eventHandlerB);
    }

    @Test
    public void testUpdateEventHandler() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");
        Preconditions.checkNotNull(redisEventHandlerDAO.getEventHandler(NAME_A), "EventHandler not found");

        EventHandler eventHandlerA = new EventHandler();
        eventHandlerA.setName(NAME_A);
        eventHandlerA.setEvent("eventA");
        eventHandlerA.setActive(true);
        eventHandlerA.setVersion(1);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(NAME_A);
        updatedEventHandler.setEvent("eventB");
        updatedEventHandler.setActive(false);
        updatedEventHandler.setVersion(2);

        Mockito.when(objectMapper.writeValueAsString(ArgumentMatchers.any()))
                .thenReturn("{}");
        Mockito.when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class)))
                .thenReturn(eventHandlerA);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Mockito.verify(jedisProxy).hset(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS),
                ArgumentMatchers.eq(NAME_A), ArgumentMatchers.eq("{}"));
        Mockito.verify(jedisProxy).srem(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS_BY_EVENT
                + "_eventA"), ArgumentMatchers.eq(NAME_A));
        Mockito.verify(jedisProxy).sadd(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS_BY_EVENT
                + "_eventB"), ArgumentMatchers.eq(NAME_A));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME_C);
        eventHandler.setEvent("eventC");
        eventHandler.setActive(true);

        Mockito.when(objectMapper.readValue(ArgumentMatchers.anyString(), ArgumentMatchers.eq(EventHandler.class)))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");
        Preconditions.checkNotNull(redisEventHandlerDAO.getEventHandler(NAME_A), "EventHandler not found");

        Mockito.when(jedisProxy.hget(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS), ArgumentMatchers.eq(NAME_A)))
                .thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler(NAME_A);

        Mockito.verify(jedisProxy).hdel(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS), ArgumentMatchers.eq(NAME_A));
        Mockito.verify(jedisProxy).srem(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS_BY_EVENT + "_eventA"), ArgumentMatchers.eq(NAME_A));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        Mockito.when(jedisProxy.hget(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS), ArgumentMatchers.eq(NAME_C)))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(NAME_C);
    }

    @Test
    public void testGetAllEventHandlers() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        List<EventHandler> allEventHandlers = Arrays.asList(
                new EventHandler().setName(NAME_A).setEvent("eventA").setActive(true),
                new EventHandler().setName(NAME_B).setEvent("eventB").setActive(false),
                new EventHandler().setName(NAME_C).setEvent("eventC").setActive(true)
        );

        Mockito.when(jedisProxy.hgetAll(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS)))
                .thenReturn(generateMockMap(allEventHandlers));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(allEventHandlers, eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        EventHandler eventHandlerA = new EventHandler().setName(NAME_A).setEvent("eventA").setActive(true);
        EventHandler eventHandlerB = new EventHandler().setName(NAME_B).setEvent("eventA").setActive(false);
        EventHandler eventHandlerC = new EventHandler().setName(NAME_C).setEvent("eventB").setActive(true);

        List<EventHandler> eventHandlersForEventA = Arrays.asList(eventHandlerA, eventHandlerB);
        List<EventHandler> eventHandlersForEventB = Collections.singletonList(eventHandlerC);

        Mockito.when(jedisProxy.smembers(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS_BY_EVENT + "_eventA")))
                .thenReturn(Collections.singleton(NAME_A));
        Mockito.when(jedisProxy.smembers(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS_BY_EVENT + "_eventB")))
                .thenReturn(Collections.singleton(NAME_C));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("eventA", false);
        assertEquals(eventHandlersForEventA, eventHandlers);

        List<EventHandler> eventHandlers2 = redisEventHandlerDAO.getEventHandlersForEvent("eventB", true);
        assertEquals(eventHandlersForEventB, eventHandlers2);
    }

    @Test
    public void testGetEventHandlerNotFound() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        Mockito.when(jedisProxy.hget(ArgumentMatchers.eq(NAMESPACE + "_" + EVENT_HANDLERS), ArgumentMatchers.eq(NAME_C)))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(NAME_C));
    }

    @Test
    public void testRecordRedisDaoEventRequests() {
        Preconditions.checkNotNull(redisEventHandlerDAO.getName(), "Missing name");

        String event = "addEventHandler";
        redisEventHandlerDAO.recordRedisDaoEventRequests(event);

        Mockito.verify(jedisProxy).hincrBy(ArgumentMatchers.eq(NAMESPACE + "_" + REDIS_DAO_EVENTS),
                ArgumentMatchers.eq(event), ArgumentMatchers.eq(1));
    }

    private Map<String, String> generateMockMap(List<EventHandler> eventHandlers) {
        LinkedList<Map.Entry<String, String>> entries = new LinkedList<>();
        eventHandlers.forEach(eventHandler -> entries.add(Map.entry(eventHandler.getName(), toJson(eventHandler))));
        return Mockito.mock(Map.class);
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}