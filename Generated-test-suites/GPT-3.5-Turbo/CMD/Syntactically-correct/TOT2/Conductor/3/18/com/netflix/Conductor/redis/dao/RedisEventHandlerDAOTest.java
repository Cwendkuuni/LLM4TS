import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

public class RedisEventHandlerDAOTest {

    private EventHandlerDAO eventHandlerDAO;

    @Before
    public void setup() {
        eventHandlerDAO = new RedisEventHandlerDAO(null, null, null, null);
    }

    @Test
    public void testAddEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        eventHandlerDAO.addEventHandler(eventHandler);

        // Edge case - Adding duplicate event handler
        try {
            eventHandlerDAO.addEventHandler(eventHandler);
        } catch (ConflictException e) {
            // Expected behavior
        }
    }

    @Test
    public void testUpdateEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        eventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setActive(false);
        eventHandlerDAO.updateEventHandler(eventHandler);

        // Edge case - Updating non-existing event handler
        try {
            eventHandlerDAO.updateEventHandler(new EventHandler());
        } catch (NotFoundException e) {
            // Expected behavior
        }
    }

    @Test
    public void testRemoveEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        eventHandlerDAO.addEventHandler(eventHandler);

        eventHandlerDAO.removeEventHandler("testEventHandler");

        // Edge case - Removing non-existing event handler
        try {
            eventHandlerDAO.removeEventHandler("nonExistingEventHandler");
        } catch (NotFoundException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        // Typical use case
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");
        eventHandler2.setActive(true);

        eventHandlerDAO.addEventHandler(eventHandler1);
        eventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(2, eventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Typical use case
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setActive(false);

        eventHandlerDAO.addEventHandler(eventHandler1);
        eventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(1, eventHandlerDAO.getEventHandlersForEvent("testEvent", true).size());

        // Edge case - No matching event handler found
        try {
            eventHandlerDAO.getEventHandlersForEvent("nonExistingEvent", true);
        } catch (NotFoundException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGetEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        eventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(eventHandler.getName(), eventHandlerDAO.getEventHandler("testEventHandler").getName());

        // Edge case - Unable to get event handler
        try {
            eventHandlerDAO.getEventHandler("nonExistingEventHandler");
        } catch (Exception e) {
            // Expected behavior
        }
    }

}