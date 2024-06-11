
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.core.config.ConductorProperties;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("eventHandlerName");
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("eventHandlerName");
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventHandler.getName())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), "eventHandlerName")).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("eventHandlerName");
    }

    @Test
    public void testGetAllEventHandlers() {
        String json = "{\"eventName\":\"TestEventHandler\", \"status\":\"Active\"}";
        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"))).thenReturn(Arrays.asList(json, json));
        LinkedList<EventHandler> actual = redisEventHandlerDAO.getAllEventHandlers();
        
        assertEquals(2, actual.size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerForEventNotFound() {
        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey("EVENT_HANDLERS_BY_EVENT"), "Event")).thenReturn(null);
        redisEventHandlerDAO.getEventHandlersForEvent("Event", true);
    }

    @Test(expected = TransientException.class)
    public void getHandlerTransientException() {
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), "EventHandlerName")).thenThrow(TransientException.class);
        redisEventHandlerDAO.getEventHandler("EventHandlerName");
    }
}
