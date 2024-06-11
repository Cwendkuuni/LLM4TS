import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String eventName = "eventName";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test(expected = ConflictException.class)
    public void shouldThrowConflictWhenAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventName);
        when(jedisProxy.hget(any(), eq(eventName))).thenReturn("{\"name\":\"eventName\"}");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenUpdateEventThatDoesNotExist() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(eventName);
        when(jedisProxy.hget(any(), eq(eventName))).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenRemoveEventThatDoesNotExist() {
        when(jedisProxy.hget(any(), eq(eventName))).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(eventName);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenGetEventHandlersForEventButNotFound() {
        when(jedisProxy.smembers(any(), eq(eventName))).thenReturn(null);
        redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
    }

    @Test(expected = TransientException.class)
    public void shouldThrowTransientExceptionWhenGetEventThatThrowsException() {
        when(jedisProxy.hget(any(), eq(eventName))).thenThrow(new IllegalArgumentException());
        redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
    }
}