
import org.junit.Test;
import static org.junit.Assert.*;
import com.netflix.conductor.common.metadata.events.EventHandler;

public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        dao.addEventHandler(eventHandler);
        assertNotNull(dao.getEventHandler("testHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        dao.addEventHandler(eventHandler);
        eventHandler.setEvent("updatedEvent");
        dao.updateEventHandler(eventHandler);
        assertEquals("updatedEvent", dao.getEventHandler("testHandler").getEvent());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        dao.addEventHandler(eventHandler);
        dao.removeEventHandler("testHandler");
        assertNull(dao.getEventHandler("testHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("handler1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("handler2");
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        dao.addEventHandler(eventHandler1);
        dao.addEventHandler(eventHandler2);
        assertEquals(2, dao.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("handler1");
        eventHandler1.setEvent("event1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("handler2");
        eventHandler2.setEvent("event2");
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO();
        dao.addEventHandler(eventHandler1);
        dao.addEventHandler(eventHandler2);
        assertEquals(1, dao.getEventHandlersForEvent("event1", true).size());
    }
}
