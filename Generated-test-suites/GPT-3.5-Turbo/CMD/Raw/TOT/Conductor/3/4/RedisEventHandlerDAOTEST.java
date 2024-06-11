import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private EventHandlerDAO eventHandlerDAO;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        when(eventHandlerDAO.getEventHandler("TestEventHandler")).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), anyString(), anyString());
        verify(jedisProxy).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        when(eventHandlerDAO.getEventHandler("TestEventHandler")).thenReturn(eventHandler);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), anyString(), anyString());
        verify(jedisProxy).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestEventHandler";

        when(eventHandlerDAO.getEventHandler(eventName)).thenReturn(new EventHandler());

        redisEventHandlerDAO.removeEventHandler(eventName);

        verify(jedisProxy).hdel(anyString(), eq(eventName));
        verify(jedisProxy).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("EventHandler1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("EventHandler2");

        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("EventHandler1", "json1", "EventHandler2", "json2"));

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "TestEvent";

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("EventHandler1"));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        String event = "TestEvent";

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("EventHandler1"));
        when(eventHandlerDAO.getEventHandler(anyString())).thenThrow(new NotFoundException("EventHandler not found"));

        redisEventHandlerDAO.getEventHandlersForEvent(event, true);
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "TestEventHandler";
        String json = "json";

        when(jedisProxy.hget(anyString(), eq(eventName))).thenReturn(json);

        EventHandler result = redisEventHandlerDAO.getEventHandler(eventName);

        assertNotNull(result);
    }

    @Test
    public void testGetEventHandlerNull() {
        String eventName = "TestEventHandler";

        when(jedisProxy.hget(anyString(), eq(eventName))).thenReturn(null);

        EventHandler result = redisEventHandlerDAO.getEventHandler(eventName);

        assertNull(result);
    }

}