import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;

    @Before
    public void setup() {
        eventHandler = new EventHandler();
        eventHandler.setName("Test Event Handler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget("EVENT_HANDLERS", "Test Event Handler")).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("Test Event Handler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(jedisProxy.hget("EVENT_HANDLERS", "Test Event Handler")).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget("EVENT_HANDLERS", "Test Event Handler")).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler("Test Event Handler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(jedisProxy.hget("EVENT_HANDLERS", "Test Event Handler")).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget("EVENT_HANDLERS", "Test Event Handler")).thenReturn("{}");

        redisEventHandlerDAO.removeEventHandler("Test Event Handler");

        assertNull(redisEventHandlerDAO.getEventHandler("Test Event Handler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget("EVENT_HANDLERS", "Test Event Handler")).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("Test Event Handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll("EVENT_HANDLERS")).thenReturn(null);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers("EVENT_HANDLERS_BY_EVENT_testEvent")).thenReturn(null);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertNotNull(eventHandlers);
        assertTrue(eventHandlers.isEmpty());
    }

}