import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Conditional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);

    private static final String TEST_EVENT_NAME = "testEvent";
    private static final String TEST_EVENT_HANDLER_NAME = "testEventHandler";
    private static final String TEST_EVENT_HANDLER_JSON = "{\"name\": \"testEventHandler\", \"event\": \"testEvent\", \"active\": true}";

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
        when(conductorProperties.getRedisNamespace()).thenReturn("testNamespace");
        when(properties.getRedisNamespace()).thenReturn("testNamespace");
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenAnswer(i -> TEST_EVENT_HANDLER_JSON);
        when(objectMapper.readValue(anyString(), EventHandler.class)).thenAnswer(s -> JsonUtils.parse(s, EventHandler.class));
        when(jedisProxy.hget(anyString(), anyString())).thenAnswer(i -> TEST_EVENT_HANDLER_JSON);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);
        when(jedisProxy.hgetAll(anyString())).thenReturn(Maps.newHashMap());
        when(jedisProxy.sadd(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.smembers(anyString())).thenReturn(Sets.newHashSet(TEST_EVENT_HANDLER_NAME));
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(TEST_EVENT_HANDLER_NAME);
        eventHandler.setEvent(TEST_EVENT_NAME);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertTrue(jedisProxy.exists(nsKey(EVENT_HANDLERS, TEST_EVENT_HANDLER_NAME)));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExists() throws Exception {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(TEST_EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(TEST_EVENT_NAME);
        existingEventHandler.setActive(true);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(TEST_EVENT_HANDLER_NAME);
        newEventHandler.setEvent(TEST_EVENT_NAME);
        newEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testGetEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(TEST_EVENT_HANDLER_NAME);
        eventHandler.setEvent(TEST_EVENT_NAME);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER_NAME);

        assertSame(eventHandler, returnedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() throws Exception {
        String nonExistentEventHandlerName = "nonExistentEventHandler";

        redisEventHandlerDAO.getEventHandler(nonExistentEventHandlerName);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(TEST_EVENT_HANDLER_NAME);
        eventHandler.setEvent(TEST_EVENT_NAME);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(TEST_EVENT_HANDLER_NAME);
        updatedEventHandler.setEvent("testEventUpdated");
        updatedEventHandler.setActive(false);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        EventHandler returnedEventHandler = redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER_NAME);

        assertFalse(returnedEventHandler.isActive());
        assertEquals("testEventUpdated", returnedEventHandler.getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() throws Exception {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("updatedEventHandler");
        updatedEventHandler.setEvent("testEventUpdated");
        updatedEventHandler.setActive(false);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(TEST_EVENT_HANDLER_NAME);
        eventHandler.setEvent(TEST_EVENT_NAME);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(TEST_EVENT_HANDLER_NAME);

        assertNull(redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() throws Exception {
        redisEventHandlerDAO.removeEventHandler(TEST_EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(TEST_EVENT_NAME);
        eventHandler1.setActive(true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(TEST_EVENT_NAME);
        eventHandler2.setActive(false);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(TEST_EVENT_NAME);
        eventHandler1.setActive(true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("differentEvent");
        eventHandler2.setActive(false);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT_NAME, false);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));

        List<EventHandler> eventHandlersActive = redisEventHandlerDAO.getEventHandlersForEvent(TEST_EVENT_NAME, true);

        assertEquals(1, eventHandlersActive.size());
        assertEquals(eventHandler1, eventHandlersActive.get(0));
    }
}