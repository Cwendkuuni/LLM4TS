import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Conditional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

 @Mock
 private JedisProxy jedisProxy;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties properties;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 assertEquals(eventHandler.getName(), jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), anyString()).trim());
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler("existingEventHandler", "testEvent");
 EventHandler updatedEventHandler = new EventHandler("updatedEventHandler", "testEvent");

 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 assertEquals(updatedEventHandler.getName(), jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), anyString()).trim());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflict() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNotFound() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler("eventHandler1", "testEvent");
 EventHandler eventHandler2 = new EventHandler("eventHandler2", "testEvent");

 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Maps.newHashMap(
 Maps.entry("eventHandler1", "{}"),
 Maps.entry("eventHandler2", "{}")
 ));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(2, eventHandlers.size());
 assertEquals(eventHandler1.getName(), eventHandlers.get(0).getName());
 assertEquals(eventHandler2.getName(), eventHandlers.get(1).getName());
 }

 @Test
 public void testGetEventHandlerForEvent() {
 EventHandler eventHandler = new EventHandler("eventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Sets.newHashSet("eventHandler"));

 EventHandler result = redisEventHandlerDAO.getEventHandlerForEvent("testEvent", false);

 assertEquals(eventHandler.getName(), result.getName());
 }

 @Test
 public void testGetEventHandlerForEventWithActiveOnly() {
 EventHandler activeEventHandler = new EventHandler("activeEventHandler", "testEvent");
 activeEventHandler.setActive(true);
 EventHandler inactiveEventHandler = new EventHandler("inactiveEventHandler", "testEvent");

 redisEventHandlerDAO.addEventHandler(activeEventHandler);
 redisEventHandlerDAO.addEventHandler(inactiveEventHandler);

 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Sets.newHashSet("activeEventHandler", "inactiveEventHandler"));

 EventHandler result = redisEventHandlerDAO.getEventHandlerForEvent("testEvent", true);

 assertEquals(activeEventHandler.getName(), result.getName());
 }

 @Test
 public void testGetEventHandlerForEventWithNotFound() {
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "nonExistentEvent")))).thenReturn(Sets.newHashSet());

 assertThrows(NotFoundException.class, () -> {
 redisEventHandlerDAO.getEventHandlerForEvent("nonExistentEvent", false);
 });
 }

 @Test
 public void testGetEventHandlerForEventWithException() {
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenThrow(new RuntimeException());

 assertThrows(TransientException.class, () -> {
 redisEventHandlerDAO.getEventHandlerForEvent("testEvent", false);
 });
 }
}