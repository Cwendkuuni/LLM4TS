package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import org.junit.Test;

public class RedisEventHandlerDAOTest {
    private final RedisEventHandlerDAO dao = new RedisEventHandlerDAO();

    @Test
    public void testAddEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("Test Handler");
        dao.addEventHandler(handler);
        // Check that the handler was added
        assertNotNull(dao.getAllEventHandlers());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("Test Handler");
        dao.addEventHandler(handler);

        handler.setActive(false);
        dao.updateEventHandler(handler);
        // Check that the handler was updated
        assertFalse(dao.getAllEventHandlers().get(0).isActive());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler handler = new EventHandler();
        handler.setName("Test Handler");
        dao.addEventHandler(handler);

        dao.removeEventHandler("Test Handler");
        // Check that the handler was removed
        assertTrue(dao.getAllEventHandlers().isEmpty());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("Test Handler 1");
        dao.addEventHandler(handler1);

        EventHandler handler2 = new EventHandler();
        handler2.setName("Test Handler 2");
        dao.addEventHandler(handler2);

        List<EventHandler> handlers = dao.getAllEventHandlers();
        assertEquals(2, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler handler = new EventHandler();
        handler.setName("Test Handler");
        handler.setEvent("Test Event");
        dao.addEventHandler(handler);
        
        List<EventHandler> handlers = dao.getEventHandlersForEvent("Test Event", true);
        assertEquals(1, handlers.size());
    }
}