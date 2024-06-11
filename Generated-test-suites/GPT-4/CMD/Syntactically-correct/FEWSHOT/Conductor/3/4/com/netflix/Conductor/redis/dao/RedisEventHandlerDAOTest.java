
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    JedisProxy jedisProxy;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    RedisProperties properties;

    @InjectMocks
    RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;

    @Before
    public void init() {
        eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setActive(true);
        eventHandler.setEvent("SampleEvent");
    }

    @Test
    public void testAddEventHandler() {
        doReturn(null).when(jedisProxy).hget(anyString(), anyString());
        doReturn("OK").when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(2)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        doReturn("Existing Handler").when(jedisProxy).hget(anyString(), anyString());
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        doReturn("Existing Handler").when(jedisProxy).hget(anyString(), anyString());
        doReturn("OK").when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(3)).hset(anyString(), anyString(), anyString()).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        doReturn(null).when(jedisProxy).hget(anyString(), anyString());
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        doReturn("Existing Handler").when(jedisProxy).hget(anyString(), anyString());
        doReturn(1L).when(jedisProxy).hdel(anyString(), anyString());
        redisEventHandlerDAO.removeEventHandler("TestEvent");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        doReturn(null).when(jedisProxy).hget(anyString(), anyString());
        redisEventHandlerDAO.removeEventHandler("TestEvent");
    }

    @Test
    public void testGetAllEventHandlers() {
        doReturn(new HashMap<String, String>()).when(jedisProxy).hgetAll(anyString());
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(handlers instanceof List);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        doReturn(new HashSet<String>(Arrays.asList("TestEvent"))).when(jedisProxy).smembers(anyString());
        doReturn("Existing Handler").when(jedisProxy).hget(anyString(), anyString());
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("SampleEvent", true);
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
    }
}
