
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, redisProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_ConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingValue");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFoundException() {
        redisEventHandlerDAO.removeEventHandler("name");
    }

    @Test
    public void testGetAllEventHandlers_IsEmpty() {
        Map<String, String> eventData = new HashMap<>();
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventData);

        List<EventHandler> resultList = redisEventHandlerDAO.getAllEventHandlers();
        verify(jedisProxy, times(1)).hgetAll(anyString());
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testGetEventHandler_Null() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("name");
        assertNull(eventHandler);
    }

    @Test
    public void testGetEventHandler_NonNull() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("name");
        assertNotNull(eventHandler);
    }
    // additional methods for getEventHandlersForEvent, index, and removeIndex can be tested similarly
}
