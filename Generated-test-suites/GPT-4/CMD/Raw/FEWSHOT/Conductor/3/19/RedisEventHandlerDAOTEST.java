
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.config.ConductorProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;

    @Before
    public void setup() {
        eventHandler = new EventHandler();
        eventHandler.setName("Test");
        eventHandler.setEvent("Sample Event");
        eventHandler.setActive(true);
    }

    @Test(expected = ConflictException.class)
    public void testAddExistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("Test");
    }

    @Test
    public void testGetNoneEventHandler() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Collections.emptyMap());
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(eventHandlers.isEmpty());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerByName_Exception() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
        redisEventHandlerDAO.getEventHandlersForEvent("Test", true);
    }

    //Add more test cases here to accurately test your class...
}
