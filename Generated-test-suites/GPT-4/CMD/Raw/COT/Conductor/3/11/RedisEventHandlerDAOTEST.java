import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private RedisProperties properties;

    @Mock
    private ConductorProperties conductorProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;

    @Before
    public void setUp() {
        eventHandler = mock(EventHandler.class);
        when(eventHandler.getName()).thenReturn("testName");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_ConflictException() {
        when(redisEventHandlerDAO.getEventHandler(any())).thenReturn(new EventHandler());
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFoundException() {
        when(redisEventHandlerDAO.getEventHandler(any())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFoundException() {
        when(redisEventHandlerDAO.getEventHandler(any())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("name1");
    }

    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.getAllEventHandlers();
        verify(redisEventHandlerDAO, times(1)).getAllEventHandlers();
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_NotFoundException() {
        when(jedisProxy.smembers(any())).thenReturn(null);
        redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
    }
}