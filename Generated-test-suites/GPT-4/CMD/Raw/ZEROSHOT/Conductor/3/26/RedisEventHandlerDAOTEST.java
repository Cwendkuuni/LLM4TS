
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {
    
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private RedisProperties redisProperties;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = buildEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(nsKey("EVENT_HANDLERS"), eventHandler.getName(), toJson(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = buildEventHandler();
        when(jedisProxy.hget(nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(nsKey("EVENT_HANDLERS"), eventHandler.getName(), toJson(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = buildEventHandler();
        when(jedisProxy.hget(nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(toJson(eventHandler));
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(jedisProxy, times(1)).hdel(nsKey("EVENT_HANDLERS"), eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> allEventHandlersMap = buildEventHandlersMap();
        when(jedisProxy.hgetAll(nsKey("EVENT_HANDLERS"))).thenReturn(allEventHandlersMap);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.size() > 0);
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(nsKey("EVENT_HANDLERS_BY_EVENT", "testEvent"))).thenReturn(new HashSet<>(Arrays.asList(eventHandler1, eventHandler2)));
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.size() == 2);
    }
}
