package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.List;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisEventHandlerDAO = Mockito.spy(new RedisEventHandlerDAO());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler result = redisEventHandlerDAO.getEventHandler("testEvent");
        Assert.assertEquals(eventHandler, result);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        EventHandler result = redisEventHandlerDAO.getEventHandler("testEvent");
        Assert.assertEquals(eventHandler, result);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("testEvent");
        EventHandler result = redisEventHandlerDAO.getEventHandler("testEvent");
        Assert.assertNull(result);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEvent1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEvent2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        Assert.assertEquals(2, allEventHandlers.size());
        Assert.assertTrue(allEventHandlers.contains(eventHandler1));
        Assert.assertTrue(allEventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEvent1");
        eventHandler1.setEvent("testEvent");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEvent2");
        eventHandler2.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        Assert.assertEquals(2, eventHandlersForEvent.size());
        Assert.assertTrue(eventHandlersForEvent.contains(eventHandler1));
        Assert.assertTrue(eventHandlersForEvent.contains(eventHandler2));
    }

}