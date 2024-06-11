import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(classes = RedisEventHandlerDAOTest.TestConfig.class)
@ActiveProfiles("test")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private Environment environment;
    private ResourceLoader resourceLoader;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private EventHandler eventHandler1;
    private EventHandler eventHandler2;
    private EventHandler eventHandler3;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        environment = mock(Environment.class);
        resourceLoader = mock(ResourceLoader.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        doReturn(jedisProxy).when(redisProperties).jedisProxy();
        doReturn(objectMapper).when(conductorProperties).objectMapper();
        doReturn(environment).when(conductorProperties).environment();
        doReturn(resourceLoader).when(conductorProperties).resourceLoader();

        eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        eventHandler3 = new EventHandler();
        eventHandler3.setName("eventHandler3");
        eventHandler3.setEvent("event2");
        eventHandler3.setActive(true);
    }

    @Configuration
    @PropertySource("classpath:test-application.properties")
    static class TestConfig {
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
            return spy(ConductorProperties.class);
        }

        @Bean
        public RedisProperties redisProperties() {
            return spy(RedisProperties.class);
        }
    }

    @Test
    public void testAddEventHandler() {
        // Given
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        // Then
        verify(jedisProxy).hset(any(), any(), any());
        verify(objectMapper).writeValueAsString(eventHandler1);
        assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() {
        // Given
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        // When
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        // Then
        verify(jedisProxy, Mockito.never()).hset(any(), any(), any());
        verify(objectMapper, Mockito.never()).writeValueAsString(eventHandler1);
        assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandler() {
        // Given
        String name = "eventHandler1";
        doReturn(eventHandler1.toString()).when(objectMapper).writeValueAsString(eventHandler1);
        doReturn(eventHandler1.toString()).when(jedisProxy).hget(eq(nsKey(EVENT_HANDLERS)), eq(name));

        // When
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(name);

        // Then
        verify(jedisProxy).hget(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verify(objectMapper).readValue(anyString(), eq(EventHandler.class));
        assertEquals(eventHandler1, eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        // Given
        String name = "eventHandler4";

        // When
        redisEventHandlerDAO.getEventHandler(name);

        // Then
        verify(jedisProxy, Mockito.never()).hget(any(), any());
        verify(objectMapper, Mockito.never()).readValue(anyString(), any());
        assertNull(redisEventHandlerDAO.getEventHandler(name));
    }

    @Test
    public void testUpdateEventHandler() {
        // Given
        String name = "eventHandler1";

        // When
        redisEventHandlerDAO.updateEventHandler(eventHandler1);

        // Then
        verify(jedisProxy).hset(any(), any(), any());
        verify(objectMapper).writeValueAsString(eventHandler1);
        assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        // Given
        String name = "eventHandler4";

        // When
        redisEventHandlerDAO.updateEventHandler(eventHandler1);

        // Then
        verify(jedisProxy, Mockito.never()).hset(any(), any(), any());
        verify(objectMapper, Mockito.never()).writeValueAsString(eventHandler1);
        assertNull(redisEventHandlerDAO.getEventHandler(name));
    }

    @Test
    public void testRemoveEventHandler() {
        // Given
        String name = "eventHandler1";

        // When
        redisEventHandlerDAO.removeEventHandler(name);

        // Then
        verify(jedisProxy).hdel(any(), eq(name));
        assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        // Given
        String name = "eventHandler4";

        // When
        redisEventHandlerDAO.removeEventHandler(name);

        // Then
        verify(jedisProxy, Mockito.never()).hdel(any(), any());
        assertNull(redisEventHandlerDAO.getEventHandler(name));
    }

    @Test
    public void testGetAllEventHandlers() {
        // Given
        doReturn(Maps.newHashMap(Maps.entry("eventHandler1", eventHandler1.toString()),
                Maps.entry("eventHandler2", eventHandler2.toString()),
                Maps.entry("eventHandler3", eventHandler3.toString()))).when(jedisProxy)
                .hgetAll(eq(nsKey(EVENT_HANDLERS)));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Then
        verify(jedisProxy).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        verify(objectMapper, Mockito.atLeast(3)).readValue(anyString(), eq(EventHandler.class));
        assertEquals(3, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
        assertTrue(eventHandlers.contains(eventHandler3));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Given
        doReturn(Sets.newHashSet("eventHandler1", "eventHandler2")).when(jedisProxy)
                .smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        // Then
        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")));
        verify(objectMapper, Mockito.atLeast(2)).readValue(anyString(), eq(EventHandler.class));
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        // Given
        doReturn(Sets.newHashSet("eventHandler1")).when(jedisProxy)
                .smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        // Then
        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")));
        verify(objectMapper, Mockito.atLeast(1)).readValue(anyString(), eq(EventHandler.class));
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventNotFound() {
        // Given
        doReturn(Sets.newHashSet()).when(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event2")));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event2", false);

        // Then
        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event2")));
        verify(objectMapper, Mockito.never()).readValue(anyString(), eq(EventHandler.class));
        assertEquals(0, eventHandlers.size());
    }
}