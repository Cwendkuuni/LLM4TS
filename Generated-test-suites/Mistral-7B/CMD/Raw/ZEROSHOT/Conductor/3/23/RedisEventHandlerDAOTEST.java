import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

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
import com.netflix.conductor.redis.tests.BaseRedisTest;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class RedisEventHandlerDAOTest extends BaseRedisTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private RedisProperties properties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("handler1"))).thenReturn("{}");
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq("handler2"))).thenReturn(null);
        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(new LinkedHashMap<String, String>() {{
            put("handler1", "{}");
            put("handler3", "{}");
        }});
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")))).thenReturn(new LinkedList<String>() {{
            add("handler1");
            add("handler3");
        }});
        when(jedisProxy.sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")), eq("handler4"))).thenReturn(1L);
        when(jedisProxy.srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "event1")), eq("handler1"))).thenReturn(1L);
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenAnswer(i -> mock(EventHandler.class));
        when(conductorProperties.getRedisConfig()).thenReturn(properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler1");
        eventHandler.setEvent("event1");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        Map<String, String> handlers = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(3, handlers.size());
        assertEquals("handler1", handlers.keySet().iterator().next());

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("handler1");
        assertNotNull(retrievedEventHandler);
        assertEquals("event1", retrievedEventHandler.getEvent());
    }

    @Test
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler1");
        eventHandler.setEvent("event1");

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            redisEventHandlerDAO.addEventHandler(eventHandler);
        } catch (ConflictException e) {
            assertEquals("EventHandler with name handler1 already exists!", e.getMessage());
        }

        Map<String, String> handlers = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(2, handlers.size());
        assertEquals("handler1", handlers.keySet().iterator().next());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("handler1");
        existingEventHandler.setEvent("event1");
        existingEventHandler.setActive(true);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("handler1");
        updatedEventHandler.setEvent("event2");
        updatedEventHandler.setActive(false);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Map<String, String> handlers = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(2, handlers.size());
        assertEquals("handler1", handlers.keySet().iterator().next());

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("handler1");
        assertNotNull(retrievedEventHandler);
        assertEquals("event2", retrievedEventHandler.getEvent());
        assertFalse(retrievedEventHandler.isActive());
    }

    @Test
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("handler3");
        updatedEventHandler.setEvent("event1");
        updatedEventHandler.setActive(true);

        try {
            redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name handler3 not found!", e.getMessage());
        }

        Map<String, String> handlers = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(2, handlers.size());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler1");
        eventHandler.setEvent("event1");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("handler1");

        Map<String, String> handlers = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(1, handlers.size());
        assertNull(handlers.get("handler1"));
    }

    @Test
    public void testRemoveEventHandlerWithNonExistentName() {
        try {
            redisEventHandlerDAO.removeEventHandler("handler3");
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name handler3 not found!", e.getMessage());
        }

        Map<String, String> handlers = jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("handler1");
        eventHandler1.setEvent("event1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("handler2");
        eventHandler2.setEvent("event1");
        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("handler3");
        eventHandler3.setEvent("event2");
        EventHandler eventHandler4 = new EventHandler();
        eventHandler4.setName("handler4");
        eventHandler4.setEvent("event2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        redisEventHandlerDAO.addEventHandler(eventHandler4);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(4, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));
        assertEquals(eventHandler4, eventHandlers.get(3));

        redisEventHandlerDAO.removeEventHandler("handler1");
        eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler2, eventHandlers.get(0));
        assertEquals(eventHandler3, eventHandlers.get(1));
        assertEquals(eventHandler4, eventHandlers.get(2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("handler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("handler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("handler3");
        eventHandler3.setEvent("event2");
        eventHandler3.setActive(true);

        EventHandler eventHandler4 = new EventHandler();
        eventHandler4.setName("handler4");
        eventHandler4.setEvent("event2");
        eventHandler4.setActive(false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        redisEventHandlerDAO.addEventHandler(eventHandler4);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event2", false);
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler3, eventHandlers.get(0));
        assertEquals(eventHandler4, eventHandlers.get(1));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event2", true);
        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler3, eventHandlers.get(0));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event3", false);
        assertEquals(0, eventHandlers.size());
    }
}