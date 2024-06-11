
package com.netflix.conductor.redis.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    private EventHandler eventHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() {
        when(jedisProxy.hget(any(), any())).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFoundException() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFoundException() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(any())).thenReturn(Collections.singletonMap("test", "{}"));

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, Mockito.times(1)).hgetAll(any());
        Assert.assertEquals(1, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(any())).thenReturn(Collections.singleton("testEventHandler"));
        when(jedisProxy.hget(any(), any())).thenReturn("testEventHandler");

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventHandler.getEvent(), true);

        verify(jedisProxy, Mockito.times(1)).smembers(any());
        Assert.assertEquals(1, handlers.size());
    }
}
