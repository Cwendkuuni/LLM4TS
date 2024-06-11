import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.TestRedisProperties;
import com.netflix.conductor.testutils.JacksonTestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class RedisEventHandlerDAOTestConfig {

    @Bean
    public JedisProxy jedisProxy() {
        return mock(JedisProxy.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return mock(ObjectMapper.class);
    }

    @Bean
    public ConductorProperties conductorProperties() {
        return mock(ConductorProperties.class);
    }

    @Bean
    public RedisProperties redisProperties() {
        return new TestRedisProperties();
    }

    @Bean
    @InjectMocks
    public RedisEventHandlerDAO redisEventHandlerDAO() {
        MockitoAnnotations.initMocks(this);
        return new RedisEventHandlerDAO(jedisProxy(), objectMapper(), conductorProperties(), redisProperties());
    }
}

public class RedisEventHandlerDAOTest {

    @Before
    public void setup() {
        // Initialize mocks
    }

    @Test
    public void testAddEventHandler_ValidEventHandler_Success() {
        // Arrange
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setClassName("TestEventHandlerClass");
        eventHandler.setConfig(Map.of("param1", "value1"));
        eventHandler.setActive(true);

        // Act
        redisEventHandlerDAO().addEventHandler(eventHandler);

        // Assert
        assertThat(eventHandler, is(notNullValue()));
        assertHandlerAddedToRedis(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_EventHandlerAlreadyExists_Conflict() {
        // Arrange
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setClassName("TestEventHandlerClass");
        existingEventHandler.setConfig(Map.of("param1", "value1"));
        existingEventHandler.setActive(true);
        redisEventHandlerDAO().addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEvent");
        newEventHandler.setClassName("TestEventHandlerClass");
        newEventHandler.setConfig(Map.of("param1", "value1"));
        newEventHandler.setActive(true);

        // Act & Assert
        redisEventHandlerDAO().addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler_ValidEventHandler_Success() {
        // Arrange
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setClassName("TestEventHandlerClass");
        existingEventHandler.setConfig(Map.of("param1", "value1"));
        existingEventHandler.setActive(true);
        redisEventHandlerDAO().addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("testEventUpdated");
        updatedEventHandler.setClassName("TestEventHandlerClassUpdated");
        updatedEventHandler.setConfig(Map.of("param1", "value2"));
        updatedEventHandler.setActive(true);

        // Act
        redisEventHandlerDAO().updateEventHandler(updatedEventHandler);

        // Assert
        assertHandlerUpdatedInRedis(updatedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NonExistentEventHandler_NotFound() {
        // Arrange
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandlerUpdated");
        updatedEventHandler.setEvent("testEventUpdated");
        updatedEventHandler.setClassName("TestEventHandlerClassUpdated");
        updatedEventHandler.setConfig(Map.of("param1", "value2"));
        updatedEventHandler.setActive(true);

        // Act & Assert
        redisEventHandlerDAO().updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler_ValidEventHandler_Success() {
        // Arrange
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setClassName("TestEventHandlerClass");
        eventHandler.setConfig(Map.of("param1", "value1"));
        eventHandler.setActive(true);
        redisEventHandlerDAO().addEventHandler(eventHandler);

        // Act
        redisEventHandlerDAO().removeEventHandler(eventHandler.getName());

        // Assert
        assertHandlerRemovedFromRedis(eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NonExistentEventHandler_NotFound() {
        // Act & Assert
        redisEventHandlerDAO().removeEventHandler("nonExistentEventHandlerName");
    }

    @Test
    public void testGetAllEventHandlers_EmptyRedis_EmptyList() {
        // Act
        List<EventHandler> allEventHandlers = redisEventHandlerDAO().getAllEventHandlers();

        // Assert
        assertThat(allEventHandlers, is(notNullValue()));
        assertThat(allEventHandlers, is(empty()));
    }

    @Test
    public void testGetAllEventHandlers_MultipleEventHandlersInRedis_ListWithEventHandlers() {
        // Arrange
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setClassName("EventHandler1Class");
        eventHandler1.setConfig(Map.of("param1", "value1"));
        eventHandler1.setActive(true);
        redisEventHandlerDAO().addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event2");
        eventHandler2.setClassName("EventHandler2Class");
        eventHandler2.setConfig(Map.of("param1", "value2"));
        eventHandler2.setActive(true);
        redisEventHandlerDAO().addEventHandler(eventHandler2);

        // Act
        List<EventHandler> allEventHandlers = redisEventHandlerDAO().getAllEventHandlers();

        // Assert
        assertThat(allEventHandlers, is(notNullValue()));
        assertThat(allEventHandlers, is(notEmpty()));
        assertThat(allEventHandlers, hasSize(2));
        assertThat(allEventHandlers, contains(eventHandler1, eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent_EmptyRedis_EmptyList() {
        // Arrange
        String event = "testEvent";

        // Act
        List<EventHandler> eventHandlers = redisEventHandlerDAO().getEventHandlersForEvent(event, false);

        // Assert
        assertThat(eventHandlers, is(notNullValue()));
        assertThat(eventHandlers, is(empty()));
    }

    @Test
    public void testGetEventHandlersForEvent_MultipleEventHandlersForEventInRedis_ListWithEventHandlers() {
        // Arrange
        String event = "testEvent";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(event);
        eventHandler1.setClassName("EventHandler1Class");
        eventHandler1.setConfig(Map.of("param1", "value1"));
        eventHandler1.setActive(true);
        redisEventHandlerDAO().addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(event);
        eventHandler2.setClassName("EventHandler2Class");
        eventHandler2.setConfig(Map.of("param1", "value2"));
        eventHandler2.setActive(true);
        redisEventHandlerDAO().addEventHandler(eventHandler2);

        // Act
        List<EventHandler> eventHandlers = redisEventHandlerDAO().getEventHandlersForEvent(event, false);

        // Assert
        assertThat(eventHandlers, is(notNullValue()));
        assertThat(eventHandlers, is(notEmpty()));
        assertThat(eventHandlers, hasSize(2));
        assertThat(eventHandlers, contains(eventHandler1, eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent_MultipleEventHandlersForEventInRedisWithActiveFilter_ListWithActiveEventHandlers() {
        // Arrange
        String event = "testEvent";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(event);
        eventHandler1.setClassName("EventHandler1Class");
        eventHandler1.setConfig(Map.of("param1", "value1"));
        eventHandler1.setActive(false);
        redisEventHandlerDAO().addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(event);
        eventHandler2.setClassName("EventHandler2Class");
        eventHandler2.setConfig(Map.of("param1", "value2"));
        eventHandler2.setActive(true);
        redisEventHandlerDAO().addEventHandler(eventHandler2);

        // Act
        List<EventHandler> eventHandlers = redisEventHandlerDAO().getEventHandlersForEvent(event, true);

        // Assert
        assertThat(eventHandlers, is(notNullValue()));
        assertThat(eventHandlers, is(notEmpty()));
        assertThat(eventHandlers, hasSize(1));
        assertThat(eventHandlers, contains(eventHandler2));
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandler_ExceptionInGetHandlerFromRedis_TransientException() {
        // Arrange
        String handlerName = "testEventHandler";

        doThrow(new RuntimeException("Redis Exception"))
                .when(jedisProxy())
                .hget(anyString(), eq(handlerName));

        // Act & Assert
        redisEventHandlerDAO().getEventHandler(handlerName);
    }

    // Add test cases for edge cases and other scenarios as needed

    private void assertHandlerAddedToRedis(EventHandler eventHandler) {
        String key = nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent());
        Set<String> names = jedisProxy().smembers(key);
        assertThat(names, hasItem(eventHandler.getName()));
    }

    private void assertHandlerUpdatedInRedis(EventHandler updatedEventHandler) {
        String key = nsKey(EVENT_HANDLERS);
        String json = toJson(updatedEventHandler);
        assertThat(jedisProxy().hget(key, updatedEventHandler.getName()), is(json));
    }

    private void assertHandlerRemovedFromRedis(String name) {
        String key = nsKey(EVENT_HANDLERS);
        String json = jedisProxy().hget(key, name);
        assertThat(json, is(nullValue()));

        Set<String> names = jedisProxy().smembers(nsKey(EVENT_HANDLERS_BY_EVENT, updatedEventHandler.getEvent()));
        assertThat(names, not(hasItem(name)));
    }

    private String nsKey(String... keys) {
        return String.join(":", Lists.asList(ConductorProperties.NAMESPACE, keys));
    }

    private String toJson(EventHandler eventHandler) {
        return JacksonTestUtils.mapper().writeValueAsString(eventHandler);
    }
}