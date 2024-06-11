
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    @Mock 
    private JedisProxy jedisProxy;
        
    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    private EventHandler eventHandler;
    private String handlerName;
    private String handlerEvent;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        handlerName = "TestHandler";
        handlerEvent = "TestEvent";

        eventHandler = new EventHandler();
        eventHandler.setName(handlerName);
        eventHandler.setEvent(handlerEvent);
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn("OLD_EVENT");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hget(anyString(), eq(handlerName));
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(handlerName)).thenReturn(null);
        
        redisEventHandlerDAO.removeEventHandler(handlerName);
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> eventHandlers = new HashMap<>();
        eventHandlers.put(handlerName, "EVENT_HANDLER");
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);
        
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        
        assertTrue(result.size() == 1);
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> eventSet = new HashSet<>();
        eventSet.add("EVENT_HANDLER_1");
        eventSet.add("EVENT_HANDLER_2");
        
        when(jedisProxy.smembers(anyString())).thenReturn(eventSet);
        
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(handlerEvent, true);
        
        assertTrue(result.size() == 2);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerException() {
        when(jedisProxy.hget(anyString(), eq(handlerName))).thenThrow(new RuntimeException());
        
        redisEventHandlerDAO.getEventHandler(handlerName);
    }
}
