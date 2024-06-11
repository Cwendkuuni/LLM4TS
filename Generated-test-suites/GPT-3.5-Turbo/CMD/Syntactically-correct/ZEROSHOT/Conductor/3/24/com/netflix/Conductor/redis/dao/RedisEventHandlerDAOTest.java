package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;



@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisPool jedisPool;

    @Mock
    private Jedis jedis;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        Mockito.when(jedis.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        assertNotNull(eventHandler.getName());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        Mockito.when(jedis.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        assertNotNull(eventHandler.getName());
    }

    @Test
    public void testRemoveEventHandler() {
        Mockito.when(jedis.hdel(anyString(), anyString())).thenReturn(1L);
        
        redisEventHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        Mockito.when(jedis.hgetAll(anyString())).thenReturn(Collections.emptyMap());
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertNotNull(eventHandlers);
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Mockito.when(jedis.smembers(anyString())).thenReturn(Collections.emptySet());
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        
        assertNotNull(eventHandlers);
        assertEquals(0, eventHandlers.size());
    }

    @Before
    public void setUp() {
        Mockito.when(jedisPool.getResource()).thenReturn(jedis);
    }
}

