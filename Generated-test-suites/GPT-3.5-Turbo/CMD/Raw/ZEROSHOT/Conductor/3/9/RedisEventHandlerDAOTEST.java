import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setEvent("test_event");

        when(jedisProxy.hset(eq("EVENT_HANDLERS"), eq("test"), anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(eq("EVENT_HANDLERS"), eq("test"), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        eventHandler.setEvent("test_event");

        when(jedisProxy.hget(eq("EVENT_HANDLERS"), eq("test"))).thenReturn("existing_event_handler");
        when(jedisProxy.hset(eq("EVENT_HANDLERS"), eq("test"), anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hget(eq("EVENT_HANDLERS"), eq("test"));
        verify(jedisProxy, times(1)).hset(eq("EVENT_HANDLERS"), eq("test"), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(eq("EVENT_HANDLERS"), eq("test"))).thenReturn("existing_event_handler");
        when(jedisProxy.hdel(eq("EVENT_HANDLERS"), eq("test"))).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("test");

        verify(jedisProxy, times(1)).hget(eq("EVENT_HANDLERS"), eq("test"));
        verify(jedisProxy, times(1)).hdel(eq("EVENT_HANDLERS"), eq("test"));
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(eq("EVENT_HANDLERS"))).thenReturn(new HashMap<>());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(eq("EVENT_HANDLERS"));
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(eq("EVENT_HANDLERS_BY_EVENT_test_event"))).thenReturn(new HashSet<>());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test_event", false);

        verify(jedisProxy, times(1)).smembers(eq("EVENT_HANDLERS_BY_EVENT_test_event"));
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(eq("EVENT_HANDLERS"), eq("test"))).thenReturn("test_event_handler");

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("test");

        verify(jedisProxy, times(1)).hget(eq("EVENT_HANDLERS"), eq("test"));
        assertEquals("test_event_handler", eventHandler);
    }
}