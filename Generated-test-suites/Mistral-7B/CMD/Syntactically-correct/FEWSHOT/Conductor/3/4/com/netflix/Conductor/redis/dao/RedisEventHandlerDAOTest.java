package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.jedis.JedisProxyMock;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private JedisProxyMock jedisProxyMock;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxyMock = new JedisProxyMock();
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxyMock,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testJson");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxyMock).hset(anyString(), eq("testName"), eq("testJson"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler("testName", "testEvent");
        when(jedisProxyMock.hget(anyString(), eq("testName"))).thenReturn("existingJson");
        redisEventHandlerDAO.addEventHandler(new EventHandler("testName", "testEvent"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("testName", "testEvent");
        EventHandler newEventHandler = new EventHandler("testName", "testEventUpdated");
        when(objectMapper.writeValueAsString(newEventHandler)).thenReturn("newJson");
        redisEventHandlerDAO.updateEventHandler(newEventHandler);
        verify(jedisProxyMock).hset(anyString(), eq("testName"), eq("newJson"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler newEventHandler = new EventHandler("testNameUpdated", "testEvent");
        when(jedisProxyMock.hget(anyString(), eq("testNameUpdated"))).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testName";
        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisProxyMock).hdel(anyString(), eq(name));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "testNameNotFound";
        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = Maps.newLinkedHashMap();
        all.put("testName1", "testJson1");
        all.put("testName2", "testJson2");
        when(jedisProxyMock.hgetAll(anyString())).thenReturn(all);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testIndexEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        redisEventHandlerDAO.index(eventHandler);
        String event = "testEvent";
        String key = "EVENT_HANDLERS_BY_EVENT_" + event;
        verify(jedisProxyMock).sadd(eq(key), eq(eventHandler.getName()));
    }

    @Test
    public void testRemoveIndexEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        redisEventHandlerDAO.removeIndex(eventHandler);
        String event = "testEvent";
        String key = "EVENT_HANDLERS_BY_EVENT_" + event;
        verify(jedisProxyMock).srem(eq(key), eq(eventHandler.getName()));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        Set<String> names = Lists.newLinkedHashSet("testName1", "testName2");
        when(jedisProxyMock.smembers(anyString())).thenReturn(names.toArray(new String[0]));
        when(jedisProxyMock.hget(anyString(), anyString())).thenAnswer(i -> "testJson");
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "testEvent";
        Set<String> names = Lists.newLinkedHashSet("testName1", "testName2");
        when(jedisProxyMock.smembers(anyString())).thenReturn(names.toArray(new String[0]));
        when(jedisProxyMock.hget(anyString(), anyString())).thenAnswer(i -> {
            EventHandler eventHandler = new EventHandler("testName", "testEvent");
            eventHandler.setActive(true);
            return toJson(eventHandler);
        });
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(1, handlers.size());
    }

    @Test
    public void testGetEventHandlerNotFound() {
        String name = "testNameNotFound";
        when(jedisProxyMock.hget(anyString(), eq(name))).thenReturn(null);
        try {
            redisEventHandlerDAO.getEventHandler(name);
            fail();
        } catch (NotFoundException nfe) {
            // expected
        }
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        when(jedisProxyMock.hget(anyString(), eq("testName"))).thenReturn("testJson");
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(name);
        assertEquals(eventHandler, retrievedEventHandler);
    }
}