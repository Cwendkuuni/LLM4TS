import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(new JedisProxy(), new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @After
    public void tearDown() {
        // Clean up any resources
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("test_event");
        assertNotNull(retrievedEventHandler);
        assertEquals("test_event", retrievedEventHandler.getEvent());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("test_event");
        assertFalse(updatedEventHandler.isActive());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("test_event");
        try {
            redisEventHandlerDAO.getEventHandler("test_event");
            fail("NotFoundException should have been thrown");
        } catch (NotFoundException ex) {
            // NotFoundException expected
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("test_event_1", true);
        EventHandler eventHandler2 = new EventHandler("test_event_2", false);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("test_event_1", true);
        EventHandler eventHandler2 = new EventHandler("test_event_2", false);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> eventHandlersForEvent1 = redisEventHandlerDAO.getEventHandlersForEvent("test_event_1", true);
        List<EventHandler> eventHandlersForEvent2 = redisEventHandlerDAO.getEventHandlersForEvent("test_event_2", true);
        assertEquals(1, eventHandlersForEvent1.size());
        assertEquals(0, eventHandlersForEvent2.size());
    }

    @Test
    public void testGetEventHandlerNotFound() {
        try {
            redisEventHandlerDAO.getEventHandler("non_existent");
            fail("NotFoundException should have been thrown");
        } catch (NotFoundException ex) {
            // NotFoundException expected
        }
    }

}