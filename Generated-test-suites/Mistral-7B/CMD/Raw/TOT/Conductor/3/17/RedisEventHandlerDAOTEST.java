package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@Configuration
public class RedisEventHandlerDAOTest {

 private EventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxyMock;
 private ObjectMapper objectMapperMock;
 private ConductorProperties conductorPropertiesMock;
 private RedisProperties redisPropertiesMock;

 @Before
 public void setup() {
 jedisProxyMock = mock(JedisProxy.class);
 objectMapperMock = mock(ObjectMapper.class);
 conductorPropertiesMock = mock(ConductorProperties.class);
 redisPropertiesMock = mock(RedisProperties.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(
 JedisProxy.create(jedisProxyMock), objectMapperMock, conductorPropertiesMock, redisPropertiesMock);
 }

 @Test
 public void testAddEventHandlerSuccess() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 when(objectMapperMock.writeValueAsString(eventHandler)).thenReturn("testJson");
 when(jedisProxyMock.hset(anyString(), anyString(), anyString())).thenReturn(1);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxyMock, times(1)).hset(anyString(), anyString(), "testJson");
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setActive(true);
 when(objectMapperMock.writeValueAsString(existingEventHandler)).thenReturn("existingJson");
 when(jedisProxyMock.hget(anyString(), "testEventHandler")).thenReturn("existingJson");

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent");
 newEventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(newEventHandler);

 verify(jedisProxyMock, never()).hset(anyString(), anyString(), anyString());
 verify(jedisProxyMock, times(1)).hdel(anyString(), "testEventHandler");
 }

 @Test
 public void testUpdateEventHandlerSuccess() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setActive(true);
 when(objectMapperMock.writeValueAsString(existingEventHandler)).thenReturn("existingJson");
 when(jedisProxyMock.hget(anyString(), "testEventHandler")).thenReturn("existingJson");
 when(jedisProxyMock.hset(anyString(), anyString(), anyString())).thenReturn(1);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedEvent");
 updatedEventHandler.setActive(true);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxyMock, times(1)).hset(anyString(), "testEventHandler", "updatedJson");
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedEvent");
 updatedEventHandler.setActive(true);
 when(objectMapperMock.writeValueAsString(updatedEventHandler)).thenReturn("updatedJson");
 when(jedisProxyMock.hget(anyString(), "testEventHandler")).thenReturn(null);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxyMock, never()).hset(anyString(), "testEventHandler", "updatedJson");
 verify(jedisProxyMock, times(1)).hdel(anyString(), "testEventHandler");
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapperMock.writeValueAsString(eventHandler)).thenReturn("testJson");
 when(jedisProxyMock.hget(anyString(), "testEventHandler")).thenReturn("testJson");
 when(jedisProxyMock.hdel(anyString(), "testEventHandler")).thenReturn(1);

 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 verify(jedisProxyMock, times(1)).hdel(anyString(), "testEventHandler");
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 when(jedisProxyMock.hget(anyString(), "testEventHandler")).thenReturn(null);

 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 verify(jedisProxyMock, never()).hdel(anyString(), "testEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent1");
 eventHandler1.setActive(true);
 when(objectMapperMock.writeValueAsString(eventHandler1)).thenReturn("json1");
 when(jedisProxyMock.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of("testEventHandler1", "json1"));

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent2");
 eventHandler2.setActive(true);
 when(objectMapperMock.writeValueAsString(eventHandler2)).thenReturn("json2");
 when(jedisProxyMock.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.of("testEventHandler1", "json1", "testEventHandler2", "json2"));

 List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(2, allEventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testGetAllEventHandlersEmpty() {
 when(jedisProxyMock.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Map.ofKeys(new String[0]));

 redisEventHandlerDAO.getAllEventHandlers();

 verify(jedisProxyMock, never()).hgetAll(nsKey(EVENT_HANDLERS));
 }

 @Test
 public void testGetEventHandlersForEventSuccess() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 when(objectMapperMock.writeValueAsString(eventHandler1)).thenReturn("json1");
 when(jedisProxyMock.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Arrays.asList("testEventHandler1"));
 when(redisEventHandlerDAO.getEventHandler("testEventHandler1")).thenReturn(eventHandler1);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

 assertEquals(1, eventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlersForEventEmpty() {
 when(jedisProxyMock.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(new LinkedList<>());

 redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

 verify(jedisProxyMock, never()).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent"));
 }
}