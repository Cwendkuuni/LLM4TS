import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName(), redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testName", true);

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName())).thenReturn(redisEventHandlerDAO.toJson(eventHandler));

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName(), redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testName";
        EventHandler eventHandler = new EventHandler("testEvent", name, true);

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn(redisEventHandlerDAO.toJson(eventHandler));

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy).hdel(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new ArrayList<>();
        eventHandlers.add(new EventHandler("testEvent1", "testName1"));
        eventHandlers.add(new EventHandler("testEvent2", "testName2"));

        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(null);

        List<EventHandler> actual = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(0, actual.size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        String event = "testEvent";

        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event))).thenReturn(null);

        redisEventHandlerDAO.getEventHandlersForEvent(event, true);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        String name = "testName";

        when(jedisProxy.hget(redisEventHandlerDAO.nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenThrow(new RuntimeException("Redis not available"));

        redisEventHandlerDAO.getEventHandler(name);
    }

}