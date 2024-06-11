package com.netflix.conductor.redis.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO redisDAO;
    @Mock EventHandler eventHandler;
    @Mock JedisProxy jedisProxy;

    @Before
    public void setUp() {
        redisDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    // Test cases for addEventHandler
    @Test(expected = ConflictException.class)
    public void addEventHandler_conflictException() {
        when(redisDAO.getEventHandler(anyString())).thenReturn(eventHandler);
        redisDAO.addEventHandler(eventHandler);
    }

    // Test cases for updateEventHandler
    @Test(expected = NotFoundException.class)
    public void updateEventHandler_notFoundException() {
        when(redisDAO.getEventHandler(anyString())).thenReturn(null);
        redisDAO.updateEventHandler(eventHandler);
    }

    // Test cases for removeEventHandler
    @Test(expected = NotFoundException.class)
    public void removeEventHandler_notFoundException() {
        when(redisDAO.getEventHandler(anyString())).thenReturn(null);
        redisDAO.removeEventHandler("nonexistent-name");
    }

    // Test cases for getAllEventHandlers
    @Test
    public void getAllEventHandlers_checkListNotEmpty() {
        List<EventHandler> allEventHandlers = redisDAO.getAllEventHandlers();
        assertFalse(allEventHandlers.isEmpty());
    }
    
    // Test cases for getEventHandlersForEvent
    @Test(expected = NotFoundException.class)
    public void getEventHandlersForEvent_notFoundException() {
        when(redisDAO.getEventHandler(anyString())).thenThrow(new NotFoundException("No matching event handler found for event."));
        redisDAO.getEventHandlersForEvent("nonexistent-event", true);
    }
}