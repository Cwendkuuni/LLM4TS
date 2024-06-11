
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private EventHandler eventHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(new EventHandler());

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("eventHandlerName1");
    }

    @Test
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler("eventHandlerName2");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(eventHandler);

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(result);
        assertEquals(eventHandlers, result);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = new ArrayList<>();
        EventHandler handler = new EventHandler();
        handler.setActive(true);
        handler.setEvent("eventType");
        eventHandlers.add(handler);

        when(jedisProxy.smembers(anyString())).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("eventType", true);

        assertNotNull(result);
        assertEquals(eventHandlers, result);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerException() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler("eventHandlerName3");
    }
}
