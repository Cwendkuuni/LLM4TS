
package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final String EVENT_NAME = "EVENT_NAME";
    
    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Mock
    private EventHandler eventHandler;
    
    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ConductorProperties conductorProperties;
    
    @Mock
    private RedisProperties properties;
    
    @Before
    public void setUp(){
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, properties);
        when(eventHandler.getName()).thenReturn(EVENT_NAME);
        when(eventHandler.getEvent()).thenReturn(EVENT_NAME);
    }
    
    @Test(expected = ConflictException.class)
    public void testAddEventHandler_WithExistingHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
    
    @Test
    public void testAddEventHandler_WithNewHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }
    
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NonExistingHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);
    }
    
    @Test
    public void testRemoveEventHandler_WithExistingHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);
        doNothing().when(jedisProxy).hdel(anyString(), anyString());
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(jedisProxy).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NonExistingHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }
    
    @Test
    public void testUpdateEventHandler_ExistingHandlerWithSameEvent() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);
        when(eventHandler.getEvent()).thenReturn(EVENT_NAME);
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testGetEventHandlersForEvent_NonExistingEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.EMPTY_SET);
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);
        assertEquals(0, result.size());
        verify(jedisProxy, never()).hget(anyString(), anyString());
    }
}
