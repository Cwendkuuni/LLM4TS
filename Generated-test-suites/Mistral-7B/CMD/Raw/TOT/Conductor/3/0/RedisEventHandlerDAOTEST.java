import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.RedisProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.RedisTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import(RedisEventHandlerDAO.class)
public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Bean
 public JedisProxy jedisProxy() {
 return Mockito.mock(JedisProxy.class);
 }

 @Bean
 public ObjectMapper objectMapper() {
 return Mockito.mock(ObjectMapper.class);
 }

 @Bean
 public ConductorProperties conductorProperties() {
 return Mockito.mock(ConductorProperties.class);
 }

 @Bean
 public RedisProperties redisProperties() {
 return Mockito.mock(RedisProperties.class);
 }

 @Before
 public void setup() {
 redisEventHandlerDAO = new RedisEventHandlerDAO(
 jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 String json = jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, eventHandler.getName());
 EventHandler retrievedEventHandler = objectMapper.readValue(json, EventHandler.class);
 assertEquals(eventHandler.getName(), retrievedEventHandler.getName());
 assertEquals(eventHandler.getEvent(), retrievedEventHandler.getEvent());
 assertEquals(eventHandler.isActive(), retrievedEventHandler.isActive());
 }

 @Test
 public void testAddEventHandlerWithConflict() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent");
 newEventHandler.setActive(true);

 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedEvent");
 updatedEventHandler.setActive(false);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 String json = jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler");
 EventHandler retrievedEventHandler = objectMapper.readValue(json, EventHandler.class);
 assertEquals(updatedEventHandler.getName(), retrievedEventHandler.getName());
 assertEquals(updatedEventHandler.getEvent(), retrievedEventHandler.getEvent());
 assertFalse(retrievedEventHandler.isActive());
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
 assertNull(jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, eventHandler.getName()));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler("nonexistentEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent1");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent2");
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlerForEvent() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlerForEventActiveOnly() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler inactiveEventHandler = new EventHandler();
 inactiveEventHandler.setName("testEventHandlerInactive");
 inactiveEventHandler.setEvent("testEvent");
 inactiveEventHandler.setActive(false);
 redisEventHandlerDAO.addEventHandler(inactiveEventHandler);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, eventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerForEventNotFound() {
 redisEventHandlerDAO.getEventHandlersForEvent("nonexistentEvent", false);
 }

 @Test
 public void testRedisSetupAndTeardown() {
 JedisProxy jedis = Mockito.mock(JedisProxy.class);
 when(jedisProxy.exists(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS)))
 .thenReturn(true);
 when(jedisProxy.hlen(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS)))
 .thenReturn(3);
 when(jedisProxy.hget(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS), "testEventHandler1"))
 .thenReturn("{}");
 when(jedisProxy.hget(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS), "testEventHandler2"))
 .thenReturn("{}");
 when(jedisProxy.hget(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS), "testEventHandler3"))
 .thenReturn("{}");
 when(jedisProxy.hlen(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))
 .thenReturn(2);
 when(jedisProxy.smembers(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")))
 .thenReturn(Lists.newArrayList("testEventHandler1", "testEventHandler2"));

 redisEventHandlerDAO = new RedisEventHandlerDAO(jedis, objectMapper, conductorProperties, redisProperties);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(3, eventHandlers.size());

 redisEventHandlerDAO.removeEventHandler("testEventHandler1");
 String json = jedisProxy.hget(RedisTestUtils.NS_KEY(RedisEventHandlerDAO.EVENT_HANDLERS), "testEventHandler1");
 assertNull(json);
 eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }
}