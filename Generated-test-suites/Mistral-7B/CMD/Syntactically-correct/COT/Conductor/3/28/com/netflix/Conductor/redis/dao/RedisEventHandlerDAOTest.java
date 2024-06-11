import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @InjectMocks
 private RedisEventHandlerDAO testObject;

 @Before
 public void initMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public String hset(String key, String field, String value) {
 return "success";
 }

 @Override
 public String hget(String key, String field) {
 return null;
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.addEventHandler(eventHandler);
 assertEquals("success", ReflectionTestUtils.getFieldValue(testObject, "lastRedisRequest"));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public String hset(String key, String field, String value) {
 throw new RuntimeException("Failed to set event handler in Redis");
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.addEventHandler(eventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setActive(true);
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("testUpdatedEvent");
 updatedEventHandler.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public String hset(String key, String field, String value) {
 return "success";
 }

 @Override
 public String hget(String key, String field) {
 return "{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}";
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.updateEventHandler(updatedEventHandler);
 assertEquals("success", ReflectionTestUtils.getFieldValue(testObject, "lastRedisRequest"));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentName() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandlerUpdated");
 updatedEventHandler.setEvent("testEvent");
 updatedEventHandler.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public String hget(String key, String field) {
 return null;
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public String hdel(String key, String field) {
 return "success";
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.removeEventHandler("testEventHandler");
 assertEquals("success", ReflectionTestUtils.getFieldValue(testObject, "lastRedisRequest"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentName() {
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public String hdel(String key, String field) {
 return "success";
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.removeEventHandler("nonExistentName");
 }

 @Test
 public void testGetAllEventHandlers() {
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public Map<String, String> hgetAll(String key) {
 return Lists.newHashMap();
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 List<EventHandler> eventHandlers = testObject.getAllEventHandlers();
 assertEquals(0, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithActiveHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public Set<String> smembers(String key) {
 return Lists.newArrayList("testEventHandler1", "testEventHandler2").asSet();
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 List<EventHandler> eventHandlers = testObject.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithInactiveHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(false);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(true);
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public Set<String> smembers(String key) {
 return Lists.newArrayList("testEventHandler1", "testEventHandler2").asSet();
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 List<EventHandler> eventHandlers = testObject.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, eventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlersForEventWithNonExistentEvent() {
 ReflectionTestUtils.setFieldValue(testObject, "jedisProxy", new JedisProxy() {
 @Override
 public Set<String> smembers(String key) {
 return Lists.newArrayList("testEventHandler1", "testEventHandler2").asSet();
 }
 });
 ReflectionTestUtils.setFieldValue(testObject, "objectMapper", new ObjectMapper());
 ReflectionTestUtils.setFieldValue(testObject, "conductorProperties", new ConductorProperties());
 ReflectionTestUtils.setFieldValue(testObject, "redisProperties", new RedisProperties());

 testObject.getEventHandlersForEvent("nonExistentEvent", false);
 }
}