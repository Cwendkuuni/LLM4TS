import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;


public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    private ConductorProperties conductorProperties;

    private RedisProperties redisProperties;

    private EventHandlerDAO eventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        conductorProperties = new ConductorProperties();
        redisProperties = new RedisProperties();
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Test_Event");
        
        eventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Test_Event");
        
        eventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "Test_Event";
        
        eventHandlerDAO.removeEventHandler(eventName);
        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = eventHandlerDAO.getAllEventHandlers();
        
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "Test_Event";
        boolean activeOnly = true;
        
        List<EventHandler> eventHandlers = eventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly);
        
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "Test_Event";
        
        EventHandler eventHandler = eventHandlerDAO.getEventHandler(eventName);
        
        assertNotNull(eventHandler);
    }
}