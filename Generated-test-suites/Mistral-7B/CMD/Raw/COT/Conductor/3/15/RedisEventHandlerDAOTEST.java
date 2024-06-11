import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;
import com.netflix.conductor.utils.jackson.JacksonUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 jedisProxy = mock(JedisProxy.class);
 objectMapper = mock(ObjectMapper.class);
 conductorProperties = mock(ConductorProperties.class);
 redisProperties = mock(RedisProperties.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "namespace", "test:");
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = createEventHandler("testEvent", "testHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 assertEquals(eventHandler.getName(), jedisProxy.hget(anyString(), eventHandler.getName()));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 EventHandler eventHandler = createEventHandler("testEvent", "testHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = createEventHandler("testEvent", "testHandler");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 EventHandler updatedEventHandler = createEventHandler("testEvent", "updatedHandler");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 assertNull(jedisProxy.hget(anyString(), existingEventHandler.getName()));
 assertEquals(updatedEventHandler.getName(), jedisProxy.hget(anyString(), updatedEventHandler.getName()));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler updatedEventHandler = createEventHandler("testEvent", "updatedHandler");
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = createEventHandler("testEvent", "testHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
 assertNull(jedisProxy.hget(anyString(), eventHandler.getName()));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = createEventHandler("testEvent1", "testHandler1");
 EventHandler eventHandler2 = createEventHandler("testEvent1", "testHandler2");
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = createEventHandler("testEvent", "testHandler1");
 EventHandler eventHandler2 = createEventHandler("testEvent", "testHandler2");
 EventHandler eventHandler3 = createEventHandler("otherEvent", "testHandler3");
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 redisEventHandlerDAO.addEventHandler(eventHandler3);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 EventHandler eventHandler1 = createEventHandler("testEvent", "testHandler1");
 EventHandler eventHandler2 = createEventHandler("testEvent", "testHandler2");
 EventHandler eventHandler3 = createEventHandler("testEvent", "testHandler3");
 EventHandler eventHandler4 = createEventHandler("testEvent", "testHandler4");
 EventHandler eventHandler5 = createEventHandler("otherEvent", "testHandler5");
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 redisEventHandlerDAO.addEventHandler(eventHandler3);
 redisEventHandlerDAO.addEventHandler(eventHandler4);
 redisEventHandlerDAO.addEventHandler(eventHandler5);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, eventHandlers.size());
 }

 private EventHandler createEventHandler(String event, String handler) {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(event + "_" + handler);
 eventHandler.setEvent(event);
 eventHandler.setHandler(handler);
 eventHandler.setActive(true);
 return eventHandler;
 }
}