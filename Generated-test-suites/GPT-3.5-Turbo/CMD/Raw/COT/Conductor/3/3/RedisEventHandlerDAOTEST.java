
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        // Test the method
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Verify the method behavior
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        // Test the method
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        // Test the method
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        // Verify the method behavior
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        // Test the method
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        // Test the method
        redisEventHandlerDAO.removeEventHandler("testEvent");

        // Verify the method behavior
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        // Test the method
        redisEventHandlerDAO.removeEventHandler("testEvent");
    }

    @Test
    public void testGetAllEventHandlers() {
        // Mocking behavior
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);
        
        // Test the method
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        
        // Verify the method behavior
        assertEquals(0, result.size());
    }

    @Test
    public void testIndex() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");
        
        // Test the method
        redisEventHandlerDAO.index(eventHandler);
        
        // Verify the method behavior
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
    }

    @Test
    public void testRemoveIndex() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");
        
        // Test the method
        redisEventHandlerDAO.removeIndex(eventHandler);
        
        // Verify the method behavior
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Mocking behavior
        when(jedisProxy.smembers(anyString())).thenReturn(new LinkedList<String>());
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEventHandler");
        
        // Test the method
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        
        // Verify the method behavior
        assertEquals(0, result.size());
    }

    @Test
    public void testGetEventHandler() {
        // Mocking behavior
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        // Test the method
        EventHandler result = redisEventHandlerDAO.getEventHandler("testEvent");
        
        // Verify the method behavior
        assertNull(result);
    }
}
