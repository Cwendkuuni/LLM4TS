import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisEventHandlerDAO = Mockito.mock(RedisEventHandlerDAO.class);
    }

    @Test
    public void testAddEventHandler() {
        // Test adding a new event handler
        // Typical use case
        EventHandler eventHandler = new EventHandler("test_event", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Edge case: Add event handler with null name
        EventHandler nullNameEventHandler = new EventHandler(null, true);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            redisEventHandlerDAO.addEventHandler(nullNameEventHandler);
        });
        assertEquals("Missing Name", exception.getMessage());
    }

    @Test
    public void testUpdateEventHandler() {
        // Test updating an existing event handler
        // Typical use case
        EventHandler existingEventHandler = new EventHandler("test_event", true);
        redisEventHandlerDAO.updateEventHandler(existingEventHandler);

        // Edge case: Update non-existing event handler
        EventHandler nonExistingEventHandler = new EventHandler("non_existing_event", true);
        Exception exception = assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.updateEventHandler(nonExistingEventHandler);
        });
        assertEquals("EventHandler with name non_existing_event not found!", exception.getMessage());
    }

    @Test
    public void testRemoveEventHandler() {
        // Test removing an existing event handler
        // Typical use case
        String eventName = "test_event";
        redisEventHandlerDAO.removeEventHandler(eventName);

        // Edge case: Remove non-existing event handler
        String nonExistingEventName = "non_existing_event";
        Exception exception = assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.removeEventHandler(nonExistingEventName);
        });
        assertEquals("EventHandler with name non_existing_event not found!", exception.getMessage());
    }

    @Test
    public void testGetAllEventHandlers() {
        // Test getting all event handlers
        // Typical use case
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Test getting event handlers for a specific event
        // Typical use case
        String event = "test_event";
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertNotNull(eventHandlers);

        // Edge case: Get event handlers for non-existing event
        String nonExistingEvent = "non_existing_event";
        Exception exception = assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.getEventHandlersForEvent(nonExistingEvent, true);
        });
        assertEquals("No matching event handler found for event: non_existing_event", exception.getMessage());
    }

    @Test
    public void testGetEventHandler() {
        // Test getting a specific event handler
        // Typical use case
        String eventName = "test_event";
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventName);
        assertNull(eventHandler);

        // Edge case: Get non-existing event handler
        String nonExistingEventHandlerName = "non_existing_event";
        Exception exception = assertThrows(TransientException.class, () -> {
            redisEventHandlerDAO.getEventHandler(nonExistingEventHandlerName);
        });
        assertEquals("Unable to get event handler named non_existing_event", exception.getMessage());
    }

}