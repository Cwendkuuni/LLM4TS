import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RedisEventHandlerDAOTest {

 private JedisProxy jedisProxy;
 private ObjectMapper objectMapper;
 private ConductorProperties conductorProperties;
 private RedisProperties redisProperties;
 private EventHandlerDAO redisEventHandlerDAO;
 private static final String TEST_NAME = "testName";
 private static final String TEST_EVENT = "testEvent";
 private static final String TEST_EVENT_NAME = "testEventName";
 private static final String TEST_EVENT_NAME_2 = "testEventName2";
 private static final String TEST_JSON = "{\"name\":\"testName\",\"event\":\"testEvent\",\"active\":true}";

 @Before
 public void setup() {
 JedisProxy jedisProxyMock = mock(JedisProxy.class);
 ObjectMapper objectMapperMock = mock(ObjectMapper.class);
 ConductorProperties conductorPropertiesMock = mock(ConductorProperties.class);
 RedisProperties redisPropertiesMock = mock(RedisProperties.class);
 redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, objectMapperMock, conductorPropertiesMock, redisPropertiesMock);
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapperMock);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorPropertiesMock);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisPropertiesMock);
 }

 @Test
 public void testAddEventHandler() throws Exception {
 // Given
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(TEST_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 when(jedisProxyMock.hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME), Mockito.eq(TEST_JSON)))
 .thenReturn(true);
 // When
 redisEventHandlerDAO.addEventHandler(eventHandler);
 // Then
 verify(jedisProxyMock).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME), Mockito.eq(TEST_JSON));
 }

 @Test
 public void testAddEventHandlerConflict() throws Exception {
 // Given
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(TEST_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(TEST_NAME);
 existingEventHandler.setEvent(TEST_EVENT);
 existingEventHandler.setActive(true);
 when(jedisProxyMock.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME)))
 .thenReturn(TEST_JSON);
 // When & Then
 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
 }

 @Test
 public void testUpdateEventHandler() throws Exception {
 // Given
 EventHandler existingEventHandler = new EventHandler();
 existingEventHandler.setName(TEST_NAME);
 existingEventHandler.setEvent(TEST_EVENT);
 existingEventHandler.setActive(true);
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(TEST_NAME);
 updatedEventHandler.setEvent(TEST_EVENT_NAME);
 updatedEventHandler.setActive(true);
 when(jedisProxyMock.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME)))
 .thenReturn(TEST_JSON);
 when(objectMapperMock.readValue(Mockito.eq(TEST_JSON), Mockito.eq(EventHandler.class)))
 .thenReturn(existingEventHandler);
 when(jedisProxyMock.hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME), Mockito.eq(toJson(updatedEventHandler))))
 .thenReturn(true);
 // When
 redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
 // Then
 verify(jedisProxyMock).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME), Mockito.eq(toJson(updatedEventHandler)));
 }

 @Test
 public void testUpdateEventHandlerNotFound() throws Exception {
 // Given
 EventHandler updatedEventHandler = new EventHandler();
 updatedEventHandler.setName(TEST_NAME);
 updatedEventHandler.setEvent(TEST_EVENT_NAME);
 updatedEventHandler.setActive(true);
 when(jedisProxyMock.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME)))
 .thenReturn((String) null);
 // When & Then
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
 }

 @Test
 public void testRemoveEventHandler() throws Exception {
 // Given
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName(TEST_NAME);
 eventHandler.setEvent(TEST_EVENT);
 eventHandler.setActive(true);
 when(jedisProxyMock.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME)))
 .thenReturn(TEST_JSON);
 when(objectMapperMock.readValue(Mockito.eq(TEST_JSON), Mockito.eq(EventHandler.class)))
 .thenReturn(eventHandler);
 when(jedisProxyMock.hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME)))
 .thenReturn(1);
 // When
 redisEventHandlerDAO.removeEventHandler(TEST_NAME);
 // Then
 verify(jedisProxyMock).hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME));
 }

 @Test
 public void testRemoveEventHandlerNotFound() throws Exception {
 // Given
 when(jedisProxyMock.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_NAME)))
 .thenReturn((String) null);
 // When & Then
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(TEST_NAME));
 }

 @Test
 public void testGetAllEventHandlers() throws Exception {
 // Given
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(TEST_EVENT_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(TEST_EVENT_NAME_2);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(true);
 Map<String, String> redisMap = new LinkedHashMap<>();
 redisMap.put(TEST_EVENT_NAME, TEST_JSON);
 redisMap.put(TEST_EVENT_NAME_2, TEST_JSON);
 when(jedisProxyMock.hgetAll(Mockito.eq(nsKey(EVENT_HANDLERS))))
 .thenReturn(redisMap);
 // When
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
 // Then
 assertEquals(2, eventHandlers.size());
 for (EventHandler handler : eventHandlers) {
 assertTrue(handler instanceof EventHandler);
 assertSame(eventHandler1, eventHandlers.get(0));
 assertSame(eventHandler2, eventHandlers.get(1));
 }
 }

 @Test
 public void testGetEventHandlersForEvent() throws Exception {
 // Given
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(TEST_EVENT_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(TEST_EVENT_NAME_2);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(false);
 Map<String, Set<String>> redisSetMap = new LinkedHashMap<>();
 redisSetMap.put(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT), Lists.newLinkedHashSet(Arrays.asList(TEST_EVENT_NAME, TEST_EVENT_NAME_2)));
 when(jedisProxyMock.smembers(Mockito.eq(nsKey(EVENT_Handlers_BY_EVENT, TEST_EVENT))))
 .thenReturn(redisSetMap.get(nsKey(EVENT_Handlers_BY_EVENT, TEST_EVENT)).toArray(new String[0]));
 // When
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);
 // Then
 assertEquals(2, eventHandlers.size());
 for (EventHandler handler : eventHandlers) {
 assertTrue(handler instanceof EventHandler);
 assertSame(eventHandler1, eventHandlers.get(0));
 assertSame(eventHandler2, eventHandlers.get(1));
 }
 }

 @Test
 public void testGetEventHandlersForEventActiveOnly() throws Exception {
 // Given
 EventHandler eventHandler1 = new EventHandler();
 eventHandler1.setName(TEST_EVENT_NAME);
 eventHandler1.setEvent(TEST_EVENT);
 eventHandler1.setActive(true);
 EventHandler eventHandler2 = new EventHandler();
 eventHandler2.setName(TEST_EVENT_NAME_2);
 eventHandler2.setEvent(TEST_EVENT);
 eventHandler2.setActive(false);
 Map<String, Set<String>> redisSetMap = new LinkedHashMap<>();
 redisSetMap.put(nsKey(EVENT_Handlers_BY_EVENT, TEST_EVENT), Lists.newLinkedHashSet(Arrays.asList(TEST_EVENT_NAME, TEST_EVENT_NAME_2)));
 // When
 List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, true);
 // Then
 assertEquals(1, eventHandlers.size());
 assertNull(eventHandlers.get(0));
 }

 @Test
 public void testGetEventHandlerNotFound() throws Exception {
 // Given
 when(jedisProxyMock.hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(TEST_EVENT_NAME)))
 .thenReturn((String) null);
 // When & Then
 assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(TEST_EVENT_NAME));
 }

 private String nsKey(String... keys) {
 return String.join(":", redisProperties.getNamespace(), keys);
 }

 private String toJson(Object o) throws Exception {
 return objectMapper.writeValueAsString(o);
}
}