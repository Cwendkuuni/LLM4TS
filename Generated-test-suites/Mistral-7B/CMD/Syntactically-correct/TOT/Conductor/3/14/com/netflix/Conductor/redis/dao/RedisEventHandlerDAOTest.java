import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.testutils.JedisTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        jedisProxy = new JedisProxy();
        JedisTestUtils.init(jedisProxy);

        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", new ConductorProperties());
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("event1", "handler1");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals("handler1", jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler1 = new EventHandler("event1", "handler1");
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler("event1", "handler2");
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        fail("Expected ConflictException but got no exception");
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler("event1", "handler1");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1", toJson(eventHandler));

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("event1");
        assertSame(eventHandler, retrievedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        redisEventHandlerDAO.getEventHandler("nonExistentEvent");
        fail("Expected NotFoundException but got no exception");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("event1", "handler1");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1", toJson(eventHandler));

        EventHandler updatedEventHandler = new EventHandler("event1", "handler2");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertEquals("handler2", jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler updatedEventHandler = new EventHandler("event1", "handler2");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        fail("Expected NotFoundException but got no exception");
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("event1", "handler1");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1", toJson(eventHandler));

        redisEventHandlerDAO.removeEventHandler("event1");

        assertNull(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("nonExistentEvent");
        fail("Expected NotFoundException but got no exception");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("event1", "handler1");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1", toJson(eventHandler1));

        EventHandler eventHandler2 = new EventHandler("event1", "handler2");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event2", toJson(eventHandler2));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("event1", "handler1");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1", toJson(eventHandler1));

        EventHandler eventHandler2 = new EventHandler("event1", "handler2");
        jedisProxy.hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "event1", toJson(eventHandler2));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        assertEquals(1, activeEventHandlers.size());
        assertTrue(activeEventHandlers.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false);
        assertEquals(0, eventHandlers.size());
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON: " + e.getMessage(), e);
        }
    }
}