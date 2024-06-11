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
import com.netflix.conductor.redis.jackson.ObjectMapperBuilder;

import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 this.jedisProxy = Mockito.mock(JedisProxy.class);
 this.objectMapper = ObjectMapperBuilder.getObjectMapper();
 this.conductorProperties = Mockito.mock(ConductorProperties.class);
 this.redisProperties = Mockito.mock(RedisProperties.class);

 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 assertEquals("testEventHandler", jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler"));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
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
 updatedEventHandler.setEvent("testEventUpdated");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 assertEquals("testEventHandler", jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler"));
 assertEquals("testEventUpdated", jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler"));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandlerUpdated");
 updatedEventHandler.setEvent("testEvent");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler"));
}

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
}

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertEquals("testEventHandler1", eventHandlers.get(0).getName());
 assertEquals("testEventHandler2", eventHandlers.get(1).getName());
}

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent1");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent1");
 eventHandler2.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("testEventHandler3");
 eventHandler3.setEvent("testEvent2");
 eventHandler3.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
 assertEquals(2, eventHandlers.size());
 assertEquals("testEventHandler1", eventHandlers.get(0).getName());
 assertEquals("testEventHandler2", eventHandlers.get(1).getName());

 List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);
 assertEquals(1, activeEventHandlers.size());
 assertEquals("testEventHandler1", activeEventHandlers.get(0).getName());
}

 @Test
 public void testGetEventHandlersForEventNotFound() {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent3", false);
 assertEquals(0, eventHandlers.size());
}
}