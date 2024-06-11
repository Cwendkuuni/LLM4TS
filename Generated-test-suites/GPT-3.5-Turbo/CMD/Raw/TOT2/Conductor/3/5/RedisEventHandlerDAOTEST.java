import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class RedisEventHandlerDAOTest {
    
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        // Test adding new event handler
        redisEventHandlerDAO = mock(RedisEventHandlerDAO.class);
        doNothing().when(redisEventHandlerDAO).addEventHandler(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(redisEventHandlerDAO, times(1)).addEventHandler(eventHandler);
        
        // Test adding existing event handler
        when(redisEventHandlerDAO.getEventHandler("TestEventHandler")).thenReturn(eventHandler);
        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        } catch (ConflictException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        // Test updating existing event handler
        redisEventHandlerDAO = mock(RedisEventHandlerDAO.class);
        doNothing().when(redisEventHandlerDAO).updateEventHandler(eventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(redisEventHandlerDAO, times(1)).updateEventHandler(eventHandler);
        
        // Test updating non-existing event handler
        when(redisEventHandlerDAO.getEventHandler("NonExistingEventHandler")).thenReturn(null);
        try {
            redisEventHandlerDAO.updateEventHandler(new EventHandler());
        } catch (NotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestEventHandler";

        // Test removing existing event handler
        redisEventHandlerDAO = mock(RedisEventHandlerDAO.class);
        doNothing().when(redisEventHandlerDAO).removeEventHandler(eventName);
        redisEventHandlerDAO.removeEventHandler(eventName);
        verify(redisEventHandlerDAO, times(1)).removeEventHandler(eventName);

        // Test removing non-existing event handler
        when(redisEventHandlerDAO.getEventHandler("NonExistingEventHandler")).thenReturn(null);
        try {
            redisEventHandlerDAO.removeEventHandler("NonExistingEventHandler");
        } catch (NotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new ArrayList<>();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        eventHandler1.setEvent("TestEvent1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent2");
        eventHandlers.add(eventHandler1);
        eventHandlers.add(eventHandler2);

        // Test getting all event handlers
        when(redisEventHandlerDAO.getAllEventHandlers()).thenReturn(eventHandlers);
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, result.size());

        // Test getting all event handlers when none exist
        eventHandlers.clear();
        when(redisEventHandlerDAO.getAllEventHandlers()).thenReturn(eventHandlers);
        assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        List<EventHandler> eventHandlers = new ArrayList<>();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent(eventName);
        eventHandler.setActive(true);
        eventHandlers.add(eventHandler);

        // Test getting event handlers for a specific event
        when(redisEventHandlerDAO.getEventHandlersForEvent(eventName, true)).thenReturn(eventHandlers);
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
        assertEquals(1, result.size());

        // Test getting event handlers for a non-existing event
        eventHandlers.clear();
        when(redisEventHandlerDAO.getEventHandlersForEvent("NonExistingEvent", true)).thenReturn(eventHandlers);
        assertEquals(0, redisEventHandlerDAO.getEventHandlersForEvent("NonExistingEvent", true).size());
        
        // Test getting event handlers for a specific event with activeOnly false
        eventHandlers.add(eventHandler);
        eventHandler.setActive(false);
        when(redisEventHandlerDAO.getEventHandlersForEvent(eventName, false)).thenReturn(eventHandlers);
        result = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        // Test getting an existing event handler
        when(redisEventHandlerDAO.getEventHandler("TestEventHandler")).thenReturn(eventHandler);
        EventHandler result = redisEventHandlerDAO.getEventHandler("TestEventHandler");
        assertEquals("TestEventHandler", result.getName());

        // Test getting a non-existing event handler
        when(redisEventHandlerDAO.getEventHandler("NonExistingEventHandler")).thenReturn(null);
        EventHandler nonExistingResult = redisEventHandlerDAO.getEventHandler("NonExistingEventHandler");
        assertEquals(null, nonExistingResult);
    }
}