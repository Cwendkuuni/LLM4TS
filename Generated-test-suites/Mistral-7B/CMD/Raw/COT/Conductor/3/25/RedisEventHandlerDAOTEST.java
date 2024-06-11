import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseRedisTest;
import com.netflix.conductor.test.HttpTestHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RedisEventHandlerDAOTest {

 @Autowired
 private RedisProperties redisProperties;
 @Autowired
 private JedisProxy jedisProxy;
 @Autowired
 private ObjectMapper objectMapper;
 @Autowired
 private ConductorProperties conductorProperties;
 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 private static final String TEST_NAME = "testEventHandler";
 private static final String TEST_EVENT = "testEvent";
 private static final String TEST_EVENT2 = "testEvent2";
 private static final String TEST_EVENT_NAMES_SET = "testEventNamesSet";

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(TEST_NAME);
 eventHandler.setEvent(TEST_EVENT);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler dbEventHandler = redisEventHandlerDAO.getEventHandler(TEST_NAME);
 assertEquals(TEST_NAME, dbEventHandler.getName());
 assertEquals(TEST_EVENT, dbEventHandler.getEvent());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(TEST_NAME);
 existingEventHandler.setEvent(TEST_EVENT);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName(TEST_NAME);
 newEventHandler.setEvent(TEST_EVENT2);

 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
 }

 @Test(expected = ConflictException.class)
 public void testUpdateEventHandlerWithNonExistingName() {
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("nonExistingName");
 newEventHandler.setEvent(TEST_EVENT);

 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.updateEventHandler(newEventHandler));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(TEST_NAME);
 existingEventHandler.setEvent(TEST_EVENT);
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(TEST_NAME);
 updatedEventHandler.setEvent(TEST_EVENT2);
 updatedEventHandler.setActive(false);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 EventHandler dbEventHandler = redisEventHandlerDAO.getEventHandler(TEST_NAME);
 assertFalse(dbEventHandler.isActive());
 assertEquals(TEST_EVENT2, dbEventHandler.getEvent());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistingName() {
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(TEST_NAME));
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent("event1");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent("event2");
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> dbEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, dbEventHandlers.size());
 }

 @Test
 public void testGetEventHandlerForEvent() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(TEST_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 List<EventHandler> dbEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 assertEquals(1, dbEventHandlers.size());
 assertEquals(TEST_NAME, dbEventHandlers.get(0).getName());
 }

 @Test
 public void testGetEventHandlerForEventWithActiveFilter() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> dbEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, true);
 assertEquals(1, dbEventHandlers.size());
 assertEquals(TEST_NAME, dbEventHandlers.get(0).getName());
 }

 @Test
 public void testGetEventHandlerForNonExistingEvent() {
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT2, false));
 }

 private void setupTestData() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("eventHandler1");
 eventHandler1.setEvent("event1");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("eventHandler2");
 eventHandler2.setEvent("event1");
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("eventHandler3");
 eventHandler3.setEvent("event2");
 eventHandler3.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 EventHandler eventHandler4 = new EventHandler();
 eventHandler4.setName("eventHandler4");
 eventHandler4.setEvent("event2");
 eventHandler4.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler4);
 }

 @Test
 public void testIndexAndRemoveIndex() {
 setupTestData();
 redisEventHandlerDAO.index(eventHandler1);
 String key = redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, TEST_EVENT);
 Set<String> names = jedisProxy.smembers(key);
 assertEquals(2, names.size());
 assertTrue(names.contains(TEST_NAME));

 redisEventHandlerDAO.removeEventHandler(TEST_NAME);
 names = jedisProxy.smembers(key);
 assertEquals(1, names.size());
 assertFalse(names.contains(TEST_NAME));
 }

 @Test
 public void testGetEventHandlerForEventWithNonExistingEvent() {
 setupTestData();
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlerForEvent(TEST_EVENT2, false));
 }
}