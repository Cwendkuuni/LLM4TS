import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;
 @Mock
 private JedisProxy jedisProxy;
 @Mock
 private ConductorProperties conductorProperties;
 @Mock
 private RedisProperties properties;
 @Mock
 private ObjectMapper objectMapper;

 @Test
 public void testAddEventHandler() {
 // Given
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
 .thenReturn(true);
 // When
 redisEventHandlerDAO.addEventHandler(eventHandler);
 // Then
 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.eq("testEventHandler"),
 Mockito.anyString());
 }

 @Test
 public void testAddEventHandler_conflict() {
 // Given
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent2");
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler")))
 .thenReturn(existingEventHandler.toJson());
 Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
 .thenThrow(new ConflictException("EventHandler with name testEventHandler already exists!"));
 // When & Then
 Assertions.assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
 }

 @Test
 public void testUpdateEventHandler() {
 // Given
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent2");
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler")))
 .thenReturn(existingEventHandler.toJson());
 Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
 .thenReturn(true);
 // When
 redisEventHandlerDAO.updateEventHandler(newEventHandler);
 // Then
 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.eq("testEventHandler"),
 Mockito.anyString());
 }

 @Test
 public void testUpdateEventHandler_notFound() {
 // Given
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler2");
 newEventHandler.setEvent("testEvent2");
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler")))
 .thenReturn(null);
 // When & Then
 Assertions.assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(newEventHandler));
 }

 @Test
 public void testRemoveEventHandler() {
 // Given
 String name = "testEventHandler";
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hdel(Mockito.anyString(), Mockito.eq(name)))
 .thenReturn(1);
 // When
 redisEventHandlerDAO.removeEventHandler(name);
 // Then
 Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.eq(name));
 }

 @Test
 public void testRemoveEventHandler_notFound() {
 // Given
 String name = "testEventHandler2";
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hdel(Mockito.anyString(), Mockito.eq(name)))
 .thenReturn(0);
 // When & Then
 Assertions.assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(name));
 }

 @Test
 public void testGetAllEventHandlers() {
 // Given
 EventHandler handler1 = new EventHandler();
 handler1.setName("testEventHandler1");
 handler1.setEvent("testEvent1");
 EventHandler handler2 = new EventHandler();
 handler2.setName("testEventHandler2");
 handler2.setEvent("testEvent2");
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.hgetAll(Mockito.anyString()))
 .thenReturn(Mockito.mapAsMap(Mockito.argThat(entry -> entry.getKey().equals("testEventHandler1")))
 .entrySet().stream().map(entry -> Map.entry(entry.getValue(), handler1))
 .collect(Collectors.toList())
 .toArray(new Map.Entry[0][]));
 // When
 List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 // Then
 Assertions.assertEquals(2, allEventHandlers.size());
 Assertions.assertEquals(handler1, allEventHandlers.get(0));
 Assertions.assertEquals(handler2, allEventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEvent_eventHandlersPresent() {
 // Given
 EventHandler handler1 = new EventHandler();
 handler1.setName("testEventHandler1");
 handler1.setEvent("testEvent");
 handler1.setActive(true);
 EventHandler handler2 = new EventHandler();
 handler2.setName("testEventHandler2");
 handler2.setEvent("testEvent");
 handler2.setActive(false);
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 Mockito.when(jedisProxy.smembers(Mockito.anyString()))
 .thenReturn(Arrays.asList("testEventHandler1", "testEventHandler2"));
 // When
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 // Then
 Assertions.assertEquals(2, eventHandlers.size());
 Assertions.assertEquals(handler1, eventHandlers.get(0));
 Assertions.assertEquals(handler2, eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEvent_eventHandlersNotPresent() {
 // Given
 // Setup mocks
 MockitoAnnotations.initMocks(this);
 // When & Then
 Assertions.assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false));
 }
}