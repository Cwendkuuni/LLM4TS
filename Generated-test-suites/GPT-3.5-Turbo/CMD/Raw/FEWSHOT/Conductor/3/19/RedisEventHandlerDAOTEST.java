package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy = mock(JedisProxy.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        when(jedisProxy.hset(any(), any(), any())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("testName"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict(){
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(any(), any())).thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(any(), any())).thenReturn("{ \"name\": \"testName\", \"event\": \"testEvent\", \"active\": true}");
        when(jedisProxy.hset(any(), any(), any())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals("testEvent", redisEventHandlerDAO.getEventHandler("testName").getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(any(), any())).thenReturn("{ \"name\": \"testName\", \"event\": \"testEvent\", \"active\": true}");
        when(jedisProxy.hdel(any(), any())).thenReturn(1L);
        redisEventHandlerDAO.removeEventHandler("testName");
        assertNull(redisEventHandlerDAO.getEventHandler("testName"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("testName");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testEvent1", "testName1", true);
        EventHandler eventHandler2 = new EventHandler("testEvent2", "testName2", false);
        when(jedisProxy.hgetAll(any())).thenReturn(
                List.of("testName1", "{\"name\":\"testName1\",\"event\":\"testEvent1\",\"active\":true}",
                        "testName2", "{\"name\":\"testName2\",\"event\":\"testEvent2\",\"active\":false}")
        );
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertEquals("testName1", eventHandlers.get(0).getName());
        assertEquals("testName2", eventHandlers.get(1).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.smembers(any())).thenReturn(Set.of("testName"));
        when(jedisProxy.hget(any(), any())).thenReturn("{ \"name\": \"testName\", \"event\": \"testEvent\", \"active\": true}");
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(1, eventHandlers.size());
        assertEquals("testName", eventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(any(), any())).thenReturn("{ \"name\": \"testName\", \"event\": \"testEvent\", \"active\": true}");
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testName");
        assertNotNull(retrievedEventHandler);
        assertEquals("testName", retrievedEventHandler.getName());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(any(), any())).thenThrow(new NullPointerException());
        redisEventHandlerDAO.getEventHandler("testName");
    }

}