import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.config.RedisProperties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Mock
 private JedisProxy jedisProxy;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties redisProperties;

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
 Mockito.eq("testEventHandler"), Mockito.anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(newEventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.updateEventHandler(eventHandler);

 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)),
 Mockito.eq("testEventHandler"), Mockito.anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(null);
 redisEventHandlerDAO.updateEventHandler(eventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistentEventHandler() {
 when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(null);
 assertNull(redisEventHandlerDAO.getEventHandler("testEventHandler"));
 }

 @Test
 public void testGetEventHandlerWithExistentEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(eventHandler);
 assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler("testEventHandler"));
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 Map<String, String> map = Mockito.map(Mockito.map("eventHandler1", "{\"name\":\"eventHandler1\", \"event\":\"event1\"}",
 "eventHandler2", "{\"name\":\"eventHandler2\", \"event\":\"event2\"}"));
 when(jedisProxy.hgetAll(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLERS)))).thenReturn(map);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertTrue(eventHandlers.contains(eventHandler1));
 assertTrue(eventHandlers.contains(eventHandler2));
 }

 @Test
 public void testGetEventHandlersForEventWithExistentEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent("testEvent");
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent("testEvent");
 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("eventHandler3");eventHandler3.setEvent("otherEvent");
 Set<String> names = Mockito.set("eventHandler1", "eventHandler2");
 when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLER_BY_EVENT, "testEvent")))).thenReturn(names);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, eventHandlers.size());
 assertTrue(eventHandlers.contains(eventHandler1));
 assertTrue(eventHandlers.contains(eventHandler2));
 }

 @Test
 public void testGetEventHandlersForEventWithExistentEventAndActiveEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(false);
 Set<String> names = Mockito.set("eventHandler1");
 when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLER_BY_EVENT, "testEvent")))).thenReturn(names);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, eventHandlers.size());
 assertTrue(eventHandlers.contains(eventHandler1));
 }

 @Test
 public void testGetEventHandlersForEventWithNonExistentEvent() {
 Set<String> names = Mockito.emptySet();
 when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.nsKey(EVENT_HANDLER_BY_EVENT, "otherEvent")))).thenReturn(names);
 assertFalse(redisEventHandlerDAO.getEventHandlersForEvent("otherEvent", false).isEmpty());
 }
}