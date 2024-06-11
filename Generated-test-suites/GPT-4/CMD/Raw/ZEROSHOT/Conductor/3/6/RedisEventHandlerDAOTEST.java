
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new ObjectMapper();
        conductorProperties = new ConductorProperties();
        redisProperties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler_Success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testAdd");
        when(jedisProxy.hget(any(), any())).thenReturn("testJson");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler_Success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testUpdate");
        eventHandler.setActive(true);
        when(jedisProxy.hget(any(), any())).thenReturn("testJson");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testUpdate");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler_Success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testRemove");
        when(jedisProxy.hget(any(), any())).thenReturn("testJson");
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testRemove");
        when(jedisProxy.hget(any(), any())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers_Success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testGetAll");
        when(jedisProxy.hgetAll(any())).thenReturn(Arrays.asList(eventHandler));
        redisEventHandlerDAO.getAllEventHandlers();
        verify(jedisProxy, times(1)).hgetAll(any());
    }

    @Test
    public void testGetEventHandlersForEvent_Success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testGetForEvent");
        eventHandler.setActive(true);
        when(jedisProxy.smembers(any())).thenReturn(Arrays.asList(eventHandler.getName()));
        when(jedisProxy.hget(any(), any())).thenReturn("testJson");
        redisEventHandlerDAO.getEventHandlersForEvent(eventHandler.getEvent(), true);
        verify(jedisProxy, times(1)).hget(any(), any());
    }
}
