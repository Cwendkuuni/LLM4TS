
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private EventHandler testEventHandler;
    private final String testKey = "test_key";

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new RedisProperties());

        testEventHandler = new EventHandler();
        testEventHandler.setName(testKey);
    }

    @After
    public void cleanup() {
        this.jedisProxy = null;
        this.redisEventHandlerDAO = null;
        this.testEventHandler = null;
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(testEventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), eq(testKey), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("");

        redisEventHandlerDAO.addEventHandler(testEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(testEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(testEventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), eq(testKey), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFoundException() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(testKey);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("");
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler(testKey);
        verify(jedisProxy, times(1)).hdel(anyString(), eq(testKey));
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(any());

        List<EventHandler> returnList = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(returnList);
    }
}
