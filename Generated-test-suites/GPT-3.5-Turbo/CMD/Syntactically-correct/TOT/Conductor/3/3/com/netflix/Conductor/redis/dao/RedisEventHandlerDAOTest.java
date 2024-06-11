import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
        EventHandler eventHandler = new EventHandler("test_event", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_event_handler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "test_event";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_event_handler");

        redisEventHandlerDAO.removeEventHandler(eventName);
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(new EventHandler("test_event_1", true));
        eventHandlers.add(new EventHandler("test_event_2", true));
        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "test_event";
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(new EventHandler("test_event_1", true));
        eventHandlers.add(new EventHandler("test_event_2", true));
        when(jedisProxy.smembers(anyString())).thenReturn(eventHandlers);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "test_event";
        EventHandler eventHandler = new EventHandler("test_event", true);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandler);

        EventHandler result = redisEventHandlerDAO.getEventHandler(eventName);
        assertNotNull(result);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistentEventHandler() {
        String eventName = "non_existent_event";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventName);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNonExistentEventHandler() {
        String eventName = "non_existent_event";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(eventName);
    }

}