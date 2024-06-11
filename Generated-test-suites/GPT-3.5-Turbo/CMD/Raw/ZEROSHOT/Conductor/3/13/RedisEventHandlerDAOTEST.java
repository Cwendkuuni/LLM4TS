import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private EventHandler eventHandler;

    @Before
    public void setup() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(new JedisProxy(), new ObjectMapper(), new ConductorProperties(), new RedisProperties());
        eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setEvent("UpdatedEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals("UpdatedEvent", redisEventHandlerDAO.getEventHandler("TestEventHandler").getEvent());
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("TestEventHandler");
        assertNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.addEventHandler(eventHandler);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        redisEventHandlerDAO.addEventHandler(eventHandler);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);
        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
    }

    @Test
    public void testIndex() {
        redisEventHandlerDAO.index(eventHandler);
        Set<String> names = redisEventHandlerDAO.jedisProxy.smembers(redisEventHandlerDAO.nsKey("EVENT_HANDLERS_BY_EVENT", "TestEvent"));
        assertTrue(names.contains("TestEventHandler"));
    }

    @Test
    public void testRemoveIndex() {
        redisEventHandlerDAO.index(eventHandler);
        redisEventHandlerDAO.removeIndex(eventHandler);
        Set<String> names = redisEventHandlerDAO.jedisProxy.smembers(redisEventHandlerDAO.nsKey("EVENT_HANDLERS_BY_EVENT", "TestEvent"));
        assertFalse(names.contains("TestEventHandler"));
    }

    @Test
    public void testEventHandlersNotFoundForEvent() {
        try {
            redisEventHandlerDAO.getEventHandlersForEvent("UnknownEvent", true);
            fail("NotFoundException should have been thrown");
        } catch (NotFoundException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("TestEventHandler");
        assertNotNull(retrievedEventHandler);
        assertEquals("TestEventHandler", retrievedEventHandler.getName());
    }
}