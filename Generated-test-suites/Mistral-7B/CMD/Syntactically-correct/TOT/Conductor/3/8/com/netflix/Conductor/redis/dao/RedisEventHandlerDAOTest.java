import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.TestRedisConnectionFactory;
import com.netflix.conductor.redis.test.TestRedisConnectionManager;
import com.netflix.conductor.test.TestApplicationContextInitializer;
import com.netflix.conductor.test.junit.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisEventHandlerDAOTest.TestConfiguration.class})
public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLER_NAME = "testEventHandler";
 private static final String EVENT = "testEvent";

 @Autowired
 private EventHandlerDAO eventHandlerDAO;

 @Before
 public void setup() {
 EventHandler eventHandler = new Handler("testEventHandler", "testHandlerClass", EVENT, false);
 eventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler savedEventHandler = eventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
 EventHandler expectedEventHandler = new Handler("testEventHandler", "testHandlerClass", EVENT, false);
 assertEquals(expectedEventHandler, savedEventHandler);
 }

 @Test
 public void testAddEventHandlerWithDuplicateName() {
 EventHandler eventHandler = new Handler("testEventHandler", "testHandlerClass2", EVENT, false);
 assertThrows(ConflictException.class, () -> eventHandlerDAO.addEventHandler(eventHandler));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler updatedEventHandler = new Handler("testEventHandler", "updatedHandlerClass", EVENT, false);
 eventHandlerDAO.updateEventHandler(updatedEventHandler);
 EventHandler savedEventHandler = eventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
 assertEquals(updatedEventHandler, savedEventHandler);
 }

 @Test
 public void testUpdateEventHandlerWithWrongName() {
 EventHandler updatedEventHandler = new Handler("wrongEventHandlerName", "updatedHandlerClass", EVENT, false);
 assertThrows(NotFoundException.class, () -> eventHandlerDAO.updateEventHandler(updatedEventHandler));
 }

 @Test
 public void testRemoveEventHandler() {
 eventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
 EventHandler removedEventHandler = eventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME);
 assertNull(removedEventHandler);
 }

 @Test
 public void testRemoveEventHandlerWithWrongName() {
 assertThrows(NotFoundException.class, () -> eventHandlerDAO.removeEventHandler("wrongEventHandlerName"));
 }

 @Test
 public void testGetAllEventHandlers() {
 List<EventHandler> allEventHandlers = eventHandlerDAO.getAllEventHandlers();
 assertEquals(1, allEventHandlers.size());
 assertEquals(EVENT_HANDLER_NAME, allEventHandlers.get(0).getName());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent(EVENT, true);
 assertEquals(1, eventHandlers.size());
 assertEquals(EVENT_HANDLER_NAME, eventHandlers.get(0).getName());

 List<EventHandler> eventHandlersForAnotherEvent = eventHandlerDAO.getEventHandlersForEvent("anotherEvent", true);
 assertFalse(eventHandlersForAnotherEvent.isEmpty());
 assertFalse(eventHandlersForAnotherEvent.contains(eventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME)));
 }

 @Test
 public void testGetEventHandlersForEventWithInactiveHandlers() {
 List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent(EVENT, false);
 assertEquals(1, eventHandlers.size());
 assertEquals(EVENT_HANDLER_NAME, eventHandlers.get(0).getName());
 }

 @Configuration
 @Import({TestApplicationContextInitializer.class, ConductorProperties.class, RedisProperties.class})
 static class TestConfiguration {

 @Bean
 public JedisProxy jedisProxy() {
 JedisProxy jedisProxy = mock(JedisProxy.class);
 when(jedisProxy.hget(ArgumentMatchers.eq(Mockito.anyString()), ArgumentMatchers.eq(Mockito.anyString())))
 .thenAnswer(invocation -> invocation.getArgument(1));
 when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
 .thenAnswer(invocation -> "OK");
 when(jedisProxy.hdel(Mockito.anyString(), Mockito.anyString()))
 .thenAnswer(invocation -> "OK");
 when(jedisProxy.sadd(Mockito.anyString(), Mockito.anyString()))
 .thenAnswer(invocation -> "OK");
 when(jedisProxy.srem(Mockito.anyString(), Mockito.anyString()))
 .thenAnswer(invocation -> "OK");
 when(jedisProxy.smembers(Mockito.anyString()))
 .thenAnswer(invocation -> Lists.newArrayList("testEventHandler"));
 return jedisProxy;
 }

 @Bean
 public EventHandlerDAO eventHandlerDAO(JedisProxy jedisProxy, ObjectMapper objectMapper, ConductorProperties conductorProperties, RedisProperties redisProperties) {
 return new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Bean
 public TestRedisConnectionFactory connectionFactory() {
 return new TestRedisConnectionFactory();
 }

 @Bean
 public TestRedisConnectionManager connectionManager() {
 return new TestRedisConnectionManager();
 }
 }

 private static class Handler implements EventHandler {

 private final String name;
 private final String handlerClass;
 private final String event;
 private final boolean active;

 public Handler(String name, String handlerClass, String event, boolean active) {
 this.name = name;
 this.handlerClass = handlerClass;
 this.event = event;
 this.active = active;
 }

 @Override
 public String getName() {
 return name;
 }

 @Override
 public String getHandlerClass() {
 return handlerClass;
 }

 @Override
 public String getEvent() {
 return event;
 }

 @Override
 public boolean isActive() {
 return active;
 }

 @Override
 public void handleEvent(Map<String, Object> eventData, String correlationToken) {
 // empty
 }
 }
}