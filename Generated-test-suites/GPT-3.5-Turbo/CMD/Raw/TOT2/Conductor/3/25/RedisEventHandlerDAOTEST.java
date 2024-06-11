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
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                new JedisProxy(/* mock JedisProxy */),
                new ObjectMapper(),
                new ConductorProperties(),
                new RedisProperties()
        );
    }

    @Test
    public void testAddEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler("event1", "handler1", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        // Check if the event handler was added successfully
        EventHandler addedEventHandler = redisEventHandlerDAO.getEventHandler("handler1");
        assertNotNull(addedEventHandler);
        assertEquals("event1", addedEventHandler.getEvent());
    }

    @Test
    public void testUpdateEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler("event2", "handler2", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        // Update the event handler
        eventHandler.setEvent("event3");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        // Check if the event handler was updated successfully
        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("handler2");
        assertNotNull(updatedEventHandler);
        assertEquals("event3", updatedEventHandler.getEvent());
    }

    @Test
    public void testRemoveEventHandler() {
        // Add an event handler
        EventHandler eventHandler = new EventHandler("event4", "handler4", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        // Remove the event handler
        redisEventHandlerDAO.removeEventHandler("handler4");
        
        // Check if the event handler was removed successfully
        EventHandler removedEventHandler = redisEventHandlerDAO.getEventHandler("handler4");
        assertNull(removedEventHandler);
    }

    @Test
    public void testGetAllEventHandlers() {
        // Add multiple event handlers
        redisEventHandlerDAO.addEventHandler(new EventHandler("event5", "handler5", true));
        redisEventHandlerDAO.addEventHandler(new EventHandler("event6", "handler6", true));
        
        // Retrieve all event handlers
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        // Check if all event handlers were retrieved
        assertEquals(2, allEventHandlers.size());
        assertTrue(allEventHandlers.stream().anyMatch(h -> h.getHandlerName().equals("handler5")));
        assertTrue(allEventHandlers.stream().anyMatch(h -> h.getHandlerName().equals("handler6")));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Add event handlers for a specific event
        redisEventHandlerDAO.addEventHandler(new EventHandler("event7", "handler7", true));
        redisEventHandlerDAO.addEventHandler(new EventHandler("event7", "handler8", false));
        
        // Retrieve event handlers for the event
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("event7", true);
        
        // Check if only active event handlers for the event were retrieved
        assertEquals(1, eventHandlersForEvent.size());
        assertEquals("handler7", eventHandlersForEvent.get(0).getHandlerName());
    }

}