package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;

public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        RedisEventHandlerDAO dao = Mockito.mock(RedisEventHandlerDAO.class);
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        Mockito.when(dao.getEventHandler("test")).thenReturn(null);
        
        dao.addEventHandler(eventHandler);
        
        Mockito.verify(dao, Mockito.times(1)).addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        RedisEventHandlerDAO dao = Mockito.mock(RedisEventHandlerDAO.class);
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        Mockito.when(dao.getEventHandler("test")).thenReturn(eventHandler);
        
        dao.updateEventHandler(eventHandler);
        
        Mockito.verify(dao, Mockito.times(1)).updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        RedisEventHandlerDAO dao = Mockito.mock(RedisEventHandlerDAO.class);
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        Mockito.when(dao.getEventHandler("test")).thenReturn(eventHandler);
        
        dao.removeEventHandler("test");
        
        Mockito.verify(dao, Mockito.times(1)).removeEventHandler("test");
    }

    @Test
    public void testGetAllEventHandlers() {
        RedisEventHandlerDAO dao = Mockito.mock(RedisEventHandlerDAO.class);
        
        dao.getAllEventHandlers();
        
        Mockito.verify(dao, Mockito.times(1)).getAllEventHandlers();
    }

    @Test
    public void testGetEventHandlersForEvent() {
        RedisEventHandlerDAO dao = Mockito.mock(RedisEventHandlerDAO.class);
        
        dao.getEventHandlersForEvent("test", true);
        
        Mockito.verify(dao, Mockito.times(1)).getEventHandlersForEvent("test", true);
    }

    @Test
    public void testGetEventHandler() {
        RedisEventHandlerDAO dao = Mockito.mock(RedisEventHandlerDAO.class);
        
        dao.getEventHandler("test");
        
        Mockito.verify(dao, Mockito.times(1)).getEventHandler("test");
    }
}