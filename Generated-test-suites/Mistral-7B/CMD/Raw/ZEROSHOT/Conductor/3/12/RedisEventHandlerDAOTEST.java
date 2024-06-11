import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.BaseDynoDAO;
import com.netflix.conductor.redis.util.ObjectMapperFactory;
import com.netflix.conductor.test.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

 private static final String NAMESPACE = "test";
 private static final String EVENT = "testEvent";
 private static final String NAME = "testEventHandler";
 private static final String JSON = "{\"name\": \"testEventHandler\", \"event\": \"testEvent\", \"active\": true}";

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

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(conductorProperties.getNamespace()).thenReturn(NAMESPACE);
 when(redisProperties.getNamespacePrefix()).thenReturn(NAMESPACE);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
 }

 @After
 public void teardown() {
 TestUtils.cleanUpRedis();
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 assertEquals(NAME, redisEventHandlerDAO.getEventHandler(NAME).getName());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflict() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(NAME);
 existingEventHandler.setEvent(EVENT);
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName(NAME);
 newEventHandler.setEvent(EVENT);
 newEventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(newEventHandler);
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT);
 eventHandler2.setActive(false);

 when(jedisProxy.hgetAll(anyString())).thenReturn(TestUtils.createMap(NAMESPACE + EVENT_HANDLERS, Lists.newArrayList(
                 NAME + "_1", NAME + "_2"
 )));
 when(objectMapper.readValue(anyString(), EventHandler.class)).thenAnswer(invocation -> {
     String name = invocation.getArgument(0).substring(invocation.getArgument(0).lastIndexOf("/") + 1);
     if (name.equals(NAME + "_1")) {
         return eventHandler1;
     } else {
         return eventHandler2;
     }
 });

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 assertEquals(eventHandler2, eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandler() {
 EventHandler expectedEventHandler = new EventHandler();
 expectedEventHandler.setName(NAME);
 expectedEventHandler.setEvent(EVENT);
 expectedEventHandler.setActive(true);
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(JSON);
 when(objectMapper.readValue(anyString(), EventHandler.class)).thenReturn(expectedEventHandler);

 EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler(NAME);
 assertNotNull(actualEventHandler);
 assertEquals(expectedEventHandler, actualEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
 redisEventHandlerDAO.getEventHandler(NAME);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(NAME);
 existingEventHandler.setEvent(EVENT);
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(NAME);
 updatedEventHandler.setEvent("testEventUpdated");
 updatedEventHandler.setActive(true);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 assertEquals("testEventUpdated", redisEventHandlerDAO.getEventHandler(NAME).getEvent());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("updatedEventHandler");
 updatedEventHandler.setEvent("testEventUpdated");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(NAME);
 eventHandler.setEvent(EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.removeEventHandler(NAME);
 assertNull(redisEventHandlerDAO.getEventHandler(NAME));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler(NAME);
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT);
 eventHandler2.setActive(false);

 when(jedisProxy.smembers(anyString())).thenReturn(TestUtils.createSet(NAME + "_1", NAME + "_2"));
 when(redisEventHandlerDAO.getEventHandler(NAME + "_1")).thenReturn(eventHandler1);
 when(redisEventHandlerDAO.getEventHandler(NAME + "_2")).thenReturn(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
 assertEquals(2, eventHandlers.size());
 assertTrue(eventHandlers.contains(eventHandler1));
 assertTrue(eventHandlers.contains(eventHandler2));
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(EVENT);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(EVENT);
 eventHandler2.setActive(false);

 when(jedisProxy.smembers(anyString())).thenReturn(TestUtils.createSet(NAME + "_1", NAME + "_2"));
 when(redisEventHandlerDAO.getEventHandler(NAME + "_1")).thenReturn(eventHandler1);
 when(redisEventHandlerDAO.getEventHandler(NAME + "_2")).thenReturn(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
 assertEquals(1, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 }
}