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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.jedis.JedisProxyMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringJUnitConfig(AppTestConfig.class)
@ActiveProfiles("test")
public class RedisEventHandlerDAOTest {

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Mock
 private JedisProxyMock jedisProxy;

 @Mock
 private ObjectMapper objectMapper;

 @Mock
 private ConductorProperties conductorProperties;

 @Mock
 private RedisProperties redisProperties;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 given(conductorProperties.getRedis()).isPresent();
 given(redisProperties.getNamespace()).isPresent();
 given(jedisProxy.hget(anyString(), anyString())).willReturn("{}");
 given(jedisProxy.hlen(anyString())).willReturn(0L);
 given(jedisProxy.hgetAll(anyString())).willReturn(Maps.newHashMap());
 given(jedisProxy.sadd(anyString(), anyString())).willReturn(1L);
 given(jedisProxy.slen(anyString())).willReturn(1L);
 given(jedisProxy.smembers(anyString())).willReturn(Sets.newLinkedHashSet("handler1"));
 given(jedisProxy.hset(anyString(), anyString(), anyString())).willAnswer(invocation -> 1);
 }

 @Configuration
 @PropertySources(value = @PropertySource(factory = RedisProperties.class))
 static class AppTestConfig {

 @Bean
 JedisProxy jedisProxy() {
 return mock(JedisProxy.class);
 }

 @Bean
 ObjectMapper objectMapper() {
 return mock(ObjectMapper.class);
 }

 @Bean
 ConductorProperties conductorProperties() {
 return mock(ConductorProperties.class);
 }

 @Bean
 RedisProperties redisProperties() {
 return mock(RedisProperties.class);
 }
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler("handler1", "event1", Lists.newArrayList(), null);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 given(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("handler1"))).willReturn("{}");
 given(objectMapper.readValue(anyString(), eq(EventHandler.class))).willReturn(eventHandler);
 assertEquals("handler1", redisEventHandlerDAO.getEventHandler("handler1").getName());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() {
 EventHandler eventHandler = new EventHandler("handler1", "event1", Lists.newArrayList(), null);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler = new EventHandler("handler1", "event1", Lists.newArrayList(), null);
 given(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("handler1"))).willReturn("{}");
 given(objectMapper.readValue(anyString(), eq(EventHandler.class))).willReturn(eventHandler);
 EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("handler1");
 assertSame(eventHandler, retrievedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistentName() {
 given(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), anyString())).willReturn(null);
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler("nonExistentHandler"));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler("handler1", "event1", Lists.newArrayList(), null);
 EventHandler updatedEventHandler = new EventHandler("handler1", "event1", Lists.newArrayList("newArg"), null);
 given(redisEventHandlerDAO.getEventHandler("handler1")).willReturn(existingEventHandler);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 given(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("handler1"))).willReturn("{}");
 given(objectMapper.readValue(anyString(), eq(EventHandler.class))).willReturn(updatedEventHandler);
 assertSame(updatedEventHandler, redisEventHandlerDAO.getEventHandler("handler1"));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentName() {
 given(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), anyString())).willReturn(null);
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(new EventHandler("nonExistentHandler", "event1", Lists.newArrayList(), null)));
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler("handler1", "event1", Lists.newArrayList(), null);
 redisEventHandlerDAO.addEventHandler(eventHandler);
 redisEventHandlerDAO.removeEventHandler("handler1");
 given(jedisProxy.hlen(eq(nsKey(EVENT_HANDLERS)))).willReturn(0L);
 assertNull(redisEventHandlerDAO.getEventHandler("handler1"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentName() {
 redisEventHandlerDAO.removeEventHandler("nonExistentHandler");
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler("handler1", "event1", Lists.newArrayList(), null);
 EventHandler eventHandler2 = new EventHandler("handler2", "event2", Lists.newArrayList(), null);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 assertEquals(eventHandler2, eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveHandlersOnly() {
 EventHandler eventHandler1 = new EventHandler("handler1", "event1", Lists.newArrayList(), true);
 EventHandler eventHandler2 = new EventHandler("handler2", "event1", Lists.newArrayList(), false);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(1, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 }

 @Test
 public void testGetEventHandlersForEventWithInactiveHandlers() {
 EventHandler eventHandler1 = new EventHandler("handler1", "event1", Lists.newArrayList(), true);
 EventHandler eventHandler2 = new EventHandler("handler2", "event1", Lists.newArrayList(), false);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
 assertEquals(2, eventHandlers.size());
 assertEquals(eventHandler1, eventHandlers.get(0));
 assertEquals(eventHandler2, eventHandlers.get(1));
 }

 @Test
 public void testGetEventHandlersForEventWithNoHandlers() {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(0, eventHandlers.size());
 }
}