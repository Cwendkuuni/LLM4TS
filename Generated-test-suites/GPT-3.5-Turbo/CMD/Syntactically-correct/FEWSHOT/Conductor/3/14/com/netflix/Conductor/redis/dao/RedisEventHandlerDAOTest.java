
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("event1");
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of());
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(eq("EVENT_HANDLERS"), eq("event1"), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("event1");
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("event1", "{}"));
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(eq("EVENT_HANDLERS"), eq("event1"), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("event1", "{}"));
        
        redisEventHandlerDAO.removeEventHandler("event1");
        
        verify(jedisProxy, times(1)).hdel(eq("EVENT_HANDLERS"), eq("event1"));
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("event1", "{}", "event2", "{}"));
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertNotNull(eventHandlers);
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("event1"));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        EventHandler eventHandler = new EventHandler();
        eventHandler.setEvent("event1");
        List<EventHandler> expectedHandlers = List.of(eventHandler);
        
        List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        
        assertNotNull(actualHandlers);
        assertEquals(expectedHandlers.size(), actualHandlers.size());
        assertEquals(expectedHandlers.get(0).getEvent(), actualHandlers.get(0).getEvent());
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("event1");
        
        assertNotNull(eventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
        
        redisEventHandlerDAO.getEventHandler("event1");
    }
}
