import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.netflix.conductor.redis.test.TestRedisProperties;
import com.netflix.conductor.test.TestEventHandler;
import com.netflix.conductor.test.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringJUnitConfig(classes = RedisEventHandlerDAOTest.TestConfig.class)
@ActiveProfiles("test")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Autowired
    private JedisProxy jedisProxy;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConductorProperties conductorProperties;

    @Autowired
    private RedisProperties redisProperties;

    private static final String TEST_EVENT = "testEvent";
    private static final String TEST_EVENT_HANDLER_NAME = "testEventHandler";
    private static final String TEST_EVENT_HANDLER_JSON = TestUtils.toJson(new TestEventHandler("testHandlerName", TEST_EVENT));

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
        given(jedisProxy.hget(anyString(), anyString())).willReturn(TEST_EVENT_HANDLER_JSON);
        given(jedisProxy.hset(anyString(), anyString(), anyString())).willAnswer(invocation -> {
            if (invocation.getArgument(0).equals(EVENT_HANDLERS)) {
                return "OK";
            }
            throw new UnsupportedOperationException();
        });
        given(jedisProxy.hdel(anyString(), anyString())).willAnswer(invocation -> {
            if (invocation.getArgument(0).equals(EVENT_HANDLERS)) {
                return "OK";
            }
            throw new UnsupportedOperationException();
        });
        given(jedisProxy.sadd(anyString(), anyString())).willAnswer(invocation -> {
            if (invocation.getArgument(0).startsWith(EVENT_HANDLERS_BY_EVENT)) {
                return "OK";
            }
            throw new UnsupportedOperationException();
        });
        given(jedisProxy.srem(anyString(), anyString())).willAnswer(invocation -> {
            if (invocation.getArgument(0).startsWith(EVENT_HANDLERS_BY_EVENT)) {
                return "OK";
            }
            throw new UnsupportedOperationException();
        });
        given(objectMapper.writeValueAsString(any())).willReturn(TEST_EVENT_HANDLER_JSON);
    }

    @Test
    public void testAddEventHandler() {
        // Given
        EventHandler eventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);

        // When
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Then
        given(jedisProxy.hset(anyString(), anyString(), anyString())).willReturn("OK");
        then(jedisProxy).should().hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), eq(TEST_EVENT_HANDLER_JSON));
        then(jedisProxy).should().sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), eq(TEST_EVENT_HANDLER_NAME));
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        // Given
        EventHandler existingEventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        // When
        EventHandler newEventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);

        // Then
        then(jedisProxy).should().hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), anyString());
        then(jedisProxy).shouldThrow(new UnsupportedOperationException()); // hset should have failed
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("addEventHandler");
        then(redisEventHandlerDAO).shouldThrow(new ConflictException("EventHandler with name testHandlerName already exists!"));
    }

    @Test
    public void testUpdateEventHandler() {
        // Given
        EventHandler existingEventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        String existingJson = TEST_EVENT_HANDLER_JSON;

        // When
        EventHandler updatedEventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);
        updatedEventHandler.setName("updatedEventHandlerName");

        // Then
        given(jedisProxy.hget(anyString(), anyString())).willReturn(existingJson);
        given(jedisProxy.hset(anyString(), anyString(), anyString())).willReturn("OK");
        then(jedisProxy).should().hset(eq(nsKey(EVENT_HANDLERS)), eq(TEST_EVENT_HANDLER_NAME), eq(toJson(updatedEventHandler)));
        then(jedisProxy).should().srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), eq(TEST_EVENT_HANDLER_NAME));
        then(jedisProxy).should().sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), eq("updatedEventHandlerName"));
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        // Given
        EventHandler updatedEventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);

        // When

        // Then
        given(jedisProxy.hget(anyString(), anyString())).willThrow(new UnsupportedOperationException());
        then(jedisProxy).shouldThrow(new UnsupportedOperationException()); // hget should have failed
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("updateEventHandler");
        then(redisEventHandlerDAO).shouldThrow(new NotFoundException("EventHandler with name testHandlerName not found!"));
    }

    @Test
    public void testRemoveEventHandler() {
        // Given
        EventHandler eventHandler = new TestEventHandler("testHandlerName", TEST_EVENT);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // When
        redisEventHandlerDAO.removeEventHandler("testHandlerName");

        // Then
        given(jedisProxy.hget(anyString(), anyString())).willThrow(new UnsupportedOperationException());
        given(jedisProxy.hdel(anyString(), anyString())).willReturn("OK");
        then(jedisProxy).should().hdel(eq(nsKey(EVENT_HANDLERS)), eq("testHandlerName"));
        then(jedisProxy).should().srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), eq("testHandlerName"));
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        // Given

        // When
        redisEventHandlerDAO.removeEventHandler("testHandlerName");

        // Then
        given(jedisProxy.hget(anyString(), anyString())).willThrow(new UnsupportedOperationException());
        then(jedisProxy).shouldThrow(new UnsupportedOperationException()); // hget should have failed
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("removeEventHandler");
        then(redisEventHandlerDAO).shouldThrow(new NotFoundException("EventHandler with name testHandlerName not found!"));
    }

    @Test
    public void testGetAllEventHandlers() {
        // Given
        EventHandler handler1 = new TestEventHandler("testHandlerName1", TEST_EVENT);
        EventHandler handler2 = new TestEventHandler("testHandlerName2", TEST_EVENT);
        given(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).willReturn(Maps.newHashMap(
                Map.of(TEST_EVENT_HANDLER_NAME, TEST_EVENT_HANDLER_JSON), Map.of("testHandlerName2", TEST_EVENT_HANDLER_JSON)
        ));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Then
        then(jedisProxy).should().hgetAll(eq(nsKey(EVENT_HANDLERS)));
        then(objectMapper).should().readValue(anyString(), eq(EventHandler.class), any());
        then(redisEventHandlerDAO).should().recordRedisDaoRequests("getAllEventHandlers");
        assertThat(eventHandlers).containsExactlyInAnyOrder(handler1, handler2);
    }

    @Test
    public void testGetEventHandlersForEventWithMultipleHandlers() {
        // Given
        EventHandler handler1 = new TestEventHandler("testHandlerName1", TEST_EVENT);
        EventHandler handler2 = new TestEventHandler("testHandlerName2", TEST_EVENT);
        given(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)))).willReturn(Collections.singletonList("testHandlerName1"));
        given(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)))).willReturn(Lists.newArrayList("testHandlerName1", "testHandlerName2"));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);

        // Then
        then(jedisProxy).should().smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), any());
        then(jedisProxy).should().smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), any());
        then(redisEventHandlerDAO).should().recordRedisDaoEventRequests("getEventHandler", TEST_EVENT);
        assertThat(eventHandlers).containsExactlyInAnyOrder(handler1, handler2);
    }

    @Test
    public void testGetEventHandlersForEventWithSingleHandler() {
        // Given
        EventHandler handler = new TestEventHandler("testHandlerName", TEST_EVENT);
        given(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)))).willReturn(Collections.singletonList("testHandlerName"));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);

        // Then
        then(jedisProxy).should().smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), any());
        then(redisEventHandlerDAO).should().recordRedisDaoEventRequests("getEventHandler", TEST_EVENT);
        assertThat(eventHandlers).containsExactly(handler);
    }

    @Test
    public void testGetEventHandlersForEventWithNoHandlers() {
        // Given
        given(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)))).willReturn(Collections.emptyList());

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, false);

        // Then
        then(jedisProxy).should().smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), any());
        then(redisEventHandlerDAO).should().recordRedisDaoEventRequests("getEventHandler", TEST_EVENT);
        assertThat(eventHandlers).isEmpty();
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        // Given
        EventHandler handler1 = new TestEventHandler("testHandlerName1", TEST_EVENT, false);
        EventHandler handler2 = new TestEventHandler("testHandlerName2", TEST_EVENT, true);
        given(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)))).willReturn(Lists.newArrayList("testHandlerName1", "testHandlerName2"));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT, true);

        // Then
        then(jedisProxy).should().smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, TEST_EVENT)), any());
        then(redisEventHandlerDAO).should().recordRedisDaoEventRequests("getEventHandler", TEST_EVENT);
        assertThat(eventHandlers).containsExactly(handler2);
    }

    @Configuration
    @Import(RedisEventHandlerDAO.class)
    static class TestConfig {
        @Bean
        public JedisProxy jedisProxy() {
            return new JedisProxy();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public ConductorProperties conductorProperties() {
            return new ConductorProperties();
        }

        @Bean
        public RedisProperties redisProperties() {
            return new TestRedisProperties();
        }
    }
}