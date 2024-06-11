package com.netflix.conductor.redis.dao;

import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Ignore;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;
  
    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private Map<String, String> mockedMap;
  
    @Test
    public void testAddEventHandler() {
        //based on the original code, it is necessary to mock the json part
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_ConflictException() {
        //testing for an already existing event handler
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("someString");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        //mocking existing EventHandler
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventhandler1");
        eventHandler.setEvent("event1");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("exisitingEventHandler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFoundException() {
        //tries to update an eventHandler which does not exist
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        // deletes an existing EventHandler
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("SomeString");
        redisEventHandlerDAO.removeEventHandler("EventHandler1");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFoundException() {
        // deletes an non-existing EventHandler
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("EventHandler1");
    }

    @Test
    public void testGetAllHandlers() {
        List<String> keys = new ArrayList<>();
        keys.add("key1");
        keys.add("key2");
        when(jedisProxy.hkeys(anyString())).thenReturn(keys);
        redisEventHandlerDAO.getAllEventHandlers();
    }

    @Test
    public void testGetEventHandlersForEvent_ActiveOnly() {
        //simulate a situation with an input event and activeOnly set to true
        //mocking an existing EventHandler, which is set to active
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        eventHandler.setActive(true);
        redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
    }

    @Test
    public void testGetEventHandlersForEvent_Inactive() {
        //simulate a situation with an input event and activeOnly set to false
        //mocking an existing EventHandler, which is set to inactive
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        eventHandler.setActive(false);
        jedisProxy.sadd(redisEventHandlerDAO.nsKey("EVENT_HANDLERS_BY_EVENT", "event1"), "eventHandler1");
        redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
    }

    @Test
    public void testIndex() {
      //mock existing EventHandler
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventHandler1");
        eventHandler.setEvent("event1");
        redisEventHandlerDAO.index(eventHandler);
    }
}
