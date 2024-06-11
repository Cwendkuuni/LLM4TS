import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEvent");

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            fail("ConflictException was not thrown");
        } catch (ConflictException e) {
            // Exception expected
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEvent");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            fail("NotFoundException was not thrown");
        } catch (NotFoundException e) {
            // Exception expected
        }
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("existingEvent");

        redisEventHandlerDAO.removeEventHandler("TestEvent");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.removeEventHandler("TestEvent");
            fail("NotFoundException was not thrown");
        } catch (NotFoundException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);

        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());
    }
}