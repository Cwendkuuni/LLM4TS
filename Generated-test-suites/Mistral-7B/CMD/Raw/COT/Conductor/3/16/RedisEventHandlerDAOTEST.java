import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.netflix.conductor.redis.jedis.JedisProxyMock;
import com.netflix.conductor.redis.utils.JsonUtils;
import com.netflix.conductor.utils.PropertyPlaceholderUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
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

 @Autowired
 private PropertyPlaceholderUtils propertyPlaceholderUtils;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(jedisProxy.hgetAll(anyString())).thenReturn(Maps.newHashMap());
 when(jedisProxy.smembers(anyString())).thenReturn(new LinkedHashSet<>());
 when(objectMapper.readValue(anyString(), EventHandler.class)).thenAnswer(invocation -> null);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
 ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
 }

 @Bean
 public JedisProxyMock jedisProxyMock() {
 return mock(JedisProxyMock.class);
 }

 @Test
 public void testAddEventHandler() {
 // Given
 EventHandler eventHandler = createEventHandler();

 // When
 redisEventHandlerDAO.addEventHandler(eventHandler);

 // Then
 Map<String, String> expectedMap = Maps.newHashMap();
 expectedMap.put(eventHandler.getName(), JsonUtils.toJson(eventHandler));
 when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(expectedMap);
 assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
 }

 @Test(expected = ConflictException.class)
 public void testAddEventHandlerWithExistingEventHandler() {
 // Given
 String existingEventHandlerName = "existingEventHandler";
 EventHandler existingEventHandler = createEventHandler();
 when(jedisProxy.hget(nsKey(EVENT_HANDLERS), existingEventHandlerName)).thenReturn(JsonUtils.toJson(existingEventHandler));
 EventHandler newEventHandler = createEventHandler();

 // When - Then
 assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
 }

 // Other test cases for updateEventHandler, removeEventHandler, getAllEventHandlers, getEventHandlersForEvent methods can be generated similarly

 // Helper methods
 private EventHandler createEventHandler() {
 EventHandler eventHandler = new EventHandler();
 eventHandler.setName("testEventHandler");
 eventHandler.setEvent("testEvent");
 eventHandler.setActive(true);
 return eventHandler;
 }

 private String nsKey(String... keys) {
 return propertyPlaceholderUtils.placeholdersToProperties(conductorProperties).getProperty("redis.namespace") + ":"
 + String.join(":", keys);
 }
}