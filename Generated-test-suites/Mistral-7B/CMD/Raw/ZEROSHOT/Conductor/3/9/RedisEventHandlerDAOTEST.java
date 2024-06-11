import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;

@SpringBootTest
public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLER_1_NAME = "handler1";
 private static final String EVENT_HANDLER_2_NAME = "handler2";
 private static final String EVENT_HANDLER_3_NAME = "handler3";
 private static final String TEST_EVENT = "testEvent";

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private JedisProxy jedisProxy;

 @Autowired
 private ConductorProperties conductorProperties;

 @Autowired
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
 }

 @After
 public void cleanup() {
 jedisProxy.flushDB();
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_1_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 Jedis jedis = jedisProxy.getJedis();
 Map<String, String> eventHandlers = jedis.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
 assertTrue(eventHandlers.size() > 0);
 assertEquals(EVENT_HANDLER_1_NAME, eventHandlers.keySet().iterator().next());
 assertNotNull(eventHandlers.get(EVENT_HANDLER_1_NAME));

 JsonNode jsonNode = objectMapper.readTree(eventHandlers.get(EVENT_HANDLER_1_NAME));
 assertEquals(eventHandler.getName(), jsonNode.get("name").asText());
 assertEquals(eventHandler.getEvent(), jsonNode.get("event").asText());
 assertEquals(eventHandler.isActive(), jsonNode.get("active").asBoolean());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_1_NAME);
 existingEventHandler.setEvent(TEST_EVENT);
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName(EVENT_HANDLER_1_NAME);
 newEventHandler.setEvent(TEST_EVENT);
 newEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(newEventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_1_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(EVENT_HANDLER_1_NAME);
 updatedEventHandler.setEvent("testEventUpdated");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 Map<String, String> eventHandlers = jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
 assertEquals(1, eventHandlers.size());
 JsonNode jsonNode = objectMapper.readTree(eventHandlers.get(EVENT_HANDLER_1_NAME));
 assertEquals("testEventUpdated", jsonNode.get("event").asText());
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_1_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_1_NAME);

 Map<String, String> eventHandlers = jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
 assertFalse(eventHandlers.containsKey(EVENT_HANDLER_1_NAME));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentName() {
 redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_1_NAME);
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(EVENT_HANDLER_1_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(EVENT_HANDLER_2_NAME);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName(EVENT_HANDLER_3_NAME);
 eventHandler3.setEvent("testEvent3");
 eventHandler3.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(3, eventHandlers.size());
 assertNotNull(eventHandlers.get(0));
 assertNotNull(eventHandlers.get(1));
 assertNotNull(eventHandlers.get(2));
 }

 @Test
 public void testGetEventHandlersForEventWithSingleEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_1_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 assertEquals(1, eventHandlers.size());
 assertNotNull(eventHandlers.get(0));
 }

 @Test
 public void testGetEventHandlersForEventWithMultipleActiveHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(EVENT_HANDLER_1_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(EVENT_HANDLER_2_NAME);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 assertEquals(2, eventHandlers.size());
 assertNotNull(eventHandlers.get(0));
 assertNotNull(eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithNoActiveHandlers() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_1_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 assertEquals(0, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithNoHandlers() {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 assertEquals(0, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithActiveOnlyTrue() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(EVENT_HANDLER_1_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(EVENT_HANDLER_2_NAME);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, true);
 assertEquals(1, eventHandlers.size());
 assertNotNull(eventHandlers.get(0));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveOnlyFalse() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(EVENT_HANDLER_1_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(EVENT_HANDLER_2_NAME);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 assertEquals(2, eventHandlers.size());
 assertNotNull(eventHandlers.get(0));
 assertNotNull(eventHandlers.get(1));
 }
}