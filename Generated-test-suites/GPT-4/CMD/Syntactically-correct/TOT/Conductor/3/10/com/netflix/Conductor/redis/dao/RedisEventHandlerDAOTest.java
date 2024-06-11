package com.netflix.conductor.redis.dao;
import org.junit.Test;
import static org.junit.Assert.*;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

public class RedisEventHandlerDAOTest {

    private EventHandlerDAO eventHandlerDAO = new RedisEventHandlerDAO(/* Your necessary parameters here */);

    @Test
    public void addEventHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(eventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflictTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void updateEventHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setEvent("updatedEvent");
        eventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals("updatedEvent", eventHandlerDAO.getEventHandler(eventHandler.getName()).getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandlerDAO.removeEventHandler(eventHandler.getName());
        assertNull(eventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void getEventHandlersForEventTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");
        eventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(eventHandlerDAO.getEventHandlersForEvent("testEvent", false));
        eventHandlerDAO.removeEventHandler(eventHandler.getName());
        assertNull(eventHandlerDAO.getEventHandlersForEvent("testEvent", false));
    }
}