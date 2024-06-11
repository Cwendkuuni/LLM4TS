
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;

import java.util.List;
import java.util.HashSet;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;
    
    @Mock
    private ConductorProperties conductorProperties;
  
    @Mock
    private RedisProperties properties;

    @Before
    public void beforeTests() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventName");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }
  
    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("eventName");
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy).hset(anyString(), anyString(), anyString());
    }
  
    @Test
    public void testRemoveEventHandler() {
        String name = "eventName";
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1L);
        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisProxy).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        Assert.assertNotNull("Failure - expected to not be null", eventHandlers);
        Assert.assertEquals("Failure - expected size to be 0", 0, eventHandlers.size());
    }
  
    @Test
    public void testGetEventHandlersForEvent() {
        String event = "eventName";
        when(jedisProxy.smembers(anyString())).thenReturn(new HashSet<>());
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        Assert.assertNotNull("Failure - expected to not be null", eventHandlers);
        Assert.assertEquals("Failure - expected size to be 0", 0, eventHandlers.size());
    }
}
