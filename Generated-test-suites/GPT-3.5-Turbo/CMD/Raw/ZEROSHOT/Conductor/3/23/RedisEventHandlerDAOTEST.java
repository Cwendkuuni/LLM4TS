import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.common.metadata.events.EventHandler.Action;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        // Initialization or mocking if needed
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setAction(Action.start_workflow);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Assert the behavior or verify interactions
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setAction(Action.start_workflow);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        // Assert the behavior or verify interactions
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testEventHandler";

        redisEventHandlerDAO.removeEventHandler(name);

        // Assert the behavior or verify interactions
        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Assert the returned list is not null or empty
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        boolean activeOnly = true;

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        // Assert the returned list is not null
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandler() {
        String name = "testEventHandler";

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(name);

        // Assert the returned event handler is not null
        assertNotNull(eventHandler);
    }
}