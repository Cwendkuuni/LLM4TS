import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import org.junit.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_ConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "testEvent";
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        redisEventHandlerDAO.removeEventHandler(eventName);
        
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFoundException() {
        String eventName = "testEvent";
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.removeEventHandler(eventName);
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);
        
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        
        Assert.assertNotNull(handlers);
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "testEvent";
        boolean activeOnly = true;
        
        redisEventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly);
        
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_NotFoundException() {
        String eventName = "testEvent";
        boolean activeOnly = true;
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly);
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "testEvent";
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("testEventHandler");
        
        EventHandler handler = redisEventHandlerDAO.getEventHandler(eventName);
        
        Assert.assertNotNull(handler);
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandler_TransientException() {
        String eventName = "testEvent";
        
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new Exception());
        
        redisEventHandlerDAO.getEventHandler(eventName);
    }
}