import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.JsonProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")

public class RedisEventHandlerDAOTest {

 private static final String EVENT_NAME = "testEvent";
 private static final String EVENT_HANDLER_NAME = "testEventHandler";

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Mock
 private JedisProxy jedisProxy;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties redisProperties;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private JsonProvider jsonProvider;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(conductorProperties.getRedisNamespace()).thenReturn("test_namespace");
 when(redisProperties.getNamespace()).thenReturn("test_namespace");
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLERS_BY_EVENT))).thenReturn("test_set");
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME)))).thenReturn(
 Lists.newArrayList("testEventHandler"));
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_NAME);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 assertEquals(EVENT_HANDLER_NAME, jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_NAME);
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_NAME);
 existingEventHandler.setEvent(EVENT_NAME);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_NAME);
 existingEventHandler.setEvent(EVENT_NAME);
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(EVENT_HANDLER_NAME);
 updatedEventHandler.setEvent("newEvent");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 assertNull(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)));
 assertEquals("newEvent", jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)));
 }

 @Test
 public void testUpdateEventHandlerNotFound() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(EVENT_HANDLER_NAME + "_not_found");
 updatedEventHandler.setEvent("newEvent");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME);
 eventHandler.setEvent(EVENT_NAME);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

 assertNull(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(EVENT_HANDLER_NAME)));
 }

 @Test
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME + "_not_found");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT_NAME);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT_NAME);
 eventHandler2.setActive(true);
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Maps.newHashMap(Maps.newHashMap().put(EVENT_HANDLER_NAME, toJson(eventHandler1))));
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT_NAME);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT_NAME);
 eventHandler2.setActive(false);
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Maps.newHashMap(Maps.newHashMap().put(EVENT_HANDLER_NAME, toJson(eventHandler1))));
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME)))).thenReturn(Maps.newArrayList("eventHandler1", "eventHandler2"));
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT_NAME);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT_NAME);
 eventHandler2.setActive(false);
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Maps.newHashMap(Maps.newHashMap().put(EVENT_HANDLER_NAME, toJson(eventHandler1))));
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_NAME)))).thenReturn(Maps.newArrayList("eventHandler1"));
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);
 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlerNotFound() {
 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("EventHandlerName_not_found");
 assertNull(eventHandler);
 }
}