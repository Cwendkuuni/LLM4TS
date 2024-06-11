package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private EventHandler eventHandler;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(eventHandler);
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(redisEventHandlerDAO.readValue(Mockito.anyString(), Mockito.eq(EventHandler.class))).thenReturn(eventHandler);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers, result);
        Mockito.verify(jedisProxy, Mockito.times(1)).hgetAll(Mockito.anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        boolean activeOnly = false;
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(eventHandler);
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertEquals(eventHandlers, result);
        Mockito.verify(jedisProxy, Mockito.times(1)).smembers(Mockito.anyString());
    }

    @Test
    public void testGetEventHandlersForEvent_ActiveOnly() {
        String event = "testEvent";
        boolean activeOnly = true;
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(eventHandler);
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertEquals(eventHandlers, result);
        Mockito.verify(jedisProxy, Mockito.times(1)).smembers(Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_NotFound() {
        String event = "testEvent";
        boolean activeOnly = false;
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenThrow(new NotFoundException("No matching event handler found"));

        redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);
    }
}