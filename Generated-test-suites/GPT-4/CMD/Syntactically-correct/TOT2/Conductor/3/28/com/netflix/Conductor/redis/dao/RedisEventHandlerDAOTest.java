package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import org.mockito.Mockito;

public class RedisEventHandlerDAOTest {
    
    private final RedisEventHandlerDAO redisHandlerDao;
    
    public RedisEventHandlerDAOTest() {
        // Mock jedisProxy, objectMapper, conductorProperties, properties here
        // For simplicity, we are not going to initialize these dependencies in this mock test
        redisHandlerDao = new RedisEventHandlerDAO(null, null, null, null);
    }
    
    @Test
    public void addEventHandlerTest() {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        Mockito.when(mockHandler.getName()).thenReturn("Test");
        
        try {
            redisHandlerDao.addEventHandler(mockHandler);
        } catch (ConflictException e) {
            fail("Should not have thrown any exception");
        }
    }
    
    @Test
    public void updateEventHandlerTest() {
        EventHandler mockHandler = Mockito.mock(EventHandler.class);
        Mockito.when(mockHandler.getName()).thenReturn("Test2");
        
        try {
            redisHandlerDao.updateEventHandler(mockHandler);
        } catch (NotFoundException e) {
            fail("Should not have thrown any exception");
        }
    }
    
    @Test
    public void removeEventHandlerTest() {
        try {
            redisHandlerDao.removeEventHandler("Test2");
        } catch (NotFoundException e) {
            fail("Should not have thrown any exception");
        }
    }
    
    @Test
    public void getAllEventHandlersTest() {
        List<EventHandler> handlers = new LinkedList<>();
        
        handlers = redisHandlerDao.getAllEventHandlers();
        
        assertNotNull(handlers);
    }
    
    @Test
    public void getEventHandlersForEventTest() {
        List<EventHandler> handlers = new LinkedList<>();
        
        handlers = redisHandlerDao.getEventHandlersForEvent("TestEvent", false);
        
        assertNotNull(handlers);
    }
}