package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisPropertiesTest;
import com.netflix.conductor.test.TestObjectMapper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@org.junit.runner.RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final String EVENT_HANDLER_NAME_1 = "handler1";
    private static final String EVENT_HANDLER_NAME_2 = "handler2";
    private static final String EVENT = "testEvent";

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @org.junit.Before
    public void setup() {
        this.jedisProxy = mock(JedisProxy.class);
        this.objectMapper = new TestObjectMapper();
        this.conductorProperties = new ConductorProperties();
        this.redisProperties = new RedisPropertiesTest().createRedisProperties();
        this.redisEventHandlerDAO = new RedisEventHandlerDAO(
                this.jedisProxy,
                this.objectMapper,
                this.conductorProperties,
                this.redisProperties);
    }

    @org.junit.Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "handler1");
        this.redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(this.jedisProxy, times(1)).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler.getName()), anyString());
    }

    @org.junit.Test(expected = ConflictException.class)
    public void testAddEventHandlerAlreadyExists() {
        this.redisEventHandlerDAO.addEventHandler(new EventHandler("testEvent", "handler1"));
        this.redisEventHandlerDAO.addEventHandler(new EventHandler("testEvent", "handler1"));
    }

    @org.junit.Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("testEvent", "handler1");
        this.redisEventHandlerDAO.addEventHandler(existingEventHandler);
        EventHandler updatedEventHandler = new EventHandler("testEvent", "handler2");
        this.redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(this.jedisProxy, times(1)).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(existingEventHandler.getName()), anyString());
    }

    @org.junit.Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        this.redisEventHandlerDAO.updateEventHandler(new EventHandler("testEvent", "handler2"));
    }

    @org.junit.Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testEvent", "handler1");
        EventHandler eventHandler2 = new EventHandler("testEvent", "handler2");
        when(this.jedisProxy.hgetAll(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)))).thenReturn(Map.ofEntries(Map.entry(eventHandler1.getName(), toJson(eventHandler1))));

        List<EventHandler> allEventHandlers = this.redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, allEventHandlers.size());
        assertEquals(eventHandler1, allEventHandlers.get(0));
    }

    @org.junit.Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "handler1");
        this.redisEventHandlerDAO.addEventHandler(eventHandler);
        this.redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(this.jedisProxy, times(1)).hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler.getName()));
    }

    @org.junit.Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        this.redisEventHandlerDAO.removeEventHandler("handler1");
    }

    @org.junit.Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("testEvent", "handler1");
        EventHandler eventHandler2 = new EventHandler("testEvent", "handler2");
        when(this.jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Arrays.asList("handler1", "handler2"));

        List<EventHandler> eventHandlers = this.redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @org.junit.Test
    public void testGetEventHandlersForEventActiveOnly() {
        EventHandler eventHandler1 = new EventHandler("testEvent", "handler1");
        EventHandler eventHandler2 = new EventHandler("testEvent", "handler2");
        when(this.jedisProxy.smembers(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Arrays.asList("handler1", "handler2"));
        when(eventHandler1.isActive()).thenReturn(true);
        when(eventHandler2.isActive()).thenReturn(false);

        List<EventHandler> eventHandlers = this.redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @org.junit.Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "handler1");
        String json = toJson(eventHandler);
        when(this.jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler.getName()))).thenReturn(json);

        EventHandler retrievedEventHandler = this.redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @org.junit.Test(expected = TransientException.class)
    public void testGetEventHandlerException() {
        when(this.jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), anyString())).thenThrow(new RuntimeException());

        this.redisEventHandlerDAO.getEventHandler("handler1");
    }

    @org.junit.Test
    public void testGetEventHandlerNotFound() {
        when(this.jedisProxy.hget(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), anyString())).thenReturn(null);

        try {
            this.redisEventHandlerDAO.getEventHandler("handler1");
            fail();
        } catch (NotFoundException e) {
            // expected
        }
    }

    private String nsKey(String... keys) {
        return this.redisProperties.getNamespace() + ":" + String.join(":", Arrays.asList(keys));
    }

    private String toJson(EventHandler eventHandler) {
        return this.objectMapper.writeValueAsString(eventHandler);
    }
}