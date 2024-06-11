import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "TestEventHandler";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing");

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "TestEventHandler";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> expected = new ArrayList<>();
        expected.add(new EventHandler());
        
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);

        List<EventHandler> actual = redisEventHandlerDAO.getAllEventHandlers();

        assertNull(actual);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "TestEvent";
        boolean activeOnly = false;

        List<EventHandler> expected = new ArrayList<>();
        expected.add(new EventHandler());
        
        when(jedisProxy.smembers(anyString())).thenReturn(null);
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existing");

        List<EventHandler> actual = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }
}