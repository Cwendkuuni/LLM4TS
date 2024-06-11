import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.exceptions.JedisException;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(anyString(), eq("testEventHandler"))).thenReturn("{\"name\":\"testEventHandler\"}");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(anyString(), eq("testEventHandler"))).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test
    public void testRemoveEventHandlerFound() {
        when(jedisProxy.hget(anyString(), eq("testEventHandler"))).thenReturn("{\"name\":\"testEventHandler\"}");

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("testEventHandler", "{\"name\":\"testEventHandler\"}"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
        assertEquals("testEventHandler", eventHandlers.get(0).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("testEventHandler"));
        when(jedisProxy.hget(anyString(), eq("testEventHandler"))).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"testEvent\"}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
        assertEquals("testEventHandler", eventHandlers.get(0).getName());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerException() {
        when(jedisProxy.hget(anyString(), eq("testEventHandler"))).thenThrow(new JedisException("Jedis Exception"));

        redisEventHandlerDAO.getEventHandler("testEventHandler");
    }

}