import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisPropertiesTest;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

 private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
 private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

 @Mock
 private JedisProxy jedisProxy;
 @Mock
 private ConductorProperties conductorProperties;
 @Mock
 private RedisProperties redisProperties;
 @Mock
 private ObjectMapper objectMapper;
 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 doReturn(redisProperties).when(conductorProperties).getProperties(RedisProperties.class);
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setClassName("testClassName");
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq("testEventHandler"), anyString());
 verify(jedisProxy, times(1)).sadd(anyString(), eq("testEventHandler"));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithConflict() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setClassName("testClassName");
 existingEventHandler.setActive(true);
 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEvent");
 newEventHandler.setClassName("testClassName");
 newEventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(newEventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq("testEventHandler"), anyString());
 verify(jedisProxy, times(1)).hset(anyString(), eq("testEventHandler"), anyString()); // should not call hset again
 }

 @Test
 public void testGetEventHandler() {
 EventHandler expectedEventHandler = new EventHandler();
 expectedEventHandler.setName("testEventHandler");
 expectedEventHandler.setEvent("testEvent");
 expectedEventHandler.setClassName("testClassName");
 expectedEventHandler.setActive(true);

 String json = objectMapper.writeValueAsString(expectedEventHandler);
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testEventHandler"))).thenReturn(json);

 EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");

 assertEquals(expectedEventHandler, actualEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerNotFound() {
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("nonExistentEventHandler"))).thenReturn(null);

 try {
 redisEventHandlerDAO.getEventHandler("nonExistentEventHandler");
 fail("Expected NotFoundException");
 } catch (NotFoundException e) {
 // expected
 }
 }

 @Test
 public void testGetAllEventHandlers() {
 Map<String, String> jsonMap = Maps.newHashMap();
 jsonMap.put("testEventHandler1", "testHandler1Json");
 jsonMap.put("testEventHandler2", "testHandler2Json");
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(jsonMap);

 List<EventHandler> actualHandlers = redisEventHandlerDAO.getAllEventHandlers();
 List<EventHandler> expectedHandlers = Lists.newArrayList(new EventHandler(), new EventHandler());

 assertEquals(expectedHandlers, actualHandlers);
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName("testEventHandler");
 existingEventHandler.setEvent("testEvent");
 existingEventHandler.setClassName("testClassName");
 existingEventHandler.setActive(true);
 String existingEventHandlerJson = objectMapper.writeValueAsString(existingEventHandler);

 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testEventHandler"))).thenReturn(existingEventHandlerJson);

 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEventUpdated");
 newEventHandler.setClassName("testClassNameUpdated");
 newEventHandler.setActive(true);

 redisEventHandlerDAO.updateEventHandler(newEventHandler);

 verify(jedisProxy, times(1)).hset(anyString(), eq("testEventHandler"), anyString());
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerNotFound() {
 EventHandler newEventHandler = new EventHandler();
 newEventHandler.setName("testEventHandler");
 newEventHandler.setEvent("testEventUpdated");
 newEventHandler.setClassName("testClassNameUpdated");
 newEventHandler.setActive(true);

 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("testEventHandler"))).thenReturn(null);

 try {
 redisEventHandlerDAO.updateEventHandler(newEventHandler);
 fail("Expected NotFoundException");
 } catch (NotFoundException e) {
 // expected
 }
 }

 @Test
 public void testRemoveEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setClassName("testClassName");
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 redisEventHandlerDAO.removeEventHandler("testEventHandler");

 verify(jedisProxy, times(1)).hdel(anyString(), eq("testEventHandler"));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerNotFound() {
 redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");

 verify(jedisProxy, never()).hdel(anyString(), anyString());
 }

 @Test
 public void testGetEventHandlersForEvent() {
 EventHandler handler1 = new EventHandler();
 handler1.setName("testEventHandler1");
 handler1.setEvent("testEvent");
 handler1.setClassName("testClassName1");
 handler1.setActive(true);

 EventHandler handler2 = new EventHandler();
 handler2.setName("testEventHandler2");
 handler2.setEvent("testEvent");
 handler2.setClassName("testClassName2");
 handler2.setActive(false);

 Set<String> names = Maps.newHashSet("testEventHandler1", "testEventHandler2");
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(names);

 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
 List<EventHandler> expectedHandlers = Lists.newArrayList(handler1, handler2);

 assertEquals(expectedHandlers, actualHandlers);
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() {
 EventHandler handler1 = new EventHandler();
 handler1.setName("testEventHandler1");
 handler1.setEvent("testEvent");
 handler1.setClassName("testClassName1");
 handler1.setActive(true);

 EventHandler handler2 = new EventHandler();
 handler2.setName("testEventHandler2");
 handler2.setEvent("testEvent");
 handler2.setClassName("testClassName2");
 handler2.setActive(false);

 Set<String> names = Maps.newHashSet("testEventHandler1");
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(names);

 List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
 List<EventHandler> expectedHandlers = Lists.newArrayList(handler1);

 assertEquals(expectedHandlers, actualHandlers);
 }
}