import org.junit.Test;
import static org.junit.Assert.*;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.dao.BaseDynoDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.google.common.collect.Lists;
import com.google.common.collect.maps.MapMaker;

public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(eventHandler.getName(), retrievedEventHandler.getName());
 assertEquals(eventHandler.getEvent(), retrievedEventHandler.getEvent());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("existingEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetAllEventHandlers() {
 redisEventHandlerDAO.addEventHandler(new EventHandler("eh1", "event1"));
 redisEventHandlerDAO.addEventHandler(new EventHandler("eh2", "event2"));
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(eventHandler.getName(), retrievedEventHandler.getName());
 assertEquals(eventHandler.getEvent(), retrievedEventHandler.getEvent());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedTestEvent");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(updatedEventHandler.getName(), retrievedEventHandler.getName());
 assertEquals(updatedEventHandler.getEvent(), retrievedEventHandler.getEvent());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentEventHandler() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedTestEvent");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertNull(retrievedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentEventHandler() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eh1");
 eventHandler1.setEvent("event1");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eh2");
 eventHandler2.setEvent("event1");
 eventHandler2.setActive(false);redisEventHandlerDAO.addEventHandler(eventHandler2);
 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("eh3");
 eventHandler3.setEvent("event2");
 eventHandler3.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler3);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
 assertEquals(2, eventHandlers.size());
 List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(1, activeEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithEmptyEvent() {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("", false);
 assertEquals(0, eventHandlers.size());
 List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("", true);
 assertEquals(0, activeEventHandlers.size());
 }

 @Test
 public void testIndexAndRemoveIndex() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.index(eventHandler);
 String eventKey = "EVENT_HANDLERS_BY_EVENT:testEvent";
 Set<String> names = jedisProxy.smembers(eventKey);
 assertEquals(1, names.size());
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 names = jedisProxy.smembers(eventKey);
 assertEquals(0, names.size());
 }
}