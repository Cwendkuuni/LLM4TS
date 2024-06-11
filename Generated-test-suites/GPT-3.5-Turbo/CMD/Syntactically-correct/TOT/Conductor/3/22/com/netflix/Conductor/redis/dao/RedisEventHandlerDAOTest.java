import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        // Setup RedisEventHandlerDAO instance
        redisEventHandlerDAO = new RedisEventHandlerDAO(new JedisProxy(), new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        // Test adding a new event handler
        EventHandler eventHandler = new EventHandler("Test Event", "Test Handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        // Verify that the event handler was added successfully
        assertNotNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test
    public void testUpdateEventHandler() {
        // Test updating an existing event handler
        EventHandler eventHandler = new EventHandler("Test Event", "Test Handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Update the event handler
        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        // Verify that the event handler was updated successfully
        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertFalse(updatedEventHandler.isActive());
    }

    @Test
    public void testRemoveEventHandler() {
        // Test removing an existing event handler
        EventHandler eventHandler = new EventHandler("Test Event", "Test Handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Remove the event handler
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        
        // Verify that the event handler was removed successfully
        assertNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test
    public void testGetAllEventHandlers() {
        // Test getting all event handlers
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        // Verify that the list is not null and contains the added event handler
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.size() > 0);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Test getting event handlers for a specific event
        String event = "Test Event";
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        
        // Verify that the list is not null and contains the added event handler
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.size() == 1);
        assertEquals(eventHandlers.get(0).getEvent(), event);
    }

    @Test
    public void testGetEventHandler() {
        // Test getting a specific event handler
        EventHandler eventHandler = new EventHandler("Test Event", "Test Handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Retrieve the event handler
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        
        // Verify that the retrieved event handler is not null and matches the added event handler
        assertNotNull(retrievedEventHandler);
        assertEquals(retrievedEventHandler.getName(), eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testNonExistentEventHandler() {
        // Test getting a non-existent event handler
        redisEventHandlerDAO.getEventHandler("NonExistentHandler");
    }

    @Test(expected = ConflictException.class)
    public void testDuplicateEventHandler() {
        // Test adding a duplicate event handler
        EventHandler eventHandler = new EventHandler("Test Event", "Test Handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        // Add the same event handler again
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

}