package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Captor
    private ArgumentCaptor<String> jsonCaptor;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), stringCaptor.capture(), jsonCaptor.capture());
        assertEquals("testHandler", stringCaptor.getValue());
        assertNotNull(jsonCaptor.getValue());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(2)).hset(Mockito.anyString(), stringCaptor.capture(), jsonCaptor.capture());
        List<String> stringValues = stringCaptor.getAllValues();
        List<String> jsonValues = jsonCaptor.getAllValues();
        assertEquals("testHandler", stringValues.get(0));
        assertEquals("testHandler", stringValues.get(1));
        assertNotNull(jsonValues.get(0));
        assertNotNull(jsonValues.get(1));
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler("testHandler");
        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(TestUtil.getEventHandlerMap());
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(TestUtil.getEventHandlerNames());
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(TestUtil.getEventHandlerJson());
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
    }
    
    @Test
    public void testGetEventHandler() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(TestUtil.getEventHandlerJson());
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("testHandler");
        assertNotNull(eventHandler);
        assertEquals("testHandler", eventHandler.getName());
    }
}