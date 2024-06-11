import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    @Mock
    JedisProxy jedisProxy;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    RedisProperties properties;

    RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_NAME = "testEvent";

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        properties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent("test");

        doReturn(null).when(jedisProxy).hget(any(), any());
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent("test");

        doReturn(toJson(eventHandler)).when(jedisProxy).hget(any(), any());
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        doReturn(null).when(jedisProxy).hget(any(), any());
        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);
        eventHandler.setEvent("test");

        doReturn(toJson(eventHandler)).when(jedisProxy).hget(any(), any());
        redisEventHandlerDAO.removeEventHandler(EVENT_NAME);

        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test
    public void testGetAllEventHandlers() {
        doReturn(Map.of(EVENT_NAME, toJson(new EventHandler()))).when(jedisProxy).hgetAll(any());
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        doReturn(Set.of(EVENT_NAME)).when(jedisProxy).smembers(any());
        doReturn(toJson(new EventHandler())).when(jedisProxy).hget(any(), any());
        
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("test", false);

        assertEquals(1, result.size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventNotFound() {
        doReturn(Set.of(EVENT_NAME)).when(jedisProxy).smembers(any());
        doThrow(new NotFoundException()).when(jedisProxy).hget(any(), any());
        
        redisEventHandlerDAO.getEventHandlersForEvent("test", true);
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_NAME);

        doReturn(toJson(eventHandler)).when(jedisProxy).hget(any(), any());
        
        EventHandler result = redisEventHandlerDAO.getEventHandler(EVENT_NAME);

        assertNotNull(result);
        assertEquals(EVENT_NAME, result.getName());
    }
}