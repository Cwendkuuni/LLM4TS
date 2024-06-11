package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

 @Mock
 private JedisProxy jedisProxy;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void before() {
    MockitoAnnotations.initMocks(this);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandler_WithoutName() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("EventName");

    when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandler);

    redisEventHandlerDAO.addEventHandler(eventHandler);

    verify(jedisProxy, times(1)).hget(anyString(), anyString());
 }

 @Test
 public void testUpdateEventHandler_WithExistingName() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("EventName");
    eventHandler.setEvent("Event");

    when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

    redisEventHandlerDAO.updateEventHandler(eventHandler);

    verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandler_WithNonExistingName() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("EventName");

    when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);

    redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
 }

 @Test
 public void testGetAllEventHandlers() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("EventName");

    when(jedisProxy.hgetAll(anyString())).thenReturn(Arrays.asList(eventHandler));

    List<EventHandler> allHandlers = redisEventHandlerDAO.getAllEventHandlers();

    verify(jedisProxy, times(1)).hgetAll(anyString());
    assertNotNull(allHandlers);
    assertEquals(1, allHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent_ActiveOnly() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("EventName");
    eventHandler.setActive(true);

    when(jedisProxy.smembers(anyString())).thenReturn(Arrays.asList(eventHandler.getName()));

    when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

    List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler.getEvent(), true);

    verify(jedisProxy, times(1)).smembers(anyString());
    assertNotNull(handlers);
    assertEquals(1, handlers.size());
    assertTrue(handlers.get(0).isActive());
 }
}
