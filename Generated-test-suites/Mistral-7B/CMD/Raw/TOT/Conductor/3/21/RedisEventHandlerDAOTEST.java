import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseRedisTest;

import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest extends BaseRedisTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "properties", properties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        String handlerJson = "{\"name\": \"" + handlerName + "\", \"event\": \"" + eventName + "\", \"isActive\": true}";

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(handlerName);
        eventHandler.setEvent(eventName);
        eventHandler.setIsActive(true);

        // Add event handler
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Check if event handler was added to Redis
        EventHandler storedEventHandler = redisEventHandlerDAO.getEventHandler(handlerName);
        assertEquals(handlerJson, storedEventHandler.toString());

        // Check if event handlers list contains the added event handler
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(eventHandlers.contains(eventHandler));

        // Cleanup
        redisEventHandlerDAO.removeEventHandler(handlerName);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(handlerName);
        eventHandler.setEvent(eventName);
        eventHandler.setIsActive(true);

        // Add event handler
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Try to add the same event handler again
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFound() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(handlerName);
        eventHandler.setEvent(eventName);
        eventHandler.setIsActive(true);

        // Update nonexistent event handler
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        String newHandlerName = "newHandlerName";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(handlerName);
        eventHandler.setEvent(eventName);
        eventHandler.setIsActive(true);

        // Add event handler
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Update event handler
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(newHandlerName);
        newEventHandler.setEvent(eventName);
        newEventHandler.setIsActive(false);
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        // Check if event handler was updated in Redis
        EventHandler storedEventHandler = redisEventHandlerDAO.getEventHandler(handlerName);
        assertNull(storedEventHandler);

        storedEventHandler = redisEventHandlerDAO.getEventHandler(newHandlerName);
        assertEquals(newEventHandler.toString(), storedEventHandler.toString());

        // Check if event handlers list contains the updated event handler
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(eventHandlers.contains(newEventHandler));

        // Cleanup
        redisEventHandlerDAO.removeEventHandler(newHandlerName);
        redisEventHandlerDAO.removeEventHandler(handlerName);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFound() throws Exception {
        String handlerName = "testHandler";

        // Remove nonexistent event handler
        redisEventHandlerDAO.removeEventHandler(handlerName);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        String eventName = "testEvent";
        String handler1Name = "handler1";
        String handler2Name = "handler2";
        EventHandler handler1 = new EventHandler();
        handler1.setName(handler1Name);
        handler1.setEvent(eventName);
        handler1.setIsActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName(handler2Name);
        handler2.setEvent(eventName);
        handler2.setIsActive(false);

        // Add event handlers
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);

        // Get all event handlers
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Check if event handlers list contains the added event handlers
        assertTrue(eventHandlers.containsAll(Lists.newArrayList(handler1, handler2)));

        // Cleanup
        redisEventHandlerDAO.removeEventHandler(handler1Name);
        redisEventHandlerDAO.removeEventHandler(handler2Name);
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        String eventName = "testEvent";
        String handler1Name = "handler1";
        String handler2Name = "handler2";

        EventHandler handler1 = new EventHandler();
        handler1.setName(handler1Name);
        handler1.setEvent(eventName);
        handler1.setIsActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName(handler2Name);
        handler2.setEvent(eventName);
        handler2.setIsActive(false);

        // Add event handlers
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);

        // Get event handlers for event
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);

        // Check if event handlers list contains the added event handlers
        assertTrue(eventHandlers.containsAll(Lists.newArrayList(handler1, handler2)));

        // Cleanup
        redisEventHandlerDAO.removeEventHandler(handler1Name);
        redisEventHandlerDAO.removeEventHandler(handler2Name);
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly() throws Exception {
        String eventName = "testEvent";
        String handler1Name = "handler1";
        String handler2Name = "handler2";

        EventHandler handler1 = new EventHandler();
        handler1.setName(handler1Name);
        handler1.setEvent(eventName);
        handler1.setIsActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName(handler2Name);
        handler2.setEvent(eventName);
        handler2.setIsActive(false);

        // Add event handlers
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);

        // Get event handlers for event with active only flag
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

        // Check if event handlers list contains only the active event handler
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(handler1));

        // Cleanup
        redisEventHandlerDAO.removeEventHandler(handler1Name);
        redisEventHandlerDAO.removeEventHandler(handler2Name);
    }
}