import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {RedisEventHandlerDAOTest.TestConfiguration.class})
public class RedisEventHandlerDAOTest {

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Mock
 private JedisProxy jedisProxy;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties redisProperties;

 private EventHandler eventHandler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 }

 @Test
 public void testAddEventHandler() throws Exception {
 redisEventHandlerDAO.addEventHandler(eventHandler);
 assertTrue(redisEventHandlerDAO.getAllEventHandlers().contains(eventHandler));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() throws Exception {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 }

 @Test
 public void testUpdateEventHandler() throws Exception {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("testUpdatedEvent");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 assertFalse(redisEventHandlerDAO.getAllEventHandlers().contains(eventHandler));
 assertTrue(redisEventHandlerDAO.getAllEventHandlers().contains(updatedEventHandler));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentName() throws Exception {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandlerUpdated");
 updatedEventHandler.setEvent("testUpdatedEvent");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() throws Exception {
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
 assertFalse(redisEventHandlerDAO.getAllEventHandlers().contains(eventHandler));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentName() throws Exception {
 redisEventHandlerDAO.removeEventHandler("nonExistentName");
 }

 @Test
 public void testGetAllEventHandlers() throws Exception {
 EventHandler handler1 = new EventHandler();
 handler1.setName("handler1");
 handler1.setEvent("event1");
 handler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(handler1);

 EventHandler handler2 = new EventHandler();
 handler2.setName("handler2");
 handler2.setEvent("event1");
 handler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(handler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertTrue(eventHandlers.contains(handler1));
 assertTrue(eventHandlers.contains(handler2));
 }

 @Test
 public void testGetHandlersForEvent() throws Exception {
 EventHandler handler1 = new EventHandler();
 handler1.setName("handler1");
 handler1.setEvent("testEvent");
 handler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(handler1);

 EventHandler handler2 = new EventHandler();
 handler2.setName("handler2");
 handler2.setEvent("testEvent");
 handler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(handler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, eventHandlers.size());
 assertTrue(eventHandlers.contains(handler1));
 assertTrue(eventHandlers.contains(handler2));

 List<EventHandler> activeHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, activeHandlers.size());
 assertTrue(activeHandlers.contains(handler1));
 }

 @Test
 public void testGetHandlerForEventWithNonExistentEvent() throws Exception {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false);
 assertEquals(0, eventHandlers.size());
 }

 @Configuration
 @Import({RedisProperties.class, ConductorProperties.class})
 public static class TestConfiguration {
 @Bean
 public JedisProxy jedisProxy() {
 return new JedisProxy();
 }

 @Bean
 public ObjectMapper objectMapper() {
 return new ObjectMapper();
 }

 @Bean
 public ConductorProperties conductorProperties() {
 return new ConductorProperties();
 }

 @Bean
 public RedisProperties redisProperties() {
 return new RedisProperties();
 }

 @Bean
 public RedisEventHandlerDAO redisEventHandlerDAO() {
 return new RedisEventHandlerDAO(
 jedisProxy(), objectMapper, conductorProperties, redisProperties);
 }
 }
}