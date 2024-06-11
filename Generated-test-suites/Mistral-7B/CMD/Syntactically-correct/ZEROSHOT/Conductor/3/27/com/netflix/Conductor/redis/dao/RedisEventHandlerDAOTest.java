import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

 private static final String TEST_EVENT_NAME = "testEventName";
 private static final String TEST_EVENT_HANDLER_NAME = "testEventHandlerName";
 private static final String TEST_EVENT_HANDLER_JSON = "{\"name\":\"testEventHandlerName\",\"event\":\"testEventName\",\"active\":true}";

 @Mock
 private JedisProxy jedisProxy;
 @Mock
 private ObjectMapper objectMapper;
 @Mock
 private ConductorProperties conductorProperties;
 @Mock
 private RedisProperties redisProperties;

 @InjectMocks
 private RedisEventHandlerDAO redisEventHandlerDAO;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(redisProperties.getNamespaceKey()).thenReturn("testNamespaceKey");
 when(conductorProperties.redisPrefix()).thenReturn("testRedisPrefix");
 }

 @Test
 public void testAddEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(TEST_EVENT_HANDLER_NAME);
 eventHandler.setEvent(TEST_EVENT_NAME);
 eventHandler.setActive(true);

 redisEventHandlerDAO.addEventHandler(eventHandler);

 verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), eq(TEST_EVENT_HANDLER_JSON));
 verify(objectMapper, times(1)).writeValueAsString(eventHandler);
 verify(redisProperties, times(1)).getNamespaceKey();
 verify(conductorProperties, times(1)).redisPrefix();
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(TEST_EVENT_HANDLER_NAME);
 existingEventHandler.setEvent(TEST_EVENT_NAME);
 existingEventHandler.setActive(true);
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME))).thenReturn(TEST_EVENT_HANDLER_JSON);

 redisEventHandlerDAO.addEventHandler(existingEventHandler);

 verify(jedisProxy, never()).hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), anyString());
 }

 @Test
 public void testGetEventHandler() {
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME))).thenReturn(TEST_EVENT_HANDLER_JSON);

 EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER_NAME);

 assertEquals(TEST_EVENT_HANDLER_NAME, eventHandler.getName());
 assertEquals(TEST_EVENT_NAME, eventHandler.getEvent());
 assertTrue(eventHandler.isActive());

 verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME));
 verify(objectMapper, times(1)).readValue(eq(TEST_EVENT_HANDLER_JSON), eq(EventHandler.class));
 }

 @Test(expected = NotFoundException.class)
 public void testGetEventHandlerWithNonExistentEventHandler() {
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME))).thenReturn(null);

 redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER_NAME);

 verify(jedisProxy, times(1)).hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME));
 }

 @Test
 public void testUpdateEventHandler() {
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(TEST_EVENT_HANDLER_NAME);
 existingEventHandler.setEvent(TEST_EVENT_NAME);
 existingEventHandler.setActive(true);
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(TEST_EVENT_HANDLER_NAME);
 updatedEventHandler.setEvent(TEST_EVENT_NAME + "_updated");
 updatedEventHandler.setActive(false);

 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME))).thenReturn(TEST_EVENT_HANDLER_JSON);
 when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn("updatedEventHandlerJSON");

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), eq("updatedEventHandlerJSON"));
 verify(objectMapper, times(1)).writeValueAsString(updatedEventHandler);
 }

 @Test(expected = NotFoundException.class)
 public void testUpdateEventHandlerWithNonExistentEventHandler() {
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(TEST_EVENT_HANDLER_NAME + "_updated");
 updatedEventHandler.setEvent(TEST_EVENT_NAME);
 updatedEventHandler.setActive(false);

 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

 verify(jedisProxy, never()).hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), anyString());
 }

 @Test
 public void testRemoveEventHandler() {
 when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME))).thenReturn(TEST_EVENT_HANDLER_JSON);

 redisEventHandlerDAO.removeEventHandler(TEST_EVENT_HANDLER_NAME);

 verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME));
 }

 @Test(expected = NotFoundException.class)
 public void testRemoveEventHandlerWithNonExistentEventHandler() {
 redisEventHandlerDAO.removeEventHandler(TEST_EVENT_HANDLER_NAME);

 verify(jedisProxy, never()).hdel(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME));
 }

 @Test
 public void testGetAllEventHandlers() {
 Map<String, String> all = Maps.newLinkedHashMap();
 all.put(TEST_EVENT_HANDLER_NAME, TEST_EVENT_HANDLER_JSON);
 all.put("anotherEventHandlerName", "anotherEventHandlerJSON");
 when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(all);

 List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

 assertEquals(2, handlers.size());
 assertEquals(TEST_EVENT_HANDLER_NAME, handlers.get(0).getName());
 assertEquals(TEST_EVENT_NAME, handlers.get(0).getEvent());
 assertTrue(handlers.get(0).isActive());
 assertEquals("anotherEventHandlerName", handlers.get(1).getName());
 // ... other assertions for the second handler

 verify(jedisProxy, times(1)).hgetAll(eq(nsKey(EVENT_HANDLERS)));
 }

 @Test
 public void testGetEventHandlersForEvent() {
 Set<String> handlers = Sets.newLinkedHashSet(TEST_EVENT_HANDLER_NAME, "anotherEventHandlerName");
 String event = TEST_EVENT_NAME;
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(handlers.toArray(new String[0]));

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

 assertEquals(2, handlers.size());
 assertEquals(TEST_EVENT_HANDLER_NAME, handlers.get(0).getName());
 assertEquals(TEST_EVENT_NAME, handlers.get(0).getEvent());
 assertTrue(handlers.get(0).isActive());
 assertEquals("anotherEventHandlerName", handlers.get(1).getName());
 // ... other assertions for the second handler

 verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveOnlyTrue() {
 Set<String> handlers = Sets.newLinkedHashSet(TEST_EVENT_HANDLER_NAME, "anotherEventHandlerName");
 String event = TEST_EVENT_NAME;
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(handlers.toArray(new String[0]));

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

 assertEquals(1, handlers.size());
 assertEquals(TEST_EVENT_HANDLER_NAME, handlers.get(0).getName());
 assertEquals(TEST_EVENT_NAME, handlers.get(0).getEvent());
 assertTrue(handlers.get(0).isActive());

 verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
 }

 @Test
 public void testGetEventHandlersForEventWithActiveOnlyFalse() {
 Set<String> handlers = Sets.newLinkedHashSet(TEST_EVENT_HANDLER_NAME, "anotherEventHandlerName");
 String event = TEST_EVENT_NAME;
 when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(handlers.toArray(new String[0]));

 List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

 assertEquals(2, handlers.size());
 assertEquals(TEST_EVENT_HANDLER_NAME, handlers.get(0).getName());
 assertEquals(TEST_EVENT_NAME, handlers.get(0).getEvent());
 assertTrue(handlers.get(0).isActive());
 assertEquals("anotherEventHandlerName", handlers.get(1).getName());
 // ... other assertions for the second handler

 verify(jedisProxy, times(1)).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
 }

 private String nsKey(String... keys) {
 return StringUtils.join(Arrays.asList(getNamespaceKey(), Arrays.asList(keys)), ":");
 }

 private String getNamespaceKey() {
 return "testNamespaceKey";
 }
}