import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.PropertySource;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@Configuration
@PropertySource(value = "classpath:test-application.properties")
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

 @Bean
 public EventHandlerDAO eventHandlerDAO() {
 return redisEventHandlerDAO;
 }

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(conductorProperties.getRedisHosts()).thenReturn(new String[]{"127.0.0.1"});
 when(redisProperties.getNamespace()).thenReturn("testnamespace");
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflict() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 String json = "{\"name\":\"testEventHandler\", \"event\":\"testEvent\", \"active\":true}";
 when(jedisProxy.hget(anyString(), anyString())).thenReturn(json);

 EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
 assertEquals(eventHandler, returnedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 when(jedisProxy.hget(anyString(), "nonExistentEventHandler")).thenReturn(null);

 try {
 redisEventHandlerDAO.getEventHandler("nonExistentEventHandler");
 fail();
 } catch (NotFoundException e) {
 // expected
 }
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("testEventHandler");
 updatedEventHandler.setEvent("updatedTestEvent");
 updatedEventHandler.setActive(false);

 redisEventHandlerDAO.updateEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 when(jedisProxy.hget(anyString(), "nonExistentEventHandler")).thenReturn(null);

 try {
 redisEventHandlerDAO.updateEventHandler(eventHandler);
 fail();
 } catch (NotFoundException e) {
 // expected
 }
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);

 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 verify(jedisProxy, times(1)).hdel(anyString(), "testEventHandler");
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent1");
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent2");
 eventHandler2.setActive(false);

 Map<String, String> eventHandlers = Maps.newLinkedHashMap();
 eventHandlers.put("testEventHandler1", toJson(eventHandler1));
 eventHandlers.put("testEventHandler2", toJson(eventHandler2));

 when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);

 List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, returnedEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(false);

 Map<String, Set<String>> eventHandlersByEvent = new LinkedHashMap<>();
 eventHandlersByEvent.put("testEvent", createSet("testEventHandler1", "testEventHandler2"));

 when(jedisProxy.smembers(anyString())).then((InvocationOnMock invocation) -> {
 Set<String> names = invocation.getArgument(0);
 return eventHandlersByEvent.get(invocation.getArgument(1));
 });

 List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, returnedEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testEventHandler1");
 eventHandler1.setEvent("testEvent");
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName("testEventHandler2");
 eventHandler2.setEvent("testEvent");
 eventHandler2.setActive(false);

 Map<String, Set<String>> eventHandlersByEvent = new LinkedHashMap<>();
 eventHandlersByEvent.put("testEvent", createSet("testEventHandler1"));

 when(jedisProxy.smembers(anyString())).then((InvocationOnMock invocation) -> {
 Set<String> names = invocation.getArgument(0);
 return eventHandlersByEvent.get(invocation.getArgument(1));
 });

 List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, returnedEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventEmpty() {
 when(jedisProxy.smembers(anyString())).thenReturn(createEmptySet());

 List<EventHandler> returnedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(0, returnedEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventNotFound() {
 when(jedisProxy.smembers(anyString())).thenReturn(createEmptySet());

 try {
 redisEventHandlerDAO.getEventHandlersForEvent("nonexistentEvent", false);
 fail();
 } catch (NotFoundException e) {
 // expected
 }
 }

 private String toJson(EventHandler eventHandler) {
 ObjectMapper objectMapper = mock(ObjectMapper.class);
 try {
 return objectMapper.writeValueAsString(eventHandler);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 private Set<String> createSet(String... strings) {
 LinkedHashSet<String> set = new LinkedHashSet<>();
 for (String string : strings) {
 set.add(string);
 }
 return set;
 }

 private Map<String, Set<String>> createMap(Map<String, Set<String>> map) {
 return map;
 }
}