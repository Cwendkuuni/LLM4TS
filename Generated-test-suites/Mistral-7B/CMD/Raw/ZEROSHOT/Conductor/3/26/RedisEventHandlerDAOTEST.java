import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.netflix.conductor.redis.util.JsonUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class RedisEventHandlerDAOTest {

 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Mock
 private EventHandler eventHandler;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 jedisProxy = mock(JedisProxy.class);
 objectMapper = mock(ObjectMapper.class);
 conductorProperties = mock(ConductorProperties.class);
 redisProperties = mock(RedisProperties.class);

 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);

 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 }

 @Test
 public void addEventHandlerShouldThrowConflictExceptionWhenEventHandlerWithSameNameExists() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("existingEventHandler");
 given(jedisProxy.hget(anyString(), anyString())).willReturn(JsonUtils.toJson(existingEventHandler));

 try {
 redisEventHandlerDAO.addEventHandler(new EventHandler());
 } catch (ConflictException ce) {
 // expected
 }

 then(jedisProxy).hset(anyString(), anyString(), anyString());
 // should not be called since event handler with same name already exists
 }

 @Test
 public void addEventHandlerShouldCallIndexMethodAndSaveEventHandlerInRedis() throws Exception {
 EventHandler eventHandlerToSave = new EventHandler();
 eventHandlerToSave.setName("testEventHandler");
 eventHandlerToSave.setEvent("testEvent");
 eventHandlerToSave.setActive(true);

 given(jedisProxy.hget(anyString(), anyString())).willReturn((String) null);
 given(objectMapper.writeValueAsString(eventHandlerToSave)).willReturn("testJsonString");

 redisEventHandlerDAO.addEventHandler(eventHandlerToSave);

 then(jedisProxy).hset(anyString(), anyString(), "testJsonString");
 then(jedisProxy).sadd(anyString(), "testEventHandler");
 }

 @Test
 public void updateEventHandlerShouldThrowNotFoundExceptionWhenEventHandlerWithSameNameDoesNotExist() {
 try {
 redisEventHandlerDAO.updateEventHandler(new EventHandler());
 } catch (NotFoundException nfe) {
 // expected
 }

 then(jedisProxy).hget(anyString(), anyString());
 // should not be called since event handler with same name does not exist
 }

 @Test
 public void updateEventHandlerShouldCallIndexMethodAndUpdateEventHandlerInRedis() throws Exception {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("existingEventHandler");
 existingEventHandler.setEvent("existingEvent");
 existingEventHandler.setActive(true);
 given(jedisProxy.hget(anyString(), anyString())).willReturn(JsonUtils.toJson(existingEventHandler));

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("updatedEventHandler");
 updatedEventHandler.setEvent("updatedEvent");
 updatedEventHandler.setActive(false);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 then(jedisProxy).hdel(anyString(), "existingEventHandler");
 then(jedisProxy).hset(anyString(), "updatedEventHandler", "updatedJsonString");
 then(jedisProxy).sadd(anyString(), "updatedEventHandler");
 }

 @Test
 public void removeEventHandlerShouldThrowNotFoundExceptionWhenEventHandlerWithSameNameDoesNotExist() {
 try {
 redisEventHandlerDAO.removeEventHandler("nonexistentEventHandler");
 } catch (NotFoundException nfe) {
 // expected
 }

 then(jedisProxy).hget(anyString(), "nonexistentEventHandler");
 // should not be called since event handler with such name does not exist
 }

 @Test
 public void removeEventHandlerShouldRemoveEventHandlerFromRedis() throws Exception {
 EventHandler eventHandlerToRemove = new EventHandler();
 eventHandlerToRemove.setName("testEventHandler");
 given(jedisProxy.hget(anyString(), anyString())).willReturn(JsonUtils.toJson(eventHandlerToRemove));

 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 then(jedisProxy).hdel(anyString(), "testEventHandler");
 }

 @Test
 public void getAllEventHandlersShouldReturnListOfAllEventHandlersInRedis() throws Exception {
 List<EventHandler> allEventHandlers = Lists.newArrayList(new EventHandler(), new EventHandler());
 given(jedisProxy.hgetAll(anyString())).willReturn(toMap("testKey", "testJsonString"));
 given(objectMapper.readValue(anyString(), EventHandler[].class)).willReturn(allEventHandlers.toArray(new EventHandler[0]));

 assertEquals(allEventHandlers, redisEventHandlerDAO.getAllEventHandlers());

 then(jedisProxy).hgetAll(anyString());
 }

 @Test
 public void getEventHandlersForEventShouldReturnListOfEventHandlersWithSameEventName() throws Exception {
 String eventName = "testEvent";
 List<EventHandler> eventHandlers = Lists.newArrayList(new EventHandler(), new EventHandler());
 given(jedisProxy.smembers(anyString())).willReturn(eventNames(eventHandlers));

 assertEquals(eventHandlers, redisEventHandlerDAO.getEventHandlersForEvent(eventName, false));

 then(jedisProxy).smembers(anyString());
 }

 @Test
 public void getEventHandlersForEventShouldReturnEmptyListWhenNoEventHandlersWithSameEventNameExist() throws Exception {
 given(jedisProxy.smembers(anyString())).willReturn(emptySet());
 assertNull(redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false));

 then(jedisProxy).smembers(anyString());
 }

 @Test
 public void getEventHandlersForEventShouldOnlyReturnActiveEventHandlersWhenActiveOnlyIsTrue() throws Exception {
 String eventName = "testEvent";
 List<EventHandler> eventHandlers = Lists.newArrayList(new EventHandler(), new EventHandler());
 EventHandler inactiveEventHandler = new EventHandler();
 inactiveEventHandler.setActive(false);
 given(jedisProxy.smembers(anyString())).willReturn(eventNames(eventHandlers, inactiveEventHandler));

 assertEquals(eventHandlers, redisEventHandlerDAO.getEventHandlersForEvent(eventName, true));

 then(jedisProxy).smembers(anyString());
 }

 // helper methods

 private Map<String, String> toMap(String key, String value) {
 Map<String, String> map = new HashMap<>();
 map.put(key, value);
 return map;
 }

 private Set<String> eventNames(EventHandler... handlers) {
 Set<String> names = handlers[0].getEvent().equals("testEvent") ? handlers[0].getNameSet() : handlers[1].getNameSet();
 return names;
 }
}