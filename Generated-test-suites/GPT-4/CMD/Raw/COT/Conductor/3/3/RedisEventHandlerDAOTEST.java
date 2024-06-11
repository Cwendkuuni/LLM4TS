
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*; 

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        redisEventHandlerDAO = mock(RedisEventHandlerDAO.class);
        eventHandler = new EventHandler();
    }

    @Test(expected = NullPointerException.class)
    public void addEventHandler_NullInput() {
        redisEventHandlerDAO.addEventHandler(null);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler_AlreadyExists() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NullPointerException.class)
    public void updateEventHandler_NullInput(){
        redisEventHandlerDAO.updateEventHandler(null);
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandler_NotExist(){
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandler_NotExist(){
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("eventHandlerName");
    }

    @Test
    public void getAllEventHandlers_Success() {
        List<EventHandler> handlers = new ArrayList<>();
        handlers.add(eventHandler);
        handlers.add(new EventHandler());

        when(redisEventHandlerDAO.getAllEventHandlers()).thenReturn(handlers);

        assert(redisEventHandlerDAO.getAllEventHandlers().size() == handlers.size());
    }

    @Test
    public void getEventHandlersForEvent_Success() {
        List<EventHandler> handlers = new ArrayList<>();
        handlers.add(eventHandler);
        handlers.add(new EventHandler());

        when(redisEventHandlerDAO.getEventHandlersForEvent(anyString(), anyBoolean())).thenReturn(handlers);

        assert(redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true).size() == handlers.size());
    }
}
