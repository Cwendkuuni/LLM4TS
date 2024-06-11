package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Test
    public void testAddEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        dao.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        dao.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        dao.removeEventHandler("testEvent");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        List<EventHandler> eventHandlers = dao.getAllEventHandlers();

        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        List<EventHandler> eventHandlers = dao.getEventHandlersForEvent("testEvent", true);

        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandler() {
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        EventHandler eventHandler = dao.getEventHandler("testEvent");

        assertNull(eventHandler);
    }
}