import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;

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
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setEvent("TEST_EVENT");
        
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), "TestEvent")).thenReturn(null);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), "TestEvent", redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setEvent("TEST_EVENT");
        
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), "TestEvent")).thenReturn(redisEventHandlerDAO.toJson(eventHandler));
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), "TestEvent", redisEventHandlerDAO.toJson(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestEvent";
        
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventName);
        eventHandler.setEvent("TEST_EVENT");
        
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenReturn(redisEventHandlerDAO.toJson(eventHandler));
        
        redisEventHandlerDAO.removeEventHandler(eventName);
        
        verify(jedisProxy, times(1)).hdel(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setEvent("TEST_EVENT");

        when(jedisProxy.hgetAll(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"))).thenReturn(null);
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventName);
        eventHandler.setEvent("TEST_EVENT");
        
        when(jedisProxy.smembers(redisEventHandlerDAO.nsKey("EVENT_HANDLERS_BY_EVENT", "TEST_EVENT"))).thenReturn(null);
        
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TEST_EVENT", false);
        
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "TestEvent";
        
        when(jedisProxy.hget(redisEventHandlerDAO.nsKey("EVENT_HANDLERS"), eventName)).thenReturn(null);
        
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventName);
        
        assertNull(eventHandler);
    }

}