package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO eventHandlerDAO;
    private JedisProxy jedisProxy;
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, new RedisProperties());
        eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerExisting() {
        Mockito.when(jedisProxy.get(eventHandler.getName())).thenReturn("existingHandler");
        eventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void addEventHandlerNew() {
        Mockito.when(jedisProxy.get(eventHandler.getName())).thenReturn(null);
        eventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq(eventHandler.getName()), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandlerNotFound() {
        Mockito.when(jedisProxy.get(eventHandler.getName())).thenReturn(null);
        eventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void updateEventHandlerExist() {
        EventHandler existing = new EventHandler();
        existing.setName("testHandler");
        Mockito.when(jedisProxy.get(eventHandler.getName())).thenReturn(existing);
        eventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq(eventHandler.getName()), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFound() {
        Mockito.when(jedisProxy.get(eventHandler.getName())).thenReturn(null);
        eventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void removeEventHandlerExist() {
        EventHandler existing = new EventHandler();
        existing.setName("testHandler");
        Mockito.when(jedisProxy.get(eventHandler.getName())).thenReturn(existing);
        eventHandlerDAO.removeEventHandler(eventHandler.getName());
        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.eq(eventHandler.getName()));
    }

    @Test
    public void getAllEventHandlers() {
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString()))
            .thenReturn(Collections.singletonMap(eventHandler.getName(), "handlersJson"));
        List<EventHandler> eventHandlers = eventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
    }

    @Test
    public void getEventhandlersForEvent() {
        String eventName = "eventName";
        Mockito.when(jedisProxy.smembers(Mockito.anyString()))
            .thenReturn(Collections.singleton(eventHandler.getName()));
        List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent(eventName, false);
        assertNotNull(eventHandlers);
    }
}