package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import java.util.Map;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RedisEventHandlerDAOTest {

    @Mock
    private RedisProperties redisProperties;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private JedisProxy jedisProxy;

    private static final String EVENT_HANDLER_NAME = "eventHandlerName";
    private static final String SERIALISED_EVENT_HANDLER = "{}";

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, conductorProperties, redisProperties);
    }

    @Test
    public void addEventHandlerTest() {

        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn(EVENT_HANDLER_NAME);
        when(jedisProxy.hget(anyString(), eq(EVENT_HANDLER_NAME))).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), eq(EVENT_HANDLER_NAME), anyString());
    }

    @Test
    public void updateEventHandlerTest() {

        EventHandler oldEvent = mock(EventHandler.class);
        EventHandler newEvent = mock(EventHandler.class);

        when(newEvent.getName()).thenReturn(EVENT_HANDLER_NAME);

        when(jedisProxy.hget(anyString(), eq(EVENT_HANDLER_NAME))).thenReturn(SERIALISED_EVENT_HANDLER);

        redisEventHandlerDAO.updateEventHandler(newEvent);
        verify(jedisProxy).hset(anyString(), eq(EVENT_HANDLER_NAME), anyString());
    }

    @Test
    public void removeEventHandlerTest() {
        when(jedisProxy.hget(anyString(), eq(EVENT_HANDLER_NAME))).thenReturn(SERIALISED_EVENT_HANDLER);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);

        verify(jedisProxy).hdel(anyString(), eq(EVENT_HANDLER_NAME));
    }

    @Test
    public void getAllEventHandlersTest() {
        Map<String, String> allEvents = new HashMap<>();
        allEvents.put(EVENT_HANDLER_NAME, SERIALISED_EVENT_HANDLER);

        when(jedisProxy.hgetAll(anyString())).thenReturn(allEvents);

        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void getEventHandlersForEventTest() {
        String event = "testEvent";
        when(jedisProxy.smembers(anyString())).thenReturn(null);
        assertEquals(0, redisEventHandlerDAO.getEventHandlersForEvent(event, false).size());

    }

}