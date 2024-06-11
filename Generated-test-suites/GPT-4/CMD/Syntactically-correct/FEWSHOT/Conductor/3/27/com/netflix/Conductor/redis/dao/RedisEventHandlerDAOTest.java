package com.netflix.conductor.redis.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import java.util.Collections;
import java.util.List;

public class RedisEventHandlerDAOTest {
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private EventHandler eventHandler;
    private RedisProperties redisProperties;

    @BeforeEach
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        redisProperties = new RedisProperties();
        eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setEvent("event");
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        assertDoesNotThrow(() -> {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        });

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testAddEventHandler_Conflict() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("someString");

        assertThrows(ConflictException.class, () -> {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        });
    }

    @Test
    public void testUpdateEventHandler() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("someString");

        assertDoesNotThrow(() -> {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
        });

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler_NotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
        });
    }

    @Test
    public void testRemoveEventHandler() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("someString");

        assertDoesNotThrow(() -> {
            redisEventHandlerDAO.removeEventHandler("test");
        });

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler_NotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            redisEventHandlerDAO.removeEventHandler("test");
        });
    }

    @Test
    public void testGetAllEventHandlers() {
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(
                Collections.singletonMap("test", "{\"name\":\"test\"}")
        );

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(handlers);
        assertEquals(1, handlers.size());
        assertEquals("test", handlers.get(0).getName());
    }
    
    // Continue with the tests for the remaining methods
}