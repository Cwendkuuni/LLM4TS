
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    // Dummy EventHandler Object
    EventHandler eventHandler = new EventHandler("testEvent", "testCondition", "testAction", false, false, 0, 0, 0, CoordinatorType.SIMPLE, null);

    @Test
    public void testAddEventHandler_Success() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Existing() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("existingHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler_Success() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("existingHandler");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler_Success() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("existingHandler");
        redisEventHandlerDAO.removeEventHandler("testEvent");
        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("testEvent");
    }

    @Test
    public void testGetAllEventHandlers_Success() {
        Map<String, String> allHandlers = new HashMap<>();
        allHandlers.put("testEvent", "handler1");
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(allHandlers);
        List<EventHandler> output = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(1, output.size());
    }

    @Test
    public void testGetEventHandlersForEvent_Success() {
        Set<String> allNames = new HashSet<>();
        allNames.add("testEvent");
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(allNames);
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("existingHandler");
        List<EventHandler> output = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(1, output.size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_NotFound() {
        Set<String> allNames = new HashSet<>();
        allNames.add("testEvent");
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(allNames);
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);
        redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
    }

}
