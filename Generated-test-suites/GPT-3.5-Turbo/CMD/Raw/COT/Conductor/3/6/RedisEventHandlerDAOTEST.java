
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO dao;

    @Before
    public void setUp() {
        dao = new RedisEventHandlerDAO(new JedisProxy(), new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");

        dao.addEventHandler(eventHandler);

        assertNotNull(dao.getEventHandler("testEvent"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventUpdate");
        eventHandler.setEvent("testEventUpdate");

        dao.addEventHandler(eventHandler);

        eventHandler.setEvent("updatedEvent");

        dao.updateEventHandler(eventHandler);

        assertEquals("updatedEvent", dao.getEventHandler("testEventUpdate").getEvent());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventRemove");
        eventHandler.setEvent("testEventRemove");

        dao.addEventHandler(eventHandler);

        dao.removeEventHandler("testEventRemove");

        assertNull(dao.getEventHandler("testEventRemove"));
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> initialHandlers = dao.getAllEventHandlers();

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testGetAllEventHandlers");
        eventHandler.setEvent("testGetAllEventHandlers");

        dao.addEventHandler(eventHandler);

        List<EventHandler> allHandlers = dao.getAllEventHandlers();

        assertEquals(initialHandlers.size() + 1, allHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEvent1");
        eventHandler1.setEvent("testGetEventHandlersForEvent");

        dao.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEvent2");
        eventHandler2.setEvent("testGetEventHandlersForEvent");

        dao.addEventHandler(eventHandler2);

        List<EventHandler> handlers = dao.getEventHandlersForEvent("testGetEventHandlersForEvent", false);

        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEventNotFound() {
        List<EventHandler> handlers = dao.getEventHandlersForEvent("nonExistingEvent", false);

        assertTrue(handlers.isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventException");
        eventHandler.setEvent("testEventException");

        dao.addEventHandler(eventHandler);

        dao.removeEventHandler("testEventException");

        dao.getEventHandlersForEvent("testEventException", false);
    }

}
