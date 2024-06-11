
package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    private RedisEventHandlerDAO testDAO;

    @Before
    public void setUp() {
        this.testDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, properties);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerWithExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("existingName");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("EventHandlerInJsonFormat");

        testDAO.addEventHandler(eventHandler);
    }
    
    @Test(expected = NotFoundException.class)
    public void updateEventHandlerWithNonExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonExistingName");

        testDAO.updateEventHandler(eventHandler);
    }
    
    @Test(expected = NotFoundException.class)
    public void removeEventHandlerWithNonExistingEventHandler() {
        testDAO.removeEventHandler("nonExistingName");
    }
    
    @Test
    public void getAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(new HashMap<>());

        List<EventHandler> eventHandlers = testDAO.getAllEventHandlers();

        assertEquals(0, eventHandlers.size());
    }
    
    @Test(expected = NotFoundException.class)
    public void getEventHandlersForEvent() {
        testDAO.getEventHandlersForEvent("nonExistingEvent", true);
    }
}
