package com.netflix.conductor.redis.dao;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

public class RedisEventHandlerDAOTest {

    @Mock
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("existingName");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("newTestEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("notExistingName");
        eventHandler.setEvent("newTestEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.removeEventHandler("testName");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotExistingName() {
        redisEventHandlerDAO.removeEventHandler("notExistingName");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        Assert.assertNotNull(handlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        Assert.assertNotNull(handlers);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNotExistingName() {
        redisEventHandlerDAO.getEventHandlersForEvent("notExistingEventName", true);
    }
}