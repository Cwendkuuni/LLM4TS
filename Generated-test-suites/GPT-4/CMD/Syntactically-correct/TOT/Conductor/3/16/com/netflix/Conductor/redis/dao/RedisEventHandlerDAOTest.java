package com.netflix.conductor.redis.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO eventHandlerDAO;

    @Before
    public void setUp() {
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test
    public void addEventHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("abc");
        eventHandler.setEvent("event1");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(true);

        eventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }

    @Test(expected=ConflictException.class)
    public void addEventHandlerConflictTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("abc");
        eventHandler.setEvent("event1");
        eventHandlerDAO.addEventHandler(eventHandler);
        eventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected=NotFoundException.class)
    public void updateEventHandlerNotFoundTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("abc");
        eventHandler.setEvent("event1");
        eventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected=NotFoundException.class)
    public void removeEventHandlerNotFoundTest() {
        eventHandlerDAO.removeEventHandler("abc");
    }

    @Test
    public void getAllEventHandlersTest() {
        List<EventHandler> list = new LinkedList<>();
        list.add(new EventHandler());
        when(jedisProxy.hgetAll(anyString())).thenReturn(list);

        List<EventHandler> eventHandlers = eventHandlerDAO.getAllEventHandlers();

        assertEquals(eventHandlers.size(), 1);
    }

    @Test
    public void getEventHandlersForEventTest() {
        Set<String> set = new HashSet<>();
        set.add("abc");
        when(jedisProxy.smembers(anyString())).thenReturn(set);

        List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent("event", true);

        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void getEventHandlerTest() {
        String name = "name";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandler);

        EventHandler handler = eventHandlerDAO.getEventHandler(name);

        assertEquals(handler.getName(), name);
    }

    @Test
    public void getEventHandlerNotFoundTest() {
        String name = "name";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        EventHandler handler = eventHandlerDAO.getEventHandler(name);

        assertNull(handler);
    }

}