import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.daos.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisPropertiesTest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
 private static final String EVENT_HANDLER_NAME = "testEventHandler";
 private static final String EVENT_HANDLER_EVENT = "testEvent";
 private static final String EVENT_HANDLER_JSON = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}";

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
 when(conductorProperties.getRedisProperties()).thenReturn(redisProperties);
 when(redisProperties.getNamespace()).thenReturn("test_namespace");
 when(objectMapper.readValue(anyString(), eq(EventHandler.class))).then(invocation -> {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_HANDLER_EVENT);
 eventHandler.setActive(true);
 return eventHandler;
 });
 }

 @After
 public void tearDown() {
 reset(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_HANDLER_EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), eq(EVENT_HANDLER_JSON));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_HANDLER_EVENT);
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_NAME);
 existingEventHandler.setEvent(EVENT_HANDLER_EVENT);
 doReturn(existingEventHandler).when(redisEventHandlerDAO).getEventHandler(EVENT_HANDLER_NAME);

 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_NAME);
 existingEventHandler.setEvent(EVENT_HANDLER_EVENT);
 existingEventHandler.setActive(true);
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName(EVENT_HANDLER_NAME);
 newEventHandler.setEvent("newEvent");
 newEventHandler.setActive(false);
 redisEventHandlerDAO.updateEventHandler(newEventHandler);

 verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME), eq(toJson(newEventHandler)));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("nonExistentEventHandler");
 newEventHandler.setEvent("nonExistentEvent");
 redisEventHandlerDAO.updateEventHandler(newEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

 verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent("event1");
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent("event2");
 eventHandler2.setActive(false);

 Map<String, String> all = Maps.newHashMap();
 all.put("eventHandler1", toJson(eventHandler1));
 all.put("eventHandler2", toJson(eventHandler2));
 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT_HANDLER_EVENT);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT_HANDLER_EVENT);
 eventHandler2.setActive(false);
 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("eventHandler3");
 eventHandler3.setEvent("otherEvent");
 eventHandler3.setActive(true);

 Set<String> names = Maps.newHashMap().keySet();
 names.add("eventHandler1");
 names.add("eventHandler2");
 when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_EVENT))).thenReturn(names);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_HANDLER_EVENT, false);
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT_HANDLER_EVENT);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT_HANDLER_EVENT);
 eventHandler2.setActive(false);
 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("eventHandler3");
 eventHandler3.setEvent("otherEvent");
 eventHandler3.setActive(true);

 Set<String> names = Maps.newHashMap().keySet();
 names.add("eventHandler1");
 when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_HANDLER_EVENT))).thenReturn(names);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_HANDLER_EVENT, true);
 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_HANDLER_EVENT);
 eventHandler.setActive(true);
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME)).thenReturn(EVENT_HANDLER_JSON);

 EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
 assertEquals(eventHandler, returnedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "nonExistentEventHandler")).thenReturn(null);

 redisEventHandlerDAO.getEventHandler("nonExistentEventHandler");
 }
}