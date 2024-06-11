package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.config.ConductorProperties;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Collections;

public class RedisEventHandlerDAOTest {
    
    private JedisProxy jedisProxy;
    private RedisProperties redisProperties;
    private ConductorProperties conductorProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;
	
	@Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        redisProperties = mock(RedisProperties.class);
        conductorProperties = mock(ConductorProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setEvent("TestEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(2)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("TestName");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, handlers.size());
        assertEquals("TestName", handlers.get(0).getName());
    }
	
	@Test
    public void testGetEventHandlerForEvent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        eventHandler.setEvent("TestEvent");
        eventHandler.setActive(true);

        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton("TestName"));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\": \"TestName\", \"event\":\"TestEvent\", \"active\":true}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);
        assertEquals(1, eventHandlers.size());
        assertEquals("TestName", eventHandlers.get(0).getName());
        assertTrue(eventHandlers.get(0).isActive());
    }
}