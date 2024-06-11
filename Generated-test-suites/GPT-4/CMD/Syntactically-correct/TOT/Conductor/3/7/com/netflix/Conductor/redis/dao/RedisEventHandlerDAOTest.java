package com.netflix.conductor.redis.dao;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    JedisProxy jedisProxy;

    @InjectMocks
    RedisEventHandlerDAO redisEventHandlerDAO;

    EventHandler eventHandler1, eventHandler2;

    @Before
    public void setUp() {
        eventHandler1 = new EventHandler();
        eventHandler2 = new EventHandler();

        eventHandler1.setName("Handler1");
        eventHandler2.setName("Handler2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testAddEventHandler() {
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_AlreadyExist() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler() {
        eventHandler1.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler1);

        verify(jedisProxy, times(2)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        redisEventHandlerDAO.updateEventHandler(eventHandler2);
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler(eventHandler1.getName());

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        redisEventHandlerDAO.removeEventHandler(eventHandler2.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        assertNotNull(redisEventHandlerDAO.getEventHandlersForEvent("Event1", true));
    }

    @Test
    public void testIndex() {
        redisEventHandlerDAO.index(eventHandler1);

        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
    }

    @Test
    public void testRemoveIndex() {
        redisEventHandlerDAO.removeIndex(eventHandler1);

        verify(jedisProxy, times(1)).srem(anyString(), anyString());
    }

    @Test
    public void testGetEventHandler() {
        assertNotNull(redisEventHandlerDAO.getEventHandler(eventHandler1.getName()));
    }
}