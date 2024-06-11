import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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
import com.netflix.conductor.redis.testutils.BaseDynoDAOTest;
import com.netflix.conductor.redis.testutils.MockObjectMapper;
import com.netflix.conductor.redis.testutils.MockRedisProperties;
import com.netflix.conductor.redis.testutils.MockSpringContext;
import com.netflix.conductor.testutils.TestJson;
import com.netflix.conductor.testutils.TestObjectMapper;
import com.netflix.conductor.testutils.TestRedisProperties;
import com.netflix.conductor.testutils.TestSpringContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@Import(RedisEventHandlerDAOTest.TestConfig.class)
public class RedisEventHandlerDAOTest {

private RedisEventHandlerDAO redisEventHandlerDAO;
private EventHandlerDAO eventHandlerDAO;
private JedisProxy jedisProxy;
private ObjectMapper objectMapper;
private ConductorProperties conductorProperties;
private RedisProperties redisProperties;
private TestSpringContext testSpringContext;

@Before
public void setup() {
redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
eventHandlerDAO = mock(EventHandlerDAO.class);
jedisProxy = mock(JedisProxy.class);
objectMapper = new TestObjectMapper();
conductorProperties = new TestConductorProperties();
redisProperties = new TestRedisProperties();
when(jedisProxy.hget(anyString(), anyString())).thenAnswer(invocation -> invocation.getArgument(1));
when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(true);
when(jedisProxy.sadd(anyString(), anyString())).thenReturn(true);
when(jedisProxy.srem(anyString(), anyString())).thenReturn(true);
when(jedisProxy.smembers(anyString())).thenAnswer(invocation -> invocation.getArgument(0).split(","));
when(jedisProxy.hgetAll(anyString())).thenAnswer(invocation -> invocation.getArgument(0).split("\n"));
when(jedisProxy.hgetAll(anyString())).thenAnswer(invocation -> invocation.getArgument(0).split("\n"));
when(eventHandlerDAO.getEventHandler(anyString())).thenAnswer(invocation -> getEventHandlerFromJson(invocation.getArgument(0), objectMapper));
when(eventHandlerDAO.addEventHandler(any())).thenAnswer(invocation -> addEventHandlerToRedis(redisEventHandlerDAO, invocation.getArgument(0)));
when(eventHandlerDAO.updateEventHandler(any())).thenAnswer(invocation -> updateEventHandlerInRedis(redisEventHandlerDAO, invocation.getArgument(0)));
when(eventHandlerDAO.removeEventHandler(anyString())).thenAnswer(invocation -> removeEventHandlerFromRedis(redisEventHandlerDAO, invocation.getArgument(0)));
when(eventHandlerDAO.getAllEventHandlers()).thenAnswer(invocation -> getAllEventHandlersFromRedis(redisEventHandlerDAO));
when(eventHandlerDAO.getEventHandlersForEvent(anyString(), anyBoolean())).thenAnswer(invocation -> getEventHandlersForEventFromRedis(redisEventHandlerDAO, invocation.getArgument(0), invocation.getArgument(1)));
testSpringContext = new TestSpringContext();
testSpringContext.registerBean(EventHandlerDAO.class, eventHandlerDAO);
testSpringContext.registerBean(JedisProxy.class, jedisProxy);
testSpringContext.registerBean(ObjectMapper.class, objectMapper);
testSpringContext.registerBean(ConductorProperties.class, conductorProperties);
testSpringContext.registerBean(RedisProperties.class, redisProperties);
testSpringContext.registerBean(RedisEventHandlerDAO.class, redisEventHandlerDAO);
}

// ... (Other test cases go here)

private static EventHandler getEventHandlerFromJson(String json, ObjectMapper objectMapper) {
return objectMapper.readValue(json, EventHandler.class);
}

private static void addEventHandlerToRedis(RedisEventHandlerDAO redisEventHandlerDAO, EventHandler eventHandler) {
redisEventHandlerDAO.addEventHandler(eventHandler);
}

private static void updateEventHandlerInRedis(RedisEventHandlerDAO redisEventHandlerDAO, EventHandler eventHandler) {
redisEventHandlerDAO.updateEventHandler(eventHandler);
}

private static void removeEventHandlerFromRedis(RedisEventHandlerDAO redisEventHandlerDAO, String name) {
redisEventHandlerDAO.removeEventHandler(name);
}

private static List<EventHandler> getAllEventHandlersFromRedis(RedisEventHandlerDAO redisEventHandlerDAO) {
return redisEventHandlerDAO.getAllEventHandlers();
}

private static List<EventHandler> getEventHandlersForEventFromRedis(RedisEventHandlerDAO redisEventHandlerDAO, String event, boolean activeOnly) {
return redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);
}

@Configuration
public static class TestConfig {

@Bean
public JedisProxy jedisProxy() {
return Mockito.mock(JedisProxy.class);
}

@Bean
public ObjectMapper objectMapper() {
return new MockObjectMapper();
}

@Bean
public ConductorProperties conductorProperties() {
return new TestConductorProperties();
}

@Bean
public RedisProperties redisProperties() {
return new TestRedisProperties();
}
}

class TestConductorProperties extends ConductorProperties {
}

class TestRedisProperties extends RedisProperties {
}
}