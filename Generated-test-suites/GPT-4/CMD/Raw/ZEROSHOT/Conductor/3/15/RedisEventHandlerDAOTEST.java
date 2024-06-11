
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;

import java.util.List;

import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        objectMapper = mock(ObjectMapper.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler");

        when(jedisProxy.hset(any(), any(), any())).thenReturn(1L);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler");

        when(jedisProxy.hset(any(), any(), any())).thenReturn(0L);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"handler\"}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("handler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"handler\"}");
        redisEventHandlerDAO.removeEventHandler("handler");
        verify(jedisProxy).hdel(any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(any())).thenReturn(Collections.singletonMap("handler1", "{\"name\":\"handler1\"}"));
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(any())).thenReturn(new HashSet<String>(Arrays.asList("handler1")));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\":\"handler1\", \"event\":\"event1\", \"active\": true}");
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        assertNotNull(handlers);
        assertEquals(1, handlers.size());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlersForEventException() {
        when(jedisProxy.smembers(any())).thenReturn(new HashSet<String>(Arrays.asList("handler1")));
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
        redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
    }
}
