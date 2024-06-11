import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Conditional;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("TestEvent", "TestHandler", true);
        Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("TestEvent", "TestHandler", true);
        Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestEvent";
        EventHandler eventHandler = new EventHandler(eventName, "TestHandler", true);
        Mockito.when(jedisProxy.hdel(Mockito.anyString(), Mockito.anyString())).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler(eventName);

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(null);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNull(handlers);
        Mockito.verify(jedisProxy, Mockito.times(1)).hgetAll(Mockito.anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        boolean activeOnly = true;
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(null);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly);

        assertNull(handlers);
        Mockito.verify(jedisProxy, Mockito.times(1)).smembers(Mockito.anyString());
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "TestEvent";
        String json = "{ \"event\": \"TestEvent\", \"name\": \"TestHandler\", \"active\": true }";
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(json);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventName);

        assertNotNull(eventHandler);
        assertEquals(eventName, eventHandler.getEvent());
        Mockito.verify(jedisProxy, Mockito.times(1)).hget(Mockito.anyString(), Mockito.anyString());
    }
}