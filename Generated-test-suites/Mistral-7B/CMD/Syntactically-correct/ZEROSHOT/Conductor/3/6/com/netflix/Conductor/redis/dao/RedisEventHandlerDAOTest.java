import org.junit.After;
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
import com.netflix.conductor.redis.mapper.JsonMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ suppressWarnings("ResultOfMethodCallIgnored") // for recordRedisDaoRequests
public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;
 private JsonMapper jsonMapper;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 jedisProxy = mock(JedisProxy.class);
 objectMapper = mock(ObjectMapper.class);
 conductorProperties = mock(ConductorProperties.class);
 redisProperties = mock(RedisProperties.class);
 jsonMapper = mock(JsonMapper.class);

 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jsonMapper", jsonMapper);
 }

 @After
 public void teardown() {
 // clear mock data
 clearRedisData(jedisProxy);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 assertEquals(1, getEventHandlersCount());

 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertNotNull(retrievedEventHandler);
 assertEquals("testEventHandler", retrievedEventHandler.getName());
 assertEquals("testEvent", retrievedEventHandler.getEvent());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflictingName() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetAllEventHandlers() {
 redisEventHandlerDAO.addEventHandler(new EventHandler("h1", "e1"));
 redisEventHandlerDAO.addEventHandler(new EventHandler("h2", "e2"));

 List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, allEventHandlers.size());
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler updatedEventHandler = new EventHandler("testEventHandler", "testEventUpdated");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertNotNull(retrievedEventHandler);
 assertEquals("testEventHandler", retrievedEventHandler.getName());
 assertEquals("testEventUpdated", retrievedEventHandler.getEvent());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentEventHandler() {
 EventHandler updatedEventHandler = new EventHandler("testEventHandlerUpdated", "testEvent");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 assertEquals(0, getEventHandlersCount());
 assertNull(redisEventHandlerDAO.getEventHandler("testEventHandler"));
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistentEventHandler() {
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 assertNull(redisEventHandlerDAO.getEventHandler("testEventHandler"));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveHandlersOnly() {
 EventHandler h1 = new EventHandler("h1", "e1", true);
 EventHandler h2 = new EventHandler("h2", "e1", false);
 EventHandler h3 = new EventHandler("h3", "e1", true);

 redisEventHandlerDAO.addEventHandler(h1);
 redisEventHandlerDAO.addEventHandler(h2);
 redisEventHandlerDAO.addEventHandler(h3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("e1", true);
 assertEquals(1, eventHandlers.size());
 assertEquals(h3.getName(), eventHandlers.get(0).getName());
 }

 @Test
 public void testGetEventHandlersForEventWithAllHandlers() {
 EventHandler h1 = new EventHandler("h1", "e1", true);
 EventHandler h2 = new EventHandler("h2", "e1", false);
 EventHandler h3 = new EventHandler("h3", "e1", true);

 redisEventHandlerDAO.addEventHandler(h1);
 redisEventHandlerDAO.addEventHandler(h2);
 redisEventHandlerDAO.addEventHandler(h3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("e1", false);
 assertEquals(3, eventHandlers.size());
 }

 private int getEventHandlersCount() {
 return (int) ReflectionTestUtils.getFieldValue(redisEventHandlerDAO, "eventHandlerCount");
 }

 private void clearRedisData(JedisProxy jedisProxy) {
 List<String> keys = Lists.newArrayList(nsKey(EVENT_HANDLERS), nsKey(EVENT_HANDLERS_BY_EVENT));
 for (String key : keys) {
 jedisProxy.del(key);
 }
 }

 private String nsKey(String... parts) {
 return String.join(":", parts);
 }
}