import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.LinkedList;
import java.util.List;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(2)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = new LinkedList<>();
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(new HashMap<>());
        eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = new LinkedList<>();
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(new HashSet<>());
        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("{\"name\":\"testEvent\"}");
        eventHandler = redisEventHandlerDAO.getEventHandler("testEvent");
        assertEquals("testEvent", eventHandler.getName());
    }

}