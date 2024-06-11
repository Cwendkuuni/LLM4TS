package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private EventHandler eventHandler;

    @Before
    public void setUp() throws Exception {
        jedisProxy = mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
        eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");
    }

    @Test
    public void testAddEventHandlerSuccess() {
        when(jedisProxy.hget(anyString(),anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected=ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(jedisProxy.hget(anyString(),anyString())).thenReturn("Existing Handler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
}