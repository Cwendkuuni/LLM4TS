
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

public class RedisEventHandlerDAOTest {
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO dao;
    private EventHandler handler;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        handler = new EventHandler();
        handler.setName("testHandler");
        handler.setEvent("testEvent");
        dao = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), 
               new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.addEventHandler(handler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddExistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_handler");
        dao.addEventHandler(handler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_handler");
        dao.updateEventHandler(handler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.updateEventHandler(handler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistingEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        dao.removeEventHandler("testHandler");
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_handler");
        dao.removeEventHandler("testHandler");
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> data = new HashMap<>();
        data.put("handler1", "data1");
        data.put("handler2", "data2");
        when(jedisProxy.hgetAll(anyString())).thenReturn(data);
        List<EventHandler> result = dao.getAllEventHandlers();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Set<String> handlerNames = new HashSet<>();
        handlerNames.add("handler1");
        when(jedisProxy.smembers(anyString())).thenReturn(handlerNames);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing_handler");
        List<EventHandler> result = dao.getEventHandlersForEvent("testEvent", false);
        assertEquals(1, result.size());
    }
}
