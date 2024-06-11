import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        doReturn(null).when(jedisProxy).hget(anyString(), anyString());

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("DuplicateEventHandler");

        doReturn("test").when(jedisProxy).hget(anyString(), anyString());

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        doReturn(eventHandler).when(jedisProxy).hget(anyString(), anyString());

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("NonExistentEventHandler");

        doReturn(null).when(jedisProxy).hget(anyString(), anyString());

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "TestEventHandlerName";

        doReturn(new EventHandler()).when(jedisProxy).hget(anyString(), eq(eventName));

        redisEventHandlerDAO.removeEventHandler(eventName);

        verify(jedisProxy, times(1)).hdel(anyString(), eq(eventName));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        String eventName = "NonExistentEventHandlerName";

        doReturn(null).when(jedisProxy).hget(anyString(), eq(eventName));

        redisEventHandlerDAO.removeEventHandler(eventName);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = List.of(new EventHandler(), new EventHandler());

        doReturn(eventHandlers).when(jedisProxy).hgetAll(anyString());

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, result.size());
        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        List<EventHandler> eventHandlers = List.of(new EventHandler(), new EventHandler());

        doReturn(Set.of("Handler1", "Handler2")).when(jedisProxy).smembers(anyString());
        doReturn(new EventHandler()).when(jedisProxy).hget(anyString(), anyString());

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

        assertNotNull(result);
        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(2)).hget(anyString(), anyString());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler expectedEventHandler = new EventHandler();
        expectedEventHandler.setName("TestHandler");

        doReturn("TestHandlerJson").when(jedisProxy).hget(anyString(), eq("TestHandler"));
        doReturn(expectedEventHandler).when(objectMapper).readValue(eq("TestHandlerJson"), eq(EventHandler.class));

        EventHandler result = redisEventHandlerDAO.getEventHandler("TestHandler");

        assertEquals(expectedEventHandler, result);
        verify(jedisProxy, times(1)).hget(anyString(), eq("TestHandler"));
    }
}