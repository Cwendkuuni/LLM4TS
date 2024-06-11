
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.dao.exceptions.ConflictException;
import com.netflix.conductor.dao.exceptions.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void before() {
        this.redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("sample-handler");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("sample-handler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Some-json");
        when(objectMapper.readValue(anyString(), Mockito.<Class<EventHandler>>any())).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
    
    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("sample-handler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Some-json");
        when(objectMapper.readValue(anyString(), Mockito.<Class<EventHandler>>any())).thenReturn(eventHandler);
        when(eventHandler.getEvent()).thenReturn("Some-event");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("sample-handler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = Mockito.mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("sample-handler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Some-json");
        when(objectMapper.readValue(anyString(), Mockito.<Class<EventHandler>>any())).thenReturn(eventHandler);
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.removeEventHandler("sample-handler");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("non-existing-handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = createEventHandlersMap();
        when(jedisProxy.hgetAll(anyString())).thenReturn(all);
        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());
        assertTrue(redisEventHandlerDAO.getAllEventHandlers().size() == 3);
    }
    
    private Map<String, String> createEventHandlersMap() {
        Map<String, String> map = new HashMap<>();
        map.put("handler1", "json1");
        map.put("handler2", "json2");
        map.put("handler3", "json3");
        return map;
    }
}
