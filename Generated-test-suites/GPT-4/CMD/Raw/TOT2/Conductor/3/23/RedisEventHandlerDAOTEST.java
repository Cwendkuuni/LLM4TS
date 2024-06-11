import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import com.netflix.conductor.core.config.ConductorProperties;

@RunWith(SpringRunner.class)
public class RedisEventHandlerDAOTest {
    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        // given
        EventHandler testEvent = new EventHandler();
        testEvent.setName("testEvent");

        // when
        redisEventHandlerDAO.addEventHandler(testEvent);

        // then
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler testEvent = new EventHandler();
        testEvent.setName("testEvent");

        when(redisEventHandlerDAO.getEventHandler(anyString()))
        .thenReturn(testEvent);

        redisEventHandlerDAO.addEventHandler(testEvent);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler testEvent = new EventHandler();
        testEvent.setName("testEventNotFound");

        redisEventHandlerDAO.updateEventHandler(testEvent);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler testEvent = new EventHandler();
        testEvent.setName("testEvent");

        when(redisEventHandlerDAO.getEventHandler(anyString()))
        .thenReturn(testEvent);

        redisEventHandlerDAO.updateEventHandler(testEvent);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        String testName = "testEventNotFound";

        redisEventHandlerDAO.removeEventHandler(testName);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler testEvent = new EventHandler();
        testEvent.setName("testEvent");

        when(redisEventHandlerDAO.getEventHandler(anyString()))
        .thenReturn(testEvent);

        redisEventHandlerDAO.removeEventHandler(testEvent.getName());

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy, times(1)).hgetAll(anyString());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String testEventName = "testEvent";
        boolean activeOnly = false;

        when(jedisProxy.smembers(anyString()))
        .thenReturn(new HashSet<>(Arrays.asList(testEventName)));

        redisEventHandlerDAO.getEventHandlersForEvent(testEventName, activeOnly);

        verify(jedisProxy, times(1)).smembers(anyString());
        verify(jedisProxy, times(1)).hget(anyString(), anyString());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandler_Exception() {
        String testName = "testEvent";

        when(jedisProxy.hget(anyString(), anyString()))
        .thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler(testName);
    }

    @Test
    public void testGetEventHandler() {
        String testName = "testEvent";

        when(jedisProxy.hget(anyString(), anyString()))
        .thenReturn("{\"name\": \""+ testName +"\", \"event\": \"testEvent\", \"active\": true}");

        redisEventHandlerDAO.getEventHandler(testName);
    }
}