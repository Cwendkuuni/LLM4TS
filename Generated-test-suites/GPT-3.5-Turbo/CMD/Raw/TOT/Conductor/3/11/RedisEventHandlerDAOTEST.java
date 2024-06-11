import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxyMock;

    @Before
    public void setup() {
        jedisProxyMock = mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("event1", true);
        
        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName())).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxyMock).hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName(), redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("event1", true);
        
        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName())).thenReturn(redisEventHandlerDAO.toJson(eventHandler));
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxyMock).hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName(), redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "event1";
        EventHandler eventHandler = new EventHandler(eventName, true);
        
        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventName)).thenReturn(redisEventHandlerDAO.toJson(eventHandler));
        
        redisEventHandlerDAO.removeEventHandler(eventName);
        
        verify(jedisProxyMock).hdel(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventName);
    }

    @Test
    public void testGetAllEventHandlers() {
        // Prepare data
        EventHandler eventHandler1 = new EventHandler("event1", true);
        EventHandler eventHandler2 = new EventHandler("event2", false);
        
        when(jedisProxyMock.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)))
                .thenReturn(Map.of(eventHandler1.getName(), redisEventHandlerDAO.toJson(eventHandler1),
                                  eventHandler2.getName(), redisEventHandlerDAO.toJson(eventHandler2)));
        
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertEquals(2, allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "event1";
        boolean activeOnly = true;
        
        when(jedisProxyMock.smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event))).thenReturn(Set.of("handler1", "handler2"));
        
        // Implement when the under test method is ready 
        
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "event1";
        EventHandler eventHandler = new EventHandler(eventName, true);
        
        when(jedisProxyMock.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventName)).thenReturn(redisEventHandlerDAO.toJson(eventHandler));
        
        EventHandler result = redisEventHandlerDAO.getEventHandler(eventName);
        
        assertNotNull(result);
    }

}