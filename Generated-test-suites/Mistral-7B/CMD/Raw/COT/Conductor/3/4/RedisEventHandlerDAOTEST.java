import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseRedisTest;
import com.netflix.conductor.testutils.JsonTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Configuration
@Import(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ObjectMapper objectMapper;

    private BaseRedisTest redisTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisTest = new BaseRedisTest(jedisProxy, conductorProperties, redisProperties, objectMapper);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    // Add test cases for each method in the class below

    // Test addEventHandler method
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq(nsKey(EVENT_HANDLERS)), eq("testEventHandler"), anyString());
    }

    // Test getAllEventHandlers method
    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event2");

        Map<String, String> all = Map.of("eventHandler1", toJson(eventHandler1), "eventHandler2", toJson(eventHandler2));

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    // Test getEventHandlerForEvent method with activeOnly = false
    @Test
    public void testGetEventHandlerForEventWithActiveOnlyFalse() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        String event = "event1";

        String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
        Set<String> names = Set.of("eventHandler1", "eventHandler2");

        when(jedisProxy.smembers(key)).thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    // Test getEventHandlerForEvent method with activeOnly = true
    @Test
    public void testGetEventHandlerForEventWithActiveOnlyTrue() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        String event = "event1";

        String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
        Set<String> names = Set.of("eventHandler1", "eventHandler2");

        when(jedisProxy.smembers(key)).thenReturn(names);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    // Test updateEventHandler method
    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existingEventHandler");
        existingEventHandler.setEvent("existingEvent");

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("updatedEventHandler");
        updatedEventHandler.setEvent("existingEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "existingEventHandler")).thenReturn(toJson(existingEventHandler));
        when(jedisProxy.hset(nsKey(EVENT_HANDLERS), "existingEventHandler", anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1))
                .hset(eq(nsKey(EVENT_HANDLERS)), eq("existingEventHandler"), anyString());
    }

    // Test removeEventHandler method
    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(toJson(eventHandler));
        when(jedisProxy.hdel(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(jedisProxy, times(1)).hdel(eq(nsKey(EVENT_HANDLERS)), eq("testEventHandler"));
    }

    // Test exception handling scenarios
    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler")).thenReturn(toJson(new EventHandler()));

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNotFoundException() {
        String name = "testEventHandler";

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenThrow(new NotFoundException(""));

        redisEventHandlerDAO.getEventHandler(name);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerWithTransientException() {
        String name = "testEventHandler";

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler(name);
    }
}