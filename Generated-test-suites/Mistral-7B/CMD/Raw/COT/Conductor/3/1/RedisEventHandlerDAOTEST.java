import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties.RedisType;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.testutils.TestRedisConnectionFactory;
import com.netflix.conductor.testutils.TestConfigurer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@Configuration
@Import(RedisEventHandlerDAO.class)
public class RedisEventHandlerDAOTest {

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Bean
 public JedisProxy jedisProxy() {
 return new TestRedisConnectionFactory().getJedisProxy();
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
 return new RedisProperties(RedisType.SINGLE);
 }

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 new TestConfigurer().withAwaitTimeout(5000).configure(this);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertSame(eventHandler, retrievedEventHandler);
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflictingName() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler conflictingEventHandler = new EventHandler();
 conflictingEventHandler.setName("testEventHandler");
 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(conflictingEventHandler));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedTestEvent");
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertSame(updatedEventHandler, retrievedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentEventHandler() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedTestEvent");

 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 assertNull(redisEventHandlerDAO.getEventHandler("testEventHandler"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentEventHandler() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 assertNull(redisEventHandlerDAO.getEventHandler("nonExistentEventHandler"));
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent1");
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent2");
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertSame(eventHandler1, eventHandlers.get(0));
 assertSame(eventHandler2, eventHandlers.get(1));
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
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("testEventHandler3");
 eventHandler3.setEvent("testEvent2");
 eventHandler3.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
 assertEquals(2, eventHandlers.size());
 assertSame(eventHandler1, eventHandlers.get(0));
 assertSame(eventHandler2, eventHandlers.get(1));

 List<EventHandler> eventHandlersActive = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);
 assertEquals(1, eventHandlersActive.size());
 assertSame(eventHandler1, eventHandlersActive.get(0));
 }

 @Test
 public void testGetEventHandlersForEventWithNonExistentEvent() {
 assertArrayEquals(Lists.newArrayList(), redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false));
 assertArrayEquals(Lists.newArrayList(), redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", true));
 }
}