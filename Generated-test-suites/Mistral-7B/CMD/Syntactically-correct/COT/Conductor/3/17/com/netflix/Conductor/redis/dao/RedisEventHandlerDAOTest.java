import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

 @Mock
 private JedisProxy jedisProxy;
 @Mock
 private ObjectMapper objectMapper;
 @Mock
 private ConductorProperties conductorProperties;
 @Mock
 private RedisProperties redisProperties;
 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @After
 public void tearDown() {
 // Clear mocks to avoid memory leaks
 reset(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler("testHandler", "testEvent", "testDescription");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
 verify(jedisProxy, times(1)).sadd(anyString(), eq(eventHandler.getName()));
 verify(objectMapper, times(1)).writeValueAsString(eventHandler);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler existingEventHandler = new EventHandler("existingHandler", "testEvent", "testDescription");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 redisEventHandlerDAO.addEventHandler(new EventHandler("testHandler", "testEvent", "testDescription"));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler("existingHandler", "testEvent", "testDescription");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler updatedEventHandler = new EventHandler("existingHandler", "updatedEvent", "updatedDescription");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq(existingEventHandler.getName()), anyString());
 verify(jedisProxy, times(1)).srem(anyString(), eq(existingEventHandler.getName()));
 verify(jedisProxy, times(1)).hset(anyString(), eq(updatedEventHandler.getName()), anyString());
 verify(objectMapper, times(1)).writeValueAsString(updatedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistingName() {
 redisEventHandlerDAO.updateEventHandler(new EventHandler("nonExistingHandler", "testEvent", "testDescription"));
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler("testHandler", "testEvent", "testDescription");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testHandler");

 verify(jedisProxy, times(1)).hdel(anyString(), eq(eventHandler.getName()));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistingName() {
 redisEventHandlerDAO.removeEventHandler("nonExistingHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler handler1 = new EventHandler("handler1", "event1", "description1");
 EventHandler handler2 = new EventHandler("handler2", "event2", "description2");

 when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("handler1", toJson(handler1), "handler2", toJson(handler2)));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertEquals(handler1, eventHandlers.get(0));
 assertEquals(handler2, eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler handler1 = new EventHandler("handler1", "event1", "description1");
 EventHandler handler2 = new EventHandler("handler2", "event1", "description2");
 EventHandler handler3 = new EventHandler("handler3", "event2", "description3");

 when(jedisProxy.smembers(anyString())).thenReturn(Set.of("handler1", "handler2"));
 when(jedisProxy.hget(anyString(), eq("handler1"))).thenReturn(toJson(handler1));
 when(jedisProxy.hget(anyString(), eq("handler2"))).thenReturn(toJson(handler2));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
 assertEquals(2, eventHandlers.size());
 assertEquals(handler1, eventHandlers.get(0));
 assertEquals(handler2, eventHandlers.get(1));

 eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(2, eventHandlers.size());
 assertEquals(handler1, eventHandlers.get(0));
 assertEquals(handler2, eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithEmptyEventHandlersList() {
 String event = "event";

 when(jedisProxy.smembers(anyString())).thenReturn(Set.of());

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
 assertNotNull(eventHandlers);
 assertEquals(0, eventHandlers.size());
 }

 private String toJson(EventHandler eventHandler) {
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 when(objectMapper.writeValueAsString(any())).thenReturn(eventHandler.toString());
 return objectMapper.writeValueAsString(eventHandler);
 }
}