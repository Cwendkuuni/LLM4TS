import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringJUnitConfig(classes = RedisEventHandlerDAOTest.class)
public class RedisEventHandlerDAOTest {

 @MockBean
 private JedisProxy jedisProxy;

 @MockBean
 private ConductorProperties conductorProperties;

 @MockBean
 private RedisProperties redisProperties;

 @Autowired
 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for addEventHandler method
 @Test
 public void testAddEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent", true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 // Check if the handler is added in the Redis
 verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 // Test for addEventHandler with existing handler
 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingHandler() throws Exception {
 EventHandler existingHandler = new EventHandler("existingHandler", "testEvent", true);
 redisEventHandlerDAO.addEventHandler(existingHandler);
 EventHandler newHandler = new EventHandler("newHandler", "testEvent", true);
 redisEventHandlerDAO.addEventHandler(newHandler);
 }

 // Test for getAllEventHandlers method
 @Test
 public void testGetAllEventHandlers() throws Exception {
 EventHandler handler1 = new EventHandler("handler1", "event1", true);
 EventHandler handler2 = new EventHandler("handler2", "event1", true);

 when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("handler1", toJson(handler1), "handler2", toJson(handler2)));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 verify(jedisProxy, times(1)).hgetAll(anyString());
 }

 // Test for getEventHandlerForEvent method with activeOnly = false
 @Test
 public void testGetEventHandlerForEventWithActiveOnlyFalse() throws Exception {
 EventHandler handler1 = new EventHandler("handler1", "event1", true);
 EventHandler handler2 = new EventHandler("handler2", "event1", false);

 when(jedisProxy.smembers(anyString())).thenReturn(Lists.newArrayList("handler1", "handler2"));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
 assertEquals(2, eventHandlers.size());
 verify(jedisProxy, times(1)).smembers(anyString());
 }

 // Test for getEventHandlerForEvent method with activeOnly = true
 @Test
 public void testGetEventHandlerForEventWithActiveOnlyTrue() throws Exception {
 EventHandler handler1 = new EventHandler("handler1", "event1", true);
 EventHandler handler2 = new EventHandler("handler2", "event1", false);

 when(jedisProxy.smembers(anyString())).thenReturn(Lists.newArrayList("handler1"));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(1, eventHandlers.size());
 verify(jedisProxy, times(1)).smembers(anyString());
 }

 // Test for removeEventHandler method
 @Test
 public void testRemoveEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent", true);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 // Check if the handler is removed from Redis
 verify(jedisProxy, times(1)).hdel(anyString(), anyString());
 }

 // Test for removeEventHandler with non-existing handler
 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistingHandler() throws Exception {
 redisEventHandlerDAO.removeEventHandler("testEventHandler");
 }

 // Test for updateEventHandler method
 @Test
 public void testUpdateEventHandler() throws Exception {
 EventHandler oldEventHandler = new EventHandler("testEventHandler", "testEvent", true);
 EventHandler newEventHandler = new EventHandler("testEventHandler", "testEvent2", true);
 redisEventHandlerDAO.addEventHandler(oldEventHandler);
 redisEventHandlerDAO.updateEventHandler(newEventHandler);

 // Check if the handler is updated in Redis
 verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 // Test for updateEventHandler with non-existing handler
 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistingHandler() throws Exception {
 EventHandler newEventHandler = new EventHandler("testEventHandler", "testEvent2", true);
 redisEventHandlerDAO.updateEventHandler(newEventHandler);
 }

 // Test for updateEventHandler with same handler
 @Test(expected = ConflictException.class)
 public void testUpdateEventHandlerWithSameHandler() throws Exception {
 EventHandler oldEventHandler = new EventHandler("testEventHandler", "testEvent", true);
 EventHandler newEventHandler = new EventHandler("testEventHandler", "testEvent", true);
 redisEventHandlerDAO.addEventHandler(oldEventHandler);
 redisEventHandlerDAO.updateEventHandler(newEventHandler);
 }

 private String toJson(EventHandler eventHandler) throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.writeValueAsString(eventHandler);
 }

 private List<EventHandler> readValue(String value, TypeReference<List<EventHandler>> typeReference) throws Exception {
 ObjectMapper objectMapper = new ObjectMapper();
 return objectMapper.readValue(value, typeReference);
 }
}