import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util. LinkedList;
import java.util.List;
import java.util.Map;

public class RedisEventHandlerDAOTest {

 private RedisEventHandlerDAO redisEventHandlerDAO;
 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 jedisProxy = mock(JedisProxy.class);
 objectMapper = mock(ObjectMapper.class);
 conductorProperties = mock(ConductorProperties.class);
 redisProperties = mock(RedisProperties.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
 verify(jedisProxy, times(1)).sadd(anyString(), eq(eventHandler.getName()));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent2");
 redisEventHandlerDAO.addEventHandler(newEventHandler);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("testEventUpdated");
 updatedEventHandler.setActive(false);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
 verify(jedisProxy, times(1)).srem(anyString(), eq(eventHandler.getName()));
 verify(jedisProxy, times(1)).hset(anyString(), eq(updatedEventHandler.getName()), anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistingName() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandlerUpdated");
 updatedEventHandler.setEvent("testEvent");
 updatedEventHandler.setActive(false);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

 verify(jedisProxy, times(1)).hdel(anyString(), eq(eventHandler.getName()));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistingName() {
 redisEventHandlerDAO.removeEventHandler("nonExistingEventHandlerName");
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
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName("testEventHandler3");
 eventHandler3.setEvent("testEvent2");
 eventHandler3.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, eventHandlers.size());
 List<EventHandler> eventHandlersActive = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, eventHandlersActive.size());
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 String json = toJson(eventHandler);
 when(jedisProxy.hget(anyString(), eq(eventHandler.getName()))).thenReturn(json);

 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(eventHandler, retrievedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistingName() {
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
 redisEventHandlerDAO.getEventHandler("nonExistingEventHandlerName");
 }
}