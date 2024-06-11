package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private EventHandler eventHandler;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_withExistingHandler() {
        // given
        String handlerName = "testHandler";
        when(eventHandler.getName()).thenReturn(handlerName);
        when(jedisProxy.hset(any(), any(), any())).thenReturn(1L);

        // when
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // then exception is thrown
    }

    @Test
    public void testAddEventHandler_success() {
        // given
        String handlerName = "testHandler";
        when(eventHandler.getName()).thenReturn(handlerName);
        when(jedisProxy.hset(any(), any(), any())).thenReturn(null);

        // when
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // then
        verify(jedisProxy).hset(any(), eq(handlerName), any());
    }
}
