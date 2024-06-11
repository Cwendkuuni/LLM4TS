
package com.netflix.conductor.redis.dao;

import javax.annotation.Resource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RedisEventHandlerDAOTest {

    @Resource
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    private EventHandler eventHandler;

    @Before
    public void beforeTest() {
        eventHandler = Mockito.mock(EventHandler.class);
    }

    @Test
    public void testAddEventHandler() {
        Mockito.when(eventHandler.getName()).thenReturn("EventHandler_Name");
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(redisEventHandlerDAO, Mockito.times(1)).addEventHandler(eventHandler);
    }
    
    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        Mockito.when(eventHandler.getName()).thenReturn("EventHandler_Name");
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
    
    @Test
    public void testUpdateEventHandler() {
        Mockito.when(eventHandler.getName()).thenReturn("EventHandler_Name");
        Mockito.when(eventHandler.getEvent()).thenReturn("Event_Name");
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(eventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(redisEventHandlerDAO, Mockito.times(1)).updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        Mockito.when(eventHandler.getName()).thenReturn("EventHandler_Name");
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        Mockito.when(eventHandler.getName()).thenReturn("EventHandler_Name");
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(eventHandler);
        redisEventHandlerDAO.removeEventHandler("EventHandler_Name");
        Mockito.verify(redisEventHandlerDAO, Mockito.times(1)).removeEventHandler("EventHandler_Name");
    }
    
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("EventHandler_Name");
    }

    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.getAllEventHandlers();
        Mockito.verify(redisEventHandlerDAO, Mockito.times(1)).getAllEventHandlers();
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        Mockito.when(eventHandler.getName()).thenReturn("EventHandler_Name");
        Mockito.when(redisEventHandlerDAO.getEventHandler("EventHandler_Name")).thenReturn(eventHandler);
        redisEventHandlerDAO.getEventHandlersForEvent("Event_Name", true);
        Mockito.verify(redisEventHandlerDAO, Mockito.times(1)).getEventHandlersForEvent("Event_Name", true);
    }
}
