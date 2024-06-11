import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = RedisEventHandlerDAOTest.Config.class)
@ActiveProfiles("test")
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

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(jedisProxy.hget(anyString(), anyString())).thenAnswer(i -> null);
 when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);
 when(objectMapper.readValue(anyString(), EventHandler.class)).thenAnswer(i -> new EventHandler());
 }

 @After
 public void tearDown() {
 // clear mocks
 }

 @Test
 public void testAddEventHandlerSuccess() {
 EventHandler eventHandler = new EventHandler();
 redisEventHandlerDAO.addEventHandler(eventHandler);
 verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerConflict() {
 EventHandler eventHandler = new EventHandler();
 EventHandler existingEventHandler = new EventHandler();
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()))).thenReturn(toJson(existingEventHandler).getBytes());
 redisEventHandlerDAO.addEventHandler(eventHandler);
 verify(jedisProxy, never()).hset(anyString(), anyString(), anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 String name = "testEventHandler";
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name))).thenReturn(null);
 redisEventHandlerDAO.getEventHandler(name);
 }

 @Test
 public void testGetEventHandlerExists() {
 EventHandler eventHandler = new EventHandler();
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()))).thenReturn(toJson(eventHandler).getBytes());
 EventHandler result = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
 assertEquals(eventHandler, result);
 }

 @Test(expected = TransientException.class)
 public void testGetEventHandlerTransientException() {
 when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException("Test Transient Exception"));
 redisEventHandlerDAO.getEventHandler("testEventHandler");
 }

 @Test
 public void testUpdateEventHandlerSuccess() {
 EventHandler existingEventHandler = new EventHandler();
 EventHandler updatedEventHandler = new EventHandler();
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(existingEventHandler.getName()))).thenReturn(toJson(existingEventHandler).getBytes());
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler updatedEventHandler = new EventHandler();
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandlerSuccess() {
 EventHandler eventHandler = new EventHandler();
 redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
 verify(jedisProxy, times(1)).hdel(anyString(), eventHandler.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 String name = "testEventHandler";
 redisEventHandlerDAO.removeEventHandler(name);
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 EventHandler eventHandler2 = new EventHandler();
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Map.of("name1", toJson(eventHandler1).getBytes(), "name2", toJson(eventHandler2).getBytes()));
 List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, result.size());
 assertEquals(eventHandler1, result.get(0));
 assertEquals(eventHandler2, result.get(1));
 }

 @Test
 public void testGetEventHandlersForEventSuccess() {
 EventHandler eventHandler1 = new EventHandler();
 EventHandler eventHandler2 = new EventHandler();
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Set.of("name1", "name2"));
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("name1"))).thenReturn(toJson(eventHandler1).getBytes());
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("name2"))).thenReturn(toJson(eventHandler2).getBytes());
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(2, result.size());
 assertEquals(eventHandler1, result.get(0));
 assertEquals(eventHandler2, result.get(1));
 }

 @Test
 public void testGetEventHandlersForEventEmpty() {
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Set.of());
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 assertEquals(0, result.size());
 }

 @Test
 public void testGetEventHandlersForEventActiveOnlySuccess() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Set.of("name1", "name2"));
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("name1"))).thenReturn(toJson(eventHandler1).getBytes());
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("name2"))).thenReturn(toJson(eventHandler2).getBytes());
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(1, result.size());
 assertEquals(eventHandler1, result.get(0));
 }

 @Test
 public void testGetEventHandlersForEventActiveOnlyEmpty() {
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Set.of());
 List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 assertEquals(0, result.size());
 }
}

 @Configuration
 @PropertySources({
 @PropertySource("classpath:test-application.properties")
 })
 public static class Config {
 @Bean
 public JedisProxy jedisProxy() {
 return mock(JedisProxy.class);
 }

 @Bean
 public ObjectMapper objectMapper() {
 return mock(ObjectMapper.class);
 }

 @Bean
 public ConductorProperties conductorProperties() {
 return mock(ConductorProperties.class);
 }

 @Bean
 public RedisProperties redisProperties() {
 return mock(RedisProperties.class);
 }
}