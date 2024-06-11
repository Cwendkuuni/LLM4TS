
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler testHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testHandler = new EventHandler();
        testHandler.setName("testHandler");
        testHandler.setEvent("testEvent");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(testHandler.getName());
        redisEventHandlerDAO.addEventHandler(testHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString()))
                .thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(testHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString()))
                .thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(testHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString()))
                .thenReturn(new HashMap<String, String>() {{ put("key", "value"); }});
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString()))
                .thenReturn(new HashSet<String>() {{ add("testHandler"); }});
        when(jedisProxy.hget(anyString()))
                .thenReturn(testHandler.getName());
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event", true);
        assertEquals(1, handlers.size());
    }
}
