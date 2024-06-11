import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        dao.addEventHandler(eventHandler);
        EventHandler retrieved = dao.getEventHandler("TestEventHandler");

        assertNotNull(retrieved);
        assertEquals("TestEventHandler", retrieved.getName());
        assertEquals("TestEvent", retrieved.getEvent());

        // Test conflict exception
        assertThrows(ConflictException.class, () -> dao.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");
        dao.addEventHandler(eventHandler);

        eventHandler.setEvent("NewTestEvent");
        dao.updateEventHandler(eventHandler);
        EventHandler updated = dao.getEventHandler("TestEventHandler");

        assertEquals("NewTestEvent", updated.getEvent());

        // Test not found exception
        assertThrows(NotFoundException.class, () -> dao.updateEventHandler(new EventHandler()));
    }

    @Test
    public void testRemoveEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");
        dao.addEventHandler(eventHandler);

        dao.removeEventHandler("TestEventHandler");
        assertNull(dao.getEventHandler("TestEventHandler"));

        // Test not found exception
        assertThrows(NotFoundException.class, () -> dao.removeEventHandler("NonExistentEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        eventHandler1.setEvent("TestEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent2");

        dao.addEventHandler(eventHandler1);
        dao.addEventHandler(eventHandler2);

        List<EventHandler> handlers = dao.getAllEventHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        eventHandler1.setEvent("TestEvent");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent");

        dao.addEventHandler(eventHandler1);
        dao.addEventHandler(eventHandler2);

        List<EventHandler> handlers = dao.getEventHandlersForEvent("TestEvent", false);
        assertEquals(2, handlers.size());
    }
}