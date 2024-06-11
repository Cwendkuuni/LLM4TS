
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addEventHandlerSuccess() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq(eventHandler.getName()), Mockito.anyString());
        // check recordRedisDaoRequests call once with "addEventHandler" argument
        Mockito.verify(jedisProxy).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestName");
        Mockito.when(redisEventHandlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEventHandlerMissingName() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
}
