import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;

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

import static org.junit.Assert.*;

@Configuration
public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLER_NAME_1 = "handler1";
 private static final String EVENT_HANDLER_NAME_2 = "handler2";
 private static final String EVENT = "testEvent";

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

 @Bean
 @Conditional(AnyRedisCondition.class)
 public JedisProxy jedisProxy() {
 return Mockito.mock(JedisProxy.class);
 }

 @Bean
 @Conditional(AnyRedisCondition.class)
 public ObjectMapper objectMapper() {
 return Mockito.mock(ObjectMapper.class);
 }

 @Bean
 @Conditional(AnyRedisCondition.class)
 public ConductorProperties conductorProperties() {
 return Mockito.mock(ConductorProperties.class);
 }

 @Bean
 @Conditional(AnyRedisCondition.class)
 public RedisProperties redisProperties() {
 return Mockito.mock(RedisProperties.class);
 }

 @Before
 public void setup() {
 Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(Maps.newHashMap());
 Mockito.when(objectMapper.readValue(Mockito.anyString(), EventHandler.class)).then(invocation -> {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(invocation.getArgument(0));
 return eventHandler;
 });

 Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenAnswer(invocation -> {
 String json = invocation.getArgument(1);
 return json != null ? json : null;
 });

 Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
 Mockito.when(jedisProxy.sadd(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
 Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(Sets.newHashSet("handler1", "handler2"));
 Mockito.when(jedisProxy.srem(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
 }

 @Test
 public void testAddEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME_1);
 eventHandler.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(EVENT_HANDLER_NAME_1), Mockito.anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflict() throws Exception {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_NAME_1);
 existingEventHandler.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName(EVENT_HANDLER_NAME_1);
 newEventHandler.setEvent(EVENT);

 try {
 redisEventHandlerDAO.addEventHandler(newEventHandler);
 } catch (Exception e) {
 assertEquals("EventHandler with name handler1 already exists!", e.getMessage());
 Mockito.verify(jedisProxy, Mockito.never()).hset(Mockito.anyString(), Mockito.eq(EVENT_HANDLER_NAME_1), Mockito.anyString());
 }
 }

 @Test
 public void testGetEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME_1);
 eventHandler.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 EventHandler getEventHandler = redisEventHandlerDAO.getEventHandler(EVENT_HANDLER_NAME_1);
 assertEquals(EVENT_HANDLER_NAME_1, getEventHandler.getName());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() throws Exception {
 try {
 redisEventHandlerDAO.getEventHandler("nonExistentHandler");
 } catch (Exception e) {
 assertEquals("EventHandler with name nonExistentHandler not found!", e.getMessage());
 }
 }

 @Test
 public void testUpdateEventHandler() throws Exception {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(EVENT_HANDLER_NAME_1);
 existingEventHandler.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(EVENT_HANDLER_NAME_1);
 updatedEventHandler.setEvent("updatedEvent");

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(EVENT_HANDLER_NAME_1), Mockito.anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() throws Exception {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("nonExistentHandler");
 updatedEventHandler.setEvent("updatedEvent");

 try {
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 } catch (Exception e) {
 assertEquals("EventHandler with name nonExistentHandler not found!", e.getMessage());
 }
 }

 @Test
 public void testRemoveEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(EVENT_HANDLER_NAME_1);
 eventHandler.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME_1);

 Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(EVENT_HANDLER_NAME_1));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() throws Exception {
 try {
 redisEventHandlerDAO.removeEventHandler("nonExistentHandler");
 } catch (Exception e) {
 assertEquals("EventHandler with name nonExistentHandler not found!", e.getMessage());
 }
 }

 @Test
 public void testGetAllEventHandlers() throws Exception {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(EVENT_HANDLER_NAME_1);
 eventHandler1.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(EVENT_HANDLER_NAME_2);
 eventHandler2.setEvent(EVENT);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 assertEquals(EVENT_HANDLER_NAME_1, eventHandlers.get(0).getName());
 assertEquals(EVENT_HANDLER_NAME_2, eventHandlers.get(1).getName());
 }

 @Test
 public void testGetEventHandlersForEvent() throws Exception {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(EVENT_HANDLER_NAME_1);
 eventHandler1.setEvent(EVENT);
 eventHandler1.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(EVENT_HANDLER_NAME_2);
 eventHandler2.setEvent(EVENT);
 eventHandler2.setActive(false);
 redisEventHandlerDAO.addEventHandler(eventHandler2);

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
 assertEquals(1, eventHandlers.size());
 assertEquals(EVENT_HANDLER_NAME_1, eventHandlers.get(0).getName());

 List<EventHandler> eventHandlersForAll = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
 assertEquals(2, eventHandlersForAll.size());
 assertEquals(EVENT_HANDLER_NAME_1, eventHandlersForAll.get(0).getName());
 assertEquals(EVENT_HANDLER_NAME_2, eventHandlersForAll.get(1).getName());
 }
}