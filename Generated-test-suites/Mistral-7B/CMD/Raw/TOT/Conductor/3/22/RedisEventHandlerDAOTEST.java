import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.RedisTestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class RedisEventHandlerDAOTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JedisProxy jedisProxy;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;
    private String name = "testEventHandler";
    private String event = "testEvent";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
        eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(event);
    }

    @Test
    public void testAddEventHandler() {
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        RedisTestUtil.setUpRedis(jedisProxy);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eq(name), eq(toJson(eventHandler)));
        verify(jedisProxy).sadd(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)), eq(name));

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(name);
        assertEquals(eventHandler, retrievedEventHandler);

        RedisTestUtil.tearDownRedis(jedisProxy);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(name);
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(name);
        newEventHandler.setEvent("testEvent");
        newEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        RedisTestUtil.setUpRedis(jedisProxy);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(name);
        updatedEventHandler.setEvent("testUpdatedEvent");
        updatedEventHandler.setActive(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(name);
        assertEquals(updatedEventHandler, retrievedEventHandler);

        RedisTestUtil.tearDownRedis(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandlerWithNonExistentName");
        updatedEventHandler.setEvent("testEvent");
        updatedEventHandler.setActive(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));
    }

    @Test(expected = ConflictException.class)
    public void testUpdateEventHandlerWithExistingNameButDifferentEvent() {
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        RedisTestUtil.setUpRedis(jedisProxy);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(name);
        updatedEventHandler.setEvent("testEventDifferent");
        updatedEventHandler.setActive(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.updateEventHandler(updatedEventHandler));

        RedisTestUtil.tearDownRedis(jedisProxy);
    }

    @Test
    public void testRemoveEventHandler() {
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        RedisTestUtil.setUpRedis(jedisProxy);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy).hdel(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verify(jedisProxy).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)), eq(name));

        assertNull(redisEventHandlerDAO.getEventHandler(name));

        RedisTestUtil.tearDownRedis(jedisProxy);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        redisEventHandlerDAO.removeEventHandler(name);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(name));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("testEventHandler1");
        handler1.setEvent("testEvent1");
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("testEventHandler2");
        handler2.setEvent("testEvent2");
        handler2.setActive(true);

        eventHandler.setName("testEventHandler3");
        eventHandler.setEvent("testEvent3");
        eventHandler.setActive(true);

        RedisTestUtil.setUpRedis(jedisProxy);

        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(3, eventHandlers.size());
        assertEquals(handler1, eventHandlers.get(0));
        assertEquals(handler2, eventHandlers.get(1));
        assertEquals(eventHandler, eventHandlers.get(2));

        RedisTestUtil.tearDownRedis(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("testEventHandler1");
        handler1.setEvent("testEvent1");
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("testEventHandler2");
        handler2.setEvent("testEvent1");
        handler2.setActive(false);

        EventHandler handler3 = new EventHandler();
        handler3.setName("testEventHandler3");
        handler3.setEvent("testEvent2");
        handler3.setActive(true);

        eventHandler.setName("testEventHandler4");
        eventHandler.setEvent("testEvent1");
        eventHandler.setActive(true);

        RedisTestUtil.setUpRedis(jedisProxy);

        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);
        redisEventHandlerDAO.addEventHandler(handler3);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
        assertEquals(3, eventHandlers.size());
        assertEquals(handler1, eventHandlers.get(0));
        assertEquals(handler2, eventHandlers.get(1));
        assertEquals(eventHandler, eventHandlers.get(2));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);
        assertEquals(2, activeEventHandlers.size());
        assertEquals(handler1, activeEventHandlers.get(0));
        assertEquals(eventHandler, activeEventHandlers.get(1));

        RedisTestUtil.tearDownRedis(jedisProxy);
    }

    @Test
    public void testGetEventHandlersForEventWithEmptySet() {
        RedisTestUtil.setUpRedis(jedisProxy);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(0, eventHandlers.size());

        RedisTestUtil.tearDownRedis(jedisProxy);
    }
}