import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;

 @Before
 public void setup() {
 jedisProxy = Mockito.mock(JedisProxy.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = Mockito.spy(new EventHandler("testEventHandler", "testEvent"));
 redisEventHandlerDAO.addEventHandler(eventHandler);
 Mockito.verify(jedisProxy, Mockito.times(1)).hset(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
 ArgumentMatchers.eq(eventHandler.getName()), ArgumentMatchors.anyString());
 Mockito.verify(jedisProxy, Mockito.times(1)).sadd(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")),
 ArgumentMatchors.eq(eventHandler.getName()));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler eventHandler = Mockito.spy(new EventHandler("testEventHandler", "testEvent"));
 redisEventHandlerDAO.addEventHandler(eventHandler);
 Mockito.when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), ArgumentMatchers.eq("testEventHandler")))
 .thenReturn("existingHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistingName() {
 Mockito.when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), ArgumentMatchers.eq("nonexistentHandler")))
 .thenReturn(null);
 assertNull(redisEventHandlerDAO.getEventHandler("nonexistentHandler"));
 }

 @Test
 public void testGetEventHandlerWithExistingName() {
 EventHandler eventHandler = new EventHandler("existingHandler", "testEvent");
 Mockito.when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), ArgumentMatchers.eq("existingHandler")))
 .thenReturn(toJson(eventHandler));
 EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler("existingHandler");
 assertEquals(eventHandler, actualEventHandler);
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler("eventHandler1", "event1");
 EventHandler eventHandler2 = new EventHandler("eventHandler2", "event2");
 Mockito.when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)))
 .thenReturn(Map.ofEntries(Map.entry("eventHandler1", toJson(eventHandler1)), Map.entry("eventHandler2", toJson(eventHandler2))));
 List<EventHandler> expectedHandlers = Lists.newArrayList(eventHandler1, eventHandler2);
 List<EventHandler> actualHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(expectedHandlers, actualHandlers);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlersForNonExistentEvent() {
 Mockito.when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "nonExistentEvent")))
 .thenReturn(Set.of());
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false));
 }

 @Test
 public void testGetEventHandlersForEventWithNoActiveHandlers() {
 EventHandler eventHandler1 = Mockito.spy(new EventHandler("eventHandler1", "testEvent") {
 @Override
 public boolean isActive() {
 return false;
 }
 });
 EventHandler eventHandler2 = new EventHandler("eventHandler2", "testEvent");
 Mockito.when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
 .thenReturn(Set.of("eventHandler1", "eventHandler2"));
 Mockito.when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), ArgumentMatchers.eq("eventHandler1")))
 .thenReturn(toJson(eventHandler1));
 List<EventHandler> expectedHandlers = Lists.newArrayList(eventHandler1);
 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(expectedHandlers, actualHandlers);
 }

 @Test
 public void testGetEventHandlersForEventWithActiveHandlers() {
 EventHandler eventHandler1 = new EventHandler("eventHandler1", "testEvent") {
 @Override
 public boolean isActive() {
 return true;
 }
 };
 EventHandler eventHandler2 = new EventHandler("eventHandler2", "testEvent") {
 @Override
 public boolean isActive() {
 return false;
 }
 };
 Mockito.when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))
 .thenReturn(Set.of("eventHandler1", "eventHandler2"));
 Mockito.when(jedisProxy.hget(redisEventHandlerDAO.nsKey(EVENT_HANDLERS), ArgumentMatchers.eq("eventHandler1")))
 .thenReturn(toJson(eventHandler1));
 List<EventHandler> expectedHandlers = Lists.newArrayList(eventHandler1);
 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(expectedHandlers, actualHandlers);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 Mockito.verify(jedisProxy, Mockito.times(1)).hdel(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
 ArgumentMatchors.eq("testEventHandler"));
 Mockito.verify(jedisProxy, Mockito.times(1)).srem(ArgumentMatchers.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")),
 ArgumentMatchors.eq("testEventHandler"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistingName() {
 redisEventHandlerDAO.removeEventHandler("nonExistingHandler");
 }
}