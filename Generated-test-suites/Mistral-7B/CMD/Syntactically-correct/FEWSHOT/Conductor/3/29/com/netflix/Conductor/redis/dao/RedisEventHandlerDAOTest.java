package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisPropertiesTest;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private RedisProperties redisProperties;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisProperties = Mockito.spy(new RedisPropertiesTest());
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        when(redisProperties.getPrefix()).thenReturn("test:");

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                Mockito.mock(ConductorProperties.class),
                redisProperties);
    }

    @After
    public void teardown() {
        reset(jedisProxy);
    }

    @Test
    public void testAddEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler("test", "testEvent");
        assertEquals(EventHandler.class, eventHandler.getClass());

        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testData");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(eq("test:EVENT_HANDLERS"), eq(name), eq("testData"));
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        String name = UUID.randomUUID().toString();
        EventHandler eventHandler = new EventHandler("test", "testEvent");

        EventHandler existingEventHandler = new EventHandler("test", "testEvent");

        EventHandler existingEventHandlerFromDB = new EventHandler("test", "testEvent");
        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(name))).thenReturn("existingData");
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(existingEventHandlerFromDB);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(eq("test:EVENT_HANDLERS"), eq(name), anyString());
        verify(jedisProxy, times(0)).sadd(anyString(), anyString());
        verify(jedisProxy).hset(eq("test:EVENT_HANDLERS"), eq(name), eq("testData"));
    }

    @Test
    public void testUpdateEventHandler() {
        String name = UUID.randomUUID().toString();
        EventHandler existingEventHandler = new EventHandler("test", "testEvent");
        EventHandler updatedEventHandler = new EventHandler("test", "testEventUpdated");

        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(name))).thenReturn("existingData");
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(existingEventHandler);
        when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn("updatedData");

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy).hset(eq("test:EVENT_HANDLERS"), eq(name), eq("updatedData"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonexistentName() {
        String name = UUID.randomUUID().toString();

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy).hdel(eq("test:EVENT_HANDLERS"), eq(name));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("test1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("test2", "testEvent2");

        when(jedisProxy.hgetAll(eq("test:EVENT_HANDLERS")))
                .thenReturn(Map.ofEntries(Arrays.asList("test1", "eventHandler1Data"), Arrays.asList("test2", "eventHandler2Data")));
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenAnswer(i -> i.getArguments()[0]);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        String name1 = UUID.randomUUID().toString();
        String name2 = UUID.randomUUID().toString();
        String name3 = UUID.randomUUID().toString();

        when(jedisProxy.smembers(eq("test:EVENT_HANDLERS_BY_EVENT:testEvent")))
                .thenReturn(Arrays.asList(name1, name2, name3));

        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(name1)))
                .thenReturn("eventHandler1Data");
        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(name2)))
                .thenReturn("eventHandler2Data");
        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(name3)))
                .thenReturn("eventHandler3Data");

        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenAnswer(i -> i.getArguments()[0]);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(3, eventHandlers.size());
        assertEquals(name1, eventHandlers.get(0).getName());
        assertEquals(name2, eventHandlers.get(1).getName());
        assertEquals(name3, eventHandlers.get(2).getName());
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        String event = "testEvent";
        String nameActive = UUID.randomUUID().toString();
        String nameInactive = UUID.randomUUID().toString();

        when(jedisProxy.smembers(eq("test:EVENT_HANDLERS_BY_EVENT:testEvent")))
                .thenReturn(Arrays.asList(nameActive, nameInactive));

        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(nameActive)))
                .thenReturn("eventHandlerActiveData");
        when(jedisProxy.hget(eq("test:EVENT_HANDLERS"), eq(nameInactive)))
                .thenReturn("eventHandlerInactiveData");

        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenAnswer(i -> i.getArguments()[0]);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(nameActive, eventHandlers.get(0).getName());
    }
}