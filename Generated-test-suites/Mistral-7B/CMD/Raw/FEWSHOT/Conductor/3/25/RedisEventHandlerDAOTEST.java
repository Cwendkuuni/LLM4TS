package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

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
import com.netflix.conductor.redis.test.BaseRedisTest;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseRedisTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);
    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

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
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        Preconditions.checkNotNull(eventHandler.getName(), "Missing Name");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy).hset(keyCaptor.capture(), eq("testEventHandler"), jsonCaptor.capture());
        String json = jsonCaptor.getValue();
        assertEquals(toJson(eventHandler), json);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent"), eq("testEventHandler"));
        recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent2");

        when(jedisProxy.hget(anyString(), eq("testEventHandler"))).thenReturn(toJson(existingEventHandler));
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    // Add other test methods for updateEventHandler, removeEventHandler, getAllEventHandlers, index, removeIndex,
    // getEventHandlersForEvent, and getEventHandler methods.
}