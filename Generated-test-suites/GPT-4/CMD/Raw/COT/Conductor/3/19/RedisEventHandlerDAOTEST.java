
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
    EventHandlerDAO eventHandlerDAO;
    JedisProxy jedisProxy;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        eventHandlerDSAO.addEventHandler(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExisting() {
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotExist() {
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        eventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotExist() {
        eventHandlerDAO.removeEventHandler("test");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = mock(EventHandler.class);
        when(eventHandler1.getName()).thenReturn("test1");
        EventHandler eventHandler2 = mock(EventHandler.class);
        when(eventHandler2.getName()).thenReturn("test2");
        eventHandlerDAO.addEventHandler(eventHandler1);
        eventHandlerDAO.addEventHandler(eventHandler2);
        assertEquals(2, eventHandlerDAO.getAllEventHandlers().size());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerFailure() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenThrow(new RuntimeException("Redis exception"));
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("test");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandlerDAO.getEventHandler(eventHandler.getName());
    }
}
