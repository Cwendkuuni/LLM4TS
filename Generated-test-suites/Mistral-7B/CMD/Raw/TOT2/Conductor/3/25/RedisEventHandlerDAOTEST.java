import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

public class RedisEventHandlerDAOTest {

 private static final Logger LOGGER = Mockito.mock(Logger.class);
 private static final String NAMESPACE = "TEST_NAMESPACE";

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

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(conductorProperties.getNamespace()).thenReturn(NAMESPACE);
 when(redisProperties.getEventHandlersNamespace()).thenReturn(NAMESPACE + ":");
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = createEventHandler("testEventHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(NsKey(EVENT_HANDLERS), eventHandler.getName(), JsonUtils.toJson(eventHandler));
 verifyNoMoreInteractions(jedisProxy);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithDuplicateName() {
 EventHandler eventHandler = createEventHandler("testEventHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler duplicateEventHandler = createEventHandler("testEventHandler");
 redisEventHandlerDAO.addEventHandler(duplicateEventHandler);

 verify(jedisProxy, times(2)).hset(NsKey(EVENT_HANDLERS), eventHandler.getName(), JsonUtils.toJson(eventHandler));
 verify(jedisProxy, times(1)).hset(NsKey(EVENT_HANDLERS), duplicateEventHandler.getName(), JsonUtils.toJson(duplicateEventHandler));
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = createEventHandler("testEventHandler");
 when(jedisProxy.hget(NsKey(EVENT_HANDLERS), eventHandler.getName())).thenReturn(JsonUtils.toJson(eventHandler));

 EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());

 assertEquals(eventHandler, actualEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistentName() {
 when(jedisProxy.hget(NsKey(EVENT_HANDLERS), "nonExistentEventHandlerName")).thenReturn(null);

 try {
 redisEventHandlerDAO.getEventHandler("nonExistentEventHandlerName");
 fail("Expected NotFoundException");
 } catch (NotFoundException e) {
 // Expected
 }
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = createEventHandler("existingEventHandler");
 EventHandler updatedEventHandler = new EventHandler("updatedEventHandlerName", "updatedEventHandler");

 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxy, times(1)).hset(NsKey(EVENT_HANDLERS), updatedEventHandler.getName(), JsonUtils.toJson(updatedEventHandler));
 verifyNoMoreInteractions(jedisProxy);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentName() {
 EventHandler updatedEventHandler = new EventHandler("updatedEventHandlerName", "updatedEventHandler");

 try {
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 fail("Expected NotFoundException");
 } catch (NotFoundException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = createEventHandler("testEventHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

 verify(jedisProxy, times(1)).hdel(NsKey(EVENT_HANDLERS), eventHandler.getName());
 verifyNoMoreInteractions(jedisProxy);
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentName() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandlerName");

 verifyNoMoreInteractions(jedisProxy);
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = createEventHandler("eventHandler1");
 EventHandler eventHandler2 = createEventHandler("eventHandler2");

 when(jedisProxy.hgetAll(NsKey(EVENT_HANDLERS))).thenReturn(ImmutableMap.<String, String>builder()
 .put(eventHandler1.getName(), JsonUtils.toJson(eventHandler1))
 .put(eventHandler2.getName(), JsonUtils.toJson(eventHandler2))
 .build());

 List<EventHandler> actualEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(2, actualEventHandlers.size());
 assertEquals(eventHandler1, actualEventHandlers.get(0));
 assertEquals(eventHandler2, actualEventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveHandlers() {
 EventHandler handler1 = createEventHandler("handler1");
 EventHandler handler2 = createEventHandler("handler2");

 when(jedisProxy.smembers(NsKey(EVENT_HANDLERS_BY_EVENT, "event"))).thenReturn(ImmutableSet.of("handler1", "handler2"));

 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event", false);

 assertEquals(2, actualHandlers.size());
 assertEquals(handler1, actualHandlers.get(0));
 assertEquals(handler2, actualHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithInactiveHandlers() {
 EventHandler handler1 = createEventHandler("handler1");
 EventHandler handler2 = createEventHandler("handler2");

 when(jedisProxy.smembers(NsKey(EVENT_HANDLERS_BY_EVENT, "event"))).thenReturn(ImmutableSet.of("handler1", "handler2"));

 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event", true);

 assertEquals(1, actualHandlers.size());
 assertFalse(actualHandlers.get(0).isActive());
 assertEquals(handler1, actualHandlers.get(0));
 }

 @Test
 public void testGetEventHandlersForEventWithNoHandlers() {
 when(jedisProxy.smembers(NsKey(EVENT_HANDLERS_BY_EVENT, "event"))).thenReturn(Collections.emptySet());

 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event", false);

 assertEquals(0, actualHandlers.size());
 }

 private EventHandler createEventHandler(String name) {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent(RandomStringUtils.randomAlphanumeric(5));
 eventHandler.setActive(true);
 return eventHandler;
 }

 private String NsKey(String... keys) {
 return String.join(":", Arrays.asList(NAMESPACE).get(0), Arrays.asList(keys).subList(1, keys.length));
 }
}