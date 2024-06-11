package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;
    
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    } 

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "test";
        redisEventHandlerDAO.removeEventHandler(name);
        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertNotNull(eventHandlers);
    }
}