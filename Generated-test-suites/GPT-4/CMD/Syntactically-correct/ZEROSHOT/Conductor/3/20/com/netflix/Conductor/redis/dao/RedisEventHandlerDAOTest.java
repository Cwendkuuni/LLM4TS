package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO eventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Spy 
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Before
    public void setUp() {
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void addEventHandlerTest() {
        EventHandler eventHandler = mock(EventHandler.class);
        eventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void updateEventHandlerTest() {
        EventHandler eventHandler = mock(EventHandler.class);
        handlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void removeEventHandlerTest() {
        String name = "eventName";
        handlerDAO.removeEventHandler(name);
        verify(jedisProxy, times(1)).hdel(anyString(), eq(name));
    }

    @Test
    public void getAllEventHandlersTest() {
        List<EventHandler> handlers = eventHandlerDAO.getAllEventHandlers();
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void getEventHandlersForEventTest() {
        String event = "eventName";
        boolean activeOnly = true;
        handlerDAO.getEventHandlersForEvent(event, activeOnly);
        verify(jedisProxy, times(1)).smembers(anyString());
    }
}