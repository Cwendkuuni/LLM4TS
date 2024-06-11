import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        RedisProperties redisProperties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, null, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("ExistingEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existing = new EventHandler();
        existing.setName("TestEvent");
        EventHandler updated = new EventHandler();
        updated.setName("TestEvent");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(updated);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler updated = new EventHandler();
        updated.setName("NonExistingEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updated);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler("TestEvent");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("NonExistingEvent");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(handlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(null);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);

        assertNotNull(handlers);
    }

    @Test
    public void testGetEventHandlersForEventInactive() {
        when(jedisProxy.smembers(anyString())).thenReturn(null);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", false);

        assertNotNull(handlers);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        when(jedisProxy.smembers(anyString())).thenReturn(Arrays.asList("NonExistingEvent"));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        redisEventHandlerDAO.getEventHandlersForEvent("NonExistingEvent", true);
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("TestEvent");

        assertNotNull(eventHandler);
    }

    @Test(expected = Exception.class)
    public void testGetEventHandlerException() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new Exception());

        redisEventHandlerDAO.getEventHandler("TestEvent");
    }
}