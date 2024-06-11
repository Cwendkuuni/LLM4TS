package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisEventHandlerDAO = mock(RedisEventHandlerDAO.class);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        verify(redisEventHandlerDAO, times(1)).addEventHandler(eventHandler);
    }

    @Test
    public void testAddExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("existingEvent");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            fail("Should throw ConflictException");
        } catch (ConflictException e) {
            // Expected
        }

        verify(redisEventHandlerDAO, never()).addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        verify(redisEventHandlerDAO, times(1)).updateEventHandler(eventHandler);
    }

    @Test
    public void testUpdateNonExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonExistingEvent");
        when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
            // Expected
        }

        verify(redisEventHandlerDAO, never()).updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "testEvent";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventName);
        when(redisEventHandlerDAO.getEventHandler(eventName)).thenReturn(eventHandler);

        try {
            redisEventHandlerDAO.removeEventHandler(eventName);
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        verify(redisEventHandlerDAO, times(1)).removeEventHandler(eventName);
    }

    @Test
    public void testRemoveNonExistingEventHandler() {
        String eventName = "nonExistingEvent";
        when(redisEventHandlerDAO.getEventHandler(eventName)).thenReturn(null);

        try {
            redisEventHandlerDAO.removeEventHandler(eventName);
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
            // Expected
        }

        verify(redisEventHandlerDAO, never()).removeEventHandler(eventName);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = mock(List.class);
        when(redisEventHandlerDAO.getAllEventHandlers()).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(result);
        assertEquals(eventHandlers, result);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "testEvent";
        boolean activeOnly = true;
        List<EventHandler> eventHandlers = mock(List.class);
        when(redisEventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly)).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly);

        assertNotNull(result);
        assertEquals(eventHandlers, result);
    }

    @Test
    public void testGetNonExistingEventHandler() {
        String eventName = "nonExistingEvent";
        when(redisEventHandlerDAO.getEventHandler(eventName)).thenReturn(null);

        EventHandler result = redisEventHandlerDAO.getEventHandler(eventName);

        assertNull(result);
    }
}