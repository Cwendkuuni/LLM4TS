import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private RedisProperties properties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper();
        properties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, null, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Test");
        when(jedisProxy.hget(null, "Test")).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(eventHandler.getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Test");
        when(jedisProxy.hget(null, "Test")).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Test");
        when(jedisProxy.hget(null, "Test")).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertNotNull(eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Test");
        when(jedisProxy.hget(null, "Test")).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(null, "Test")).thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler("Test");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(null, "Test")).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("Test");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(null)).thenReturn(new ArrayList<>());

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNotNull(handlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(null)).thenReturn(new ArrayList<>());

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("Test", true);
        assertNotNull(handlers);
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(null, "Test")).thenReturn("{}");

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("Test");
        assertNotNull(eventHandler);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(null, "Test")).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler("Test");
    }
}