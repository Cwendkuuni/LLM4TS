import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;

    @Before
    public void setup() {
        eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        redisEventHandlerDAO.removeEventHandler("testName");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
        verify(jedisProxy, times(1)).srem(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNull(handlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(null);
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertNull(handlers);
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{}");
        EventHandler handler = redisEventHandlerDAO.getEventHandler("testName");

        assertNotNull(handler);
    }
}