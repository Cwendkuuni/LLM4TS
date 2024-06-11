package com.netflix.conductor.redis.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

 private static final String JSON_EVENT_HANDLER = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}";
 private static final String JSON_EVENT_HANDLERS = "{\"EVENT_HANDLERS\":" + JSON_EVENT_HANDLER + "}";
 private static final String JSON_EVENT_HANDLERS_BY_EVENT = "{\"EVENT_HANDLERS_BY_EVENT\":{\"testEvent\":[\"testEventHandler\"]}}";

 @Mock
 private JedisProxy jedisProxy;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties redisProperties;

 @Mock
 private ObjectMapper objectMapper;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 private RedisEventHandlerDAO testRedisEventHandlerDAO;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 testRedisEventHandlerDAO = new RedisEventHandlerDAO(
         mock(JedisProxy.class), mock(ObjectMapper.class), mock(ConductorProperties.class), mock(RedisProperties.class));
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn(JSON_EVENT_HANDLER);
 when(jedisProxy.hset(StringUtils.isEmpty(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS)) ? "" : testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS),
 "testEventHandler", JSON_EVENT_HANDLER)).thenReturn(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 Map<String, String> eventHandlers = jedisProxy.hgetAll(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS));
 assertEquals(1, eventHandlers.size());
 assertEquals(JSON_EVENT_HANDLER, eventHandlers.get("testEventHandler"));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn(JSON_EVENT_HANDLER);
 when(jedisProxy.hset(StringUtils.isEmpty(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS)) ? "" : testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS),
 "testEventHandler", JSON_EVENT_HANDLER)).thenThrow(new RuntimeException("Redis test exception"));
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = testRedisEventHandlerDAO.getEventHandler("testEventHandler");
 assertNull(eventHandler);
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(JSON_EVENT_HANDLER);
 when(objectMapper.readValue(JSON_EVENT_HANDLER, EventHandler.class)).thenReturn(eventHandler);
 eventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(eventHandler.getName(), "testEventHandler");
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistingEventHandler() {
 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertNull(eventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setActive(true);
 when(objectMapper.writeValueAsString(existingEventHandler)).thenReturn(JSON_EVENT_HANDLER);
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(JSON_EVENT_HANDLER);
 when(objectMapper.writeValueAsString(new EventHandler("testEventHandler2", "testEvent", true))).thenReturn(JSON_EVENT_HANDLER + "_updated");
 when(jedisProxy.hset(StringUtils.isEmpty(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS)) ? "" : testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS),
 "testEventHandler", JSON_EVENT_HANDLER + "_updated"))
 .thenReturn(true);
 EventHandler updatedEventHandler = redisEventHandlerDAO.updateEventHandler(existingEventHandler);
 assertEquals(updatedEventHandler.getName(), "testEventHandler2");
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistingEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler2");
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn(JSON_EVENT_HANDLER);
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(JSON_EVENT_HANDLER);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 Map<String, String> eventHandlers = jedisProxy.hgetAll(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS));
 assertEquals(0, eventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistingEventHandler() {
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 Map<String, String> eventHandlers = Maps.newHashMap();
 eventHandlers.put("testEventHandler1", JSON_EVENT_HANDLER);
 eventHandlers.put("testEventHandler2", JSON_EVENT_HANDLER + "_updated");
 when(jedisProxy.hgetAll(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS))).thenReturn(eventHandlers);
 List<EventHandler> eventHandlersList = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlersList.size());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 String event = "testEvent";
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(false);
 when(jedisProxy.smembers(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Sets.newHashSet("testEventHandler1", "testEventHandler2"));
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler1")).thenReturn(JSON_EVENT_HANDLER);
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler2")).thenReturn(JSON_EVENT_HANDLER + "_updated");
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithActiveOnlyTrue() {
 String event = "testEvent";
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(false);
 when(jedisProxy.smembers(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Sets.newHashSet("testEventHandler1", "testEventHandler2"));
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler1")).thenReturn(JSON_EVENT_HANDLER);
 when(jedisProxy.hget(testRedisEventHandlerDAO.nsKey(EVENT_HANDLERS), "testEventHandler2")).thenReturn(JSON_EVENT_HANDLER + "_updated");
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
 assertEquals(1, eventHandlers.size());
 }
}