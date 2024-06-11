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
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.BaseRedisTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseRedisTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testName");
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler1 = createEventHandler("testEvent", "testName");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler1));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testName");
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test
    public void testGetEventHandlerNotFound() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler("nonExistentName"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        String newName = "newTestName";
        EventHandler updatedEventHandler = new EventHandler("testEvent", newName, List.of());
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(newName);
        assertEquals(updatedEventHandler, retrievedEventHandler);
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(createEventHandler("testEvent", "newTestName")));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("testName");
        assertNull(redisEventHandlerDAO.getEventHandler("testName"));
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        assertNull(redisEventHandlerDAO.getEventHandler("testName"));
        redisEventHandlerDAO.removeEventHandler("testName");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = createEventHandler("testEvent1", "testName1");
        EventHandler eventHandler2 = createEventHandler("testEvent1", "testName2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, allEventHandlers.size());
        assertTrue(allEventHandlers.contains(eventHandler1));
        assertTrue(allEventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = createEventHandler("testEvent1", "testName1");
        EventHandler eventHandler2 = createEventHandler("testEvent1", "testName2");
        EventHandler eventHandler3 = createEventHandler("testEvent2", "testName3");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
        assertEquals(2, eventHandlersForEvent.size());
        assertTrue(eventHandlersForEvent.contains(eventHandler1));
        assertTrue(eventHandlersForEvent.contains(eventHandler2));

        List<EventHandler> eventHandlersForEventActive = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);
        assertEquals(1, eventHandlersForEventActive.size());
        assertTrue(eventHandlersForEventActive.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlersForEventNotFound() {
        EventHandler eventHandler1 = createEventHandler("testEvent1", "testName1");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent2", false);
        assertEquals(0, eventHandlersForEvent.size());
        List<EventHandler> eventHandlersForEventActive = redisEventHandlerDAO.getEventHandlersForEvent("testEvent2", true);
        assertEquals(0, eventHandlersForEventActive.size());
    }

    private EventHandler createEventHandler(String event, String name) {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent(event);
        eventHandler.setIsActive(true);
        return eventHandler;
    }
}