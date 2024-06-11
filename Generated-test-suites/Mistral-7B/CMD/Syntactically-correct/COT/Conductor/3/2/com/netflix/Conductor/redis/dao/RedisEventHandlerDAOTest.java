import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 jedisProxy = Mockito.mock(JedisProxy.class);
 objectMapper = Mockito.mock(ObjectMapper.class);
 conductorProperties = Mockito.mock(ConductorProperties.class);
 redisProperties = Mockito.mock(RedisProperties.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 // Verify the event handler has been added to Redis
 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.eq("testEventHandler"), Mockito.anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("existingEventHandler");
 existingEventHandler.setEvent("existingEvent");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("existingEventHandler");
 newEventHandler.setEvent("newEvent");
 redisEventHandlerDAO.addEventHandler(newEventHandler);
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 String json = objectMapper.writeValueAsString(eventHandler);
 Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler"))).thenReturn(json);

 EventHandler result = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(eventHandler, result);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistentName() {
 Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("nonExistentEventHandler"))).thenReturn(null);

 try {
 redisEventHandlerDAO.getEventHandler("nonExistentEventHandler");
 fail();
 } catch (NotFoundException nfe) {
 // Expected
 }
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("existingEventHandler");
 existingEventHandler.setEvent("existingEvent");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("existingEventHandler");
 newEventHandler.setEvent("newEvent");

 redisEventHandlerDAO.updateEventHandler(newEventHandler);

 // Verify the event handler has been updated in Redis
 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)), Mockito.eq("existingEventHandler"), Mockito.anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentEventHandler() {
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("nonExistentEventHandler");
 newEventHandler.setEvent("newEvent");
 redisEventHandlerDAO.updateEventHandler(newEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 // Verify the event handler has been removed from Redis
 Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.eq("testEventHandler"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentEventHandler() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler handler1 = new EventHandler();
 handler1.setName("handler1");
 handler1.setEvent("event1");
 EventHandler handler2 = new EventHandler();
 handler2.setName("handler2");
 handler2.setEvent("event2");

 Mockito.when(jedisProxy.hgetAll(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)))).thenReturn(Map.of("handler1", "handler1Json", "handler2", "handler2Json"));
 List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, result.size());
 assertEquals(handler1, result.get(0));
 assertEquals(handler2, result.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveHandlersOnly() {
 EventHandler handler1 = new EventHandler();
 handler1.setName("handler1");
 handler1.setEvent("event1");
 handler1.setActive(true);
 EventHandler handler2 = new EventHandler();
 handler2.setName("handler2");
 handler2.setEvent("event1");
 handler2.setActive(false);

 Mockito.when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")))).thenReturn(Set.of("handler1", "handler2"));
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(1, result.size());
 assertEquals(handler1, result.get(0));
 }

 @Test
 public void testGetEventHandlersForEventWithAllHandlers() {
 EventHandler handler1 = new EventHandler();
 handler1.setName("handler1");
 handler1.setEvent("event1");
 EventHandler handler2 = new EventHandler();
 handler2.setName("handler2");
 handler2.setEvent("event1");

 Mockito.when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS_BY_EVENT, "event1")))).thenReturn(Set.of("handler1", "handler2"));
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
 assertEquals(2, result.size());
 assertEquals(handler1, result.get(0));
 assertEquals(handler2, result.get(1));
 }
}