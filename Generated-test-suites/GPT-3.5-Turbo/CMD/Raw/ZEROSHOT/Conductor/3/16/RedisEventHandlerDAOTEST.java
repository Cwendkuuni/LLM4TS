import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private EventHandlerDAO eventHandlerDAO;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler testEventHandler;

    @Before
    public void setUp() {
        testEventHandler = new EventHandler();
        testEventHandler.setName("TestEventHandler");
    }

    @Test
    public void testAddEventHandler() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(testEventHandler);

        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        when(jedisProxy.hget(any(), any())).thenReturn("existingEventHandler");

        redisEventHandlerDAO.addEventHandler(testEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(jedisProxy.hget(any(), any())).thenReturn("existingEventHandler");
        when(eventHandlerDAO.getEventHandler(any())).thenReturn(testEventHandler);

        redisEventHandlerDAO.updateEventHandler(testEventHandler);

        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(testEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        when(eventHandlerDAO.getEventHandler(any())).thenReturn(testEventHandler);

        redisEventHandlerDAO.removeEventHandler("TestEventHandler");

        verify(jedisProxy).hdel(any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(eventHandlerDAO.getEventHandler(any())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("NonExistingEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(any())).thenReturn(null);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(any())).thenReturn(null);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);

        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler("TestEventHandler");

        assertEquals(null, eventHandler);
    }
}