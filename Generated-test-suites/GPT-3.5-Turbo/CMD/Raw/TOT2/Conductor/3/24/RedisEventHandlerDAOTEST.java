import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(new JedisProxy(), new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("test_name");
        assertNotNull(retrievedEventHandler);
        assertEquals("test_name", retrievedEventHandler.getName());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("test_name");
        assertNotNull(updatedEventHandler);
        assertEquals(false, updatedEventHandler.isActive());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("test_name");
        EventHandler removedEventHandler = redisEventHandlerDAO.getEventHandler("test_name");
        assertNull(removedEventHandler);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("event1", "handler1", true);
        EventHandler eventHandler2 = new EventHandler("event2", "handler2", false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertTrue(allEventHandlers.contains(eventHandler1));
        assertTrue(allEventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("event1", "handler1", true);
        EventHandler eventHandler2 = new EventHandler("event1", "handler2", false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(1, eventHandlersForEvent.size());
        assertTrue(eventHandlersForEvent.contains(eventHandler1));
    }
}