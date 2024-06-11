package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Mock
    private Map<String, String> mockMap;

    @Mock
    private EventHandler mockEventHandler;

    @Before
    public void setup() {
        // Execute any setup methods here if necessary
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(mockEventHandler);
        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflictException() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(mockEventHandler);
        redisEventHandlerDAO.addEventHandler(mockEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(mockEventHandler);
        redisEventHandlerDAO.updateEventHandler(mockEventHandler);
        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFoundException() {
        redisEventHandlerDAO.updateEventHandler(mockEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(mockEventHandler);
        redisEventHandlerDAO.removeEventHandler("");
        verify(jedisProxy).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFoundException() {
        redisEventHandlerDAO.removeEventHandler("");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(mockMap);
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(result);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(Exception.class);
        redisEventHandlerDAO.getEventHandler("");
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<String> mockNames = new LinkedList<>();
        mockNames.add("name");
        when(jedisProxy.smembers(anyString())).thenReturn(mockNames);
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(mockEventHandler);
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("", true);
        assertNotNull(result);
    }
}
