import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.testutils.TestRedisUtils;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;
 private RedisEventHandlerDAO redisEventHandlerDAO;
 private TestRedisUtils testRedisUtils;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 jedisProxy = mock(JedisProxy.class);
 objectMapper = mock(ObjectMapper.class);
 conductorProperties = mock(ConductorProperties.class);
 redisProperties = mock(RedisProperties.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 testRedisUtils = new TestRedisUtils();
 testRedisUtils.init(jedisProxy, redisProperties.getNamespace());
 }

 @After
 public void tearDown() {
 testRedisUtils.shutdown();
 }

 @Test
 public void testAddEventHandler() throws Exception {
 EventHandler eventHandler = createEventHandler("testAddEventHandler");

 redisEventHandlerDAO.addEventHandler(eventHandler);

 Map<String, String> all = jedisProxy.hgetAll(redisEventHandlerDAO.getNamespace() + ":EVENT_HANDLERS");
 assertEquals(1, all.size());
 Map<String, String> eventHandlerMap = Maps.newHashMap(all);
 String json = eventHandlerMap.get(eventHandler.getName());
 EventHandler loadedEventHandler = objectMapper.readValue(json, EventHandler.class);
 assertEquals(eventHandler.getName(), loadedEventHandler.getName());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() throws Exception {
 EventHandler eventHandler = createEventHandler("testAddEventHandlerConflict");
 EventHandler existingEventHandler = createEventHandler("testAddEventHandlerConflict");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetEventHandler() throws Exception {
 String name = "testGetEventHandler";
 EventHandler eventHandler = createEventHandler(name);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler loadedEventHandler = redisEventHandlerDAO.getEventHandler(name);
 assertEquals(name, loadedEventHandler.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() throws Exception {
 String name = "testGetEventHandlerNotFound";
 redisEventHandlerDAO.getEventHandler(name);
 }

 @Test
 public void testUpdateEventHandler() throws Exception {
 EventHandler eventHandler = createEventHandler("testUpdateEventHandler");
 EventHandler updatedEventHandler = createEventHandler("testUpdateEventHandlerUpdated");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 Map<String, String> all = jedisProxy.hgetAll(redisEventHandlerDAO.getNamespace() + ":EVENT_HANDLERS");
 assertEquals(1, all.size());
 Map<String, String> eventHandlerMap = Maps.newHashMap(all);
 String json = eventHandlerMap.get(updatedEventHandler.getName());
 EventHandler loadedEventHandler = objectMapper.readValue(json, EventHandler.class);
 assertEquals(updatedEventHandler.getName(), loadedEventHandler.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() throws Exception {
 EventHandler updatedEventHandler = createEventHandler("testUpdateEventHandlerNotFound");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() throws Exception {
 EventHandler eventHandler = createEventHandler("testRemoveEventHandler");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

 assertEquals(0, jedisProxy.hlen(redisEventHandlerDAO.getNamespace() + ":EVENT_HANDLERS"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() throws Exception {
 redisEventHandlerDAO.removeEventHandler("testRemoveEventHandlerNotFound");
 }

 @Test
 public void testGetAllEventHandlers() throws Exception {
 EventHandler eventHandler1 = createEventHandler("testGetAllEventHandlers1");
 EventHandler eventHandler2 = createEventHandler("testGetAllEventHandlers2");
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, allEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() throws Exception {
 String event = "testGetEventHandlersForEvent";
 EventHandler eventHandler1 = createEventHandler("testGetEventHandlersForEvent1");
 EventHandler eventHandler2 = createEventHandler("testGetEventHandlersForEvent2");
 EventHandler eventHandler3 = createEventHandler("testGetEventHandlersForEvent3");
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
 assertEquals(3, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() throws Exception {
 String event = "testGetEventHandlersForEventActiveOnly";
 EventHandler eventHandler1 = createEventHandler("testGetEventHandlersForEventActiveOnly1");
 EventHandler eventHandler2 = createEventHandler("testGetEventHandlersForEventActiveOnly2");
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
 assertEquals(1, eventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlersForEventNotFound() throws Exception {
 String event = "testGetEventHandlersForEventNotFound";
 redisEventHandlerDAO.getEventHandlersForEvent(event, false);
 }

 @Test
 private void testIndex() throws Exception {
 EventHandler eventHandler = createEventHandler("testIndex");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 String key = redisEventHandlerDAO.getNamespace() + ":EVENT_HANDLERS_BY_EVENT:" + eventHandler.getEvent();
 assertEquals(1, jedisProxy.scard(key));
 }

 @Test
 private void testRemoveIndex() throws Exception {
 EventHandler eventHandler = createEventHandler("testRemoveIndex");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

 String key = redisEventHandlerDAO.getNamespace() + ":EVENT_HANDLERS_BY_EVENT:" + eventHandler.getEvent();
 assertEquals(0, jedisProxy.scard(key));
 }

 private EventHandler createEventHandler(String name) {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(name);
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 return eventHandler;
 }
}