
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.junit.Assert;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.common.metadata.events.EventHandler;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());

        Exception exception = null;
        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        } catch (Exception e) {
            exception = e;
        }
        assertTrue(exception instanceof ConflictException);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent2");

        Exception exception = null;
        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
        } catch (Exception e) {
            exception = e;
        }
        assertTrue(exception instanceof NotFoundException);

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\": \"testEventHandler\", \"event\": \"testEvent\"}");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        Exception exception = null;
        try {
            redisEventHandlerDAO.removeEventHandler("testEventHandler");
        } catch (Exception e) {
            exception = e;
        }
        assertTrue(exception instanceof NotFoundException);
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\": \"testEventHandler\", \"event\": \"testEvent\"}");
        redisEventHandlerDAO.removeEventHandler("testEventHandler");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Collections.singletonMap("testEventHandler", "{\"name\": \"testEventHandler\", \"event\": \"testEvent\"}"));
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, allEventHandlers.size());
        assertEquals("testEvent", allEventHandlers.get(0).getEvent());
        assertEquals("testEventHandler", allEventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Collections.singleton("testEventHandler"));
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("{\"name\": \"testEventHandler\", \"event\": \"testEvent\", \"active\": true}");
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(1, eventHandlers.size());
        assertEquals("testEvent", eventHandlers.get(0).getEvent());
        assertTrue(eventHandlers.get(0).isActive());
        assertEquals("testEventHandler", eventHandlers.get(0).getName());

        Exception exception = null;
        try {
            redisEventHandlerDAO.getEventHandlersForEvent("nonExistingEvent", true);
        } catch (Exception e) {
            exception = e;
        }
        assertTrue(exception instanceof NotFoundException);
    }
}
