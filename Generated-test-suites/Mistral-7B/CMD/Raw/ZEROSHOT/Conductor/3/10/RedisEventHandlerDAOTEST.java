import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

 @Mock
 private JedisProxy jedisProxy;
 @Mock
 private ObjectMapper objectMapper;
 @Mock
 private ConductorProperties conductorProperties;
 @Mock
 private RedisProperties redisProperties;
 @Mock
 private com.netflix.conductor.common.metrics.Metrics metrics;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testHandlerValue");
 when(jedisProxy.hset(anyString(), eq("testHandler"), anyString())).thenReturn(1L);

 redisEventHandlerDAO.addEventHandler(eventHandler);
 verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"), eq("testHandlerValue"));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflict() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testHandlerValue");
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn("existHandlerValue");

 redisEventHandlerDAO.addEventHandler(eventHandler);
 verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"), anyString()); // should not be called
 verify(jedisProxy, times(1)).hset(anyString(), eq("testHandler"), anyString());
 verifyThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
 }

 @Test
 public void testGetAllEventHandlers() {
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName("testHandler1");
 eventHandler1.setEvent("testEvent1");
 eventHandler1.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler1)).thenReturn("testHandler1Value");
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(Maps.newHashMap(Arrays.asList(
 new AbstractMap.SimpleEntry<>("testHandler1", "testHandler1Value"),
 new AbstractMap.SimpleEntry<>("testHandler2", "testHandler2Value")
 )));

 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 assertEquals(2, eventHandlers.size());
 verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
 }

 @Test
 public void testGetEventHandlerForEvent() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testHandlerValue");
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn("testHandlerValue");
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Sets.newHashSet("testHandler"));

 EventHandler eventHandlerFromDAO = redisEventHandlerDAO.getEventHandler("testEvent", false);
 assertEquals(eventHandler, eventHandlerFromDAO);
 }

 @Test
 public void testGetEventHandlerForEventActiveOnly() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(false);
 when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testHandlerValue");
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testHandler"))).thenReturn("testHandlerValue");
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Sets.newHashSet("testHandler1", "testHandler2"));

 EventHandler eventHandlerFromDAO = redisEventHandlerDAO.getEventHandler("testEvent", true);
 assertNull(eventHandlerFromDAO);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerForEventNotFound() {
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), anyString())).thenReturn(null);
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(Sets.newHashSet());

 try {
 redisEventHandlerDAO.getEventHandler("testEvent", false);
 } catch (NotFoundException e) {
 // Expected
 }
 }
}