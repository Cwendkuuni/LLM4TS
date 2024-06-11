
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedList;
import java.util.List;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties properties;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper(); // normally we would mock this, but ObjectMapper has too many final methods
        conductorProperties = mock(ConductorProperties.class);
        properties = mock(RedisProperties.class);
        
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }
    
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        when(jedisProxy.hget(any(), any())).thenReturn("test");

        assertThrows(ConflictException.class, ()->redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("name123");

        when(jedisProxy.hget(any(), any())).thenReturn(null);

        assertThrows(NotFoundException.class, ()->redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        String name = "name123";
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        assertThrows(NotFoundException.class, ()->redisEventHandlerDAO.removeEventHandler(name));
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> testHandlers = new LinkedList<>();
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        testHandlers.add(eventHandler);

        when(jedisProxy.hgetAll(any())).thenReturn(testHandlers);

        List<EventHandler> returnedHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(testHandlers, returnedHandlers);
    }

    @Test
    public void testGetEventHandlerFail() {
        String name = "name123";

        when(jedisProxy.hget(any(), any())).thenThrow(new RuntimeException());

        assertThrows(TransientException.class, ()->redisEventHandlerDAO.getEventHandler(name));
    }

    // Add tests for the remaining methods
}
