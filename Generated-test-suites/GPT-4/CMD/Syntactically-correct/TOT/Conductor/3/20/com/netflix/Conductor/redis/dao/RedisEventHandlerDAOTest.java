package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {
    
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO eventHandlerDAO;
    private EventHandler eventHandler;

    @Before
    public void setup() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
        eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        eventHandler.setActive(true);
        jedisProxy.hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), toJson(eventHandler));
    }
    
    @Test
    public void testAddEventHandler() {
        eventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        eventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        eventHandlerDAO.removeEventHandler("testEvent");
        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = eventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
    }
    
    // Test getEventHandlersForEvent is not that simple, needs more mock scenarios
    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent("test", true);
        assertNotNull(eventHandlers);
    }    
}