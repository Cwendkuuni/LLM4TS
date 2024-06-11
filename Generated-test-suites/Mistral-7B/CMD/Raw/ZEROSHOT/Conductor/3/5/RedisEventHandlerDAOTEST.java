import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RedisEventHandlerDAOTest {

 private JedisProxy jedisProxyMock;
 private ObjectMapper objectMapperMock;
 private ConductorProperties conductorPropertiesMock;
 private RedisProperties redisPropertiesMock;
 private EventHandlerDAO redisEventHandlerDAO;
 private AnnotationConfigApplicationContext applicationContext;

 @Before
 public void setup() {
 ConductorProperties conductorProperties = new ConductorProperties();
 RedisProperties redisProperties = new RedisProperties();
 applicationContext = new AnnotationConfigApplicationContext();
 applicationContext.register(AnyRedisCondition.class);
 applicationContext.refresh();

 jedisProxyMock = Mockito.mock(JedisProxy.class);
 objectMapperMock = Mockito.mock(ObjectMapper.class);
 conductorPropertiesMock = conductorProperties;
 redisPropertiesMock = redisProperties;
 redisEventHandlerDAO = applicationContext.getBean(RedisEventHandlerDAO.class);

 Mockito.when(jedisProxyMock.hgetAll(ArgumentMatchers.anyString()))
 .thenReturn(createMap("EventHandler1", "{}", "EventHandler2", "{}"));
 Mockito.when(jedisProxyMock.hget(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
 .thenAnswer(invocation -> invocation.getArguments()[1]);
 Mockito.when(jedisProxyMock.hset(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
 .thenReturn(1);
 Mockito.when(jedisProxyMock.sadd(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
 .thenReturn(1);
 Mockito.when(jedisProxyMock.srem(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
 .thenReturn(1);

 applicationContext.getBean(JedisProxy.class).setProxy(jedisProxyMock);
 applicationContext.getBean(ObjectMapper.class).setMapper(objectMapperMock);
 applicationContext.getBean(ConductorProperties.class).setProperties(conductorPropertiesMock);
 applicationContext.getBean(RedisProperties.class).setProperties(redisPropertiesMock);
 }

 @Test
 public void testAddEventHandler() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("EventHandler1");
 eventHandler.setEvent("event1");
 eventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(eventHandler);

 Mockito.verify(jedisProxyMock, Mockito.times(1)).hset(ArgumentMatchers.anyString(), ArgumentMatchers.eq("EventHandler1"), ArgumentMatchers.anyString());
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingName() throws Exception {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("EventHandler1");
 eventHandler.setEvent("event1");
 redisEventHandlerDAO.addEventHandler(eventHandler);
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("EventHandler1");
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 }

 @Test
 public void testGetAllEventHandlers() throws Exception {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlerForName() throws Exception {
 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("EventHandler1");
 assertNotNull(eventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerForNonExistentName() throws Exception {
 redisEventHandlerDAO.getEventHandler("NonExistentName");
 }

 @Test
 public void testUpdateEventHandler() throws Exception {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("EventHandler1");
 existingEventHandler.setEvent("event1");
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("EventHandler1");
 updatedEventHandler.setEvent("event2");
 updatedEventHandler.setActive(false);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 Mockito.verify(jedisProxyMock, Mockito.times(2)).hset(ArgumentMatchers.anyString(), ArgumentMatchers.eq("EventHandler1"), ArgumentMatchers.anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerForNonExistentName() throws Exception {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName("NonExistentName");
 updatedEventHandler.setEvent("event1");
 updatedEventHandler.setActive(true);
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 }

 @Test
 public void testRemoveEventHandler() throws Exception {
 redisEventHandlerDAO.removeEventHandler("EventHandler1");
 Mockito.verify(jedisProxyMock, Mockito.times(1)).hdel(ArgumentMatchers.anyString(), ArgumentMatchers.eq("EventHandler1"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerForNonExistentName() throws Exception {
 redisEventHandlerDAO.removeEventHandler("NonExistentName");
 }

 @Test
 public void testGetEventHandlersForEvent() throws Exception {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEventWithInactiveHandlers() throws Exception {
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
 assertEquals(2, eventHandlers.size());
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlersForNonExistentEvent() throws Exception {
 redisEventHandlerDAO.getEventHandlersForEvent("NonExistentEvent", true);
 }
}