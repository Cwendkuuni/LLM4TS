import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
 private static final String NAME_EVENT_HANDLER_1 = "eventHandler1";
 private static final String NAME_EVENT_HANDLER_2 = "eventHandler2";
 private static final String NAME_EVENT_HANDLER_3 = "eventHandler3";
 private static final String EVENT_EVENT_HANDLER_1 = "testEvent";

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

 private void setupMocks() {
 Mockito.when(jedisProxy.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(NAME_EVENT_HANDLER_1)))
 .thenReturn("{}");
 Mockito.when(jedisProxy.hgetAll(Mockito.eq(nsKey(EVENT_HANDLERS))))
 .thenReturn(new HashMap<String, String>() {{
 put(NAME_EVENT_HANDLER_1, "{}");
 put(NAME_EVENT_HANDLER_2, "{}");
 put(NAME_EVENT_HANDLER_3, "{}");
 }});
 Mockito.when(jedisProxy.hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(NAME_EVENT_Handler_1),
 Mockito.eq("{}"))).thenReturn(1);
 Mockito.when(jedisProxy.hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(NAME_EVENT_HANDLER_1)))
 .thenReturn(1);
 Mockito.when(jedisProxy.sadd(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_EVENT_HANDLER_1)),
 Mockito.eq(NAME_EVENT_HANDLER_1))).thenReturn(1);
 Mockito.when(jedisProxy.srem(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_EVENT_HANDLER_1)),
 Mockito.eq(NAME_EVENT_HANDLER_1))).thenReturn(1);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 // Check if the event handler was added to the Redis
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(NAME_EVENT_Handler_1), Mockito.eq("{}")));
 // Check if the event handler was indexed
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .sadd(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_EVENT_HANDLER_1)), Mockito.eq(NAME_EVENT_HANDLER_1)));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflictException() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(NAME_EVENT_HANDLER_1);
 eventHandler2.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler2.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 EventHandler eventHandlerUpdated = new EventHandler();
 eventHandlerUpdated.setName(NAME_EVENT_HANDLER_1);
 eventHandlerUpdated.setEvent(EVENT_EVENT_HANDLER_2);
 eventHandlerUpdated.setActive(false);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.updateEventHandler(eventHandlerUpdated);

 // Check if the event handler was updated in the Redis
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(NAME_EVENT_Handler_1), Mockito.eq("{}")));
 // Check if the event handler was unindexed and indexed again
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .srem(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_EVENT_HANDLER_1)), Mockito.eq(NAME_EVENT_HANDLER_1)));
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .sadd(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_EVENT_HANDLER_2)), Mockito.eq(NAME_EVENT_HANDLER_1)));
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNotFoundException() {
 EventHandler eventHandlerUpdated = new EventHandler();
 eventHandlerUpdated.setName(NAME_EVENT_HANDLER_1);
 eventHandlerUpdated.setEvent(EVENT_EVENT_HANDLER_2);
 eventHandlerUpdated.setActive(false);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.updateEventHandler(eventHandlerUpdated);
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.removeEventHandler(NAME_EVENT_HANDLER_1);

 // Check if the event handler was removed from the Redis
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(NAME_EVENT_Handler_1)));
 // Check if the event handler was unindexed
 assertEquals(1, Mockito.verify(jedisProxy, Mockito.times(1))
 .srem(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT_EVENT_HANDLER_1)), Mockito.eq(NAME_EVENT_HANDLER_1)));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNotFoundException() {
 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.removeEventHandler(NAME_EVENT_HANDLER_1);
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(NAME_EVENT_HANDLER_2);
 eventHandler2.setEvent(EVENT_EVENT_HANDLER_2);
 eventHandler2.setActive(false);

 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName(NAME_EVENT_HANDLER_3);
 eventHandler3.setEvent(EVENT_EVENT_HANDLER_3);
 eventHandler3.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(3, allEventHandlers.size());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(NAME_EVENT_HANDLER_2);
 eventHandler2.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler2.setActive(false);

 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName(NAME_EVENT_HANDLER_3);
 eventHandler3.setEvent(EVENT_EVENT_HANDLER_3);
 eventHandler3.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_EVENT_HANDLER_1, false);
 assertEquals(2, eventHandlersForEvent.size());
 }

 @Test
 public void testGetEventHandlersForEventWithActiveOnly() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(NAME_EVENT_HANDLER_2);
 eventHandler2.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler2.setActive(false);

 EventHandler eventHandler3 = new EventHandler();
 eventHandler3.setName(NAME_EVENT_HANDLER_3);
 eventHandler3.setEvent(EVENT_EVENT_HANDLER_3);
 eventHandler3.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);
 redisEventHandlerDAO.addEventHandler(eventHandler2);
 redisEventHandlerDAO.addEventHandler(eventHandler3);

 List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_EVENT_HANDLER_1, true);
 assertEquals(1, eventHandlersForEvent.size());
 }

 @Test
 public void testGetEventHandler() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(NAME_EVENT_HANDLER_1);
 eventHandler1.setEvent(EVENT_EVENT_HANDLER_1);
 eventHandler1.setActive(true);

 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.addEventHandler(eventHandler1);

 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(NAME_EVENT_HANDLER_1);
 assertEquals(eventHandler1, eventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNotFoundException() {
 setupMocks();
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
 redisEventHandlerDAO.getEventHandler(NAME_EVENT_HANDLER_1);
 }
}