import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;
import java.util.ArrayList;
import java.util.List;

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
    private RedisEventHandlerDAO dao;

    private EventHandler handler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new EventHandler();
        handler.setName("TestHandler");
    }

    @Test 
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.addEventHandler(handler);
        verify(jedisProxy, times(1)).hset(anyString(),anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExisting() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Handler");
        dao.addEventHandler(handler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Handler");
        dao.updateEventHandler(handler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNonExisting() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.updateEventHandler(handler);
    }

    @Test
    public void testRemoveEventHandler() {
       when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Handler");
       dao.removeEventHandler("TestHandler");
       verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNonExisting() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.removeEventHandler("TestHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = dao.getAllEventHandlers();
        assertNotNull(handlers);
    }

    @Test 
    public void testGetEventHandlersForEvent() {
        List<EventHandler> handlers = dao.getEventHandlersForEvent("TestEvent", false);
        assertNotNull(handlers);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNone() {
        List<EventHandler> handlers = dao.getEventHandlersForEvent("NonExistentEvent", false);
    }
}