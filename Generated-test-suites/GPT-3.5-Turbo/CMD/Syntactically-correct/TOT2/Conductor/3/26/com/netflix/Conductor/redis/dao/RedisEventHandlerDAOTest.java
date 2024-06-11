import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Test if event handler is successfully added
        assertNotNull(redisEventHandlerDAO.getEventHandler("testEvent"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("");
        doNothing().when(jedisProxy).hset(anyString(), anyString(), anyString());
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        // Test if event handler is successfully updated
        assertNotNull(redisEventHandlerDAO.getEventHandler("testEvent"));
    }

    @Test
    public void testRemoveEventHandler() {
        String eventName = "testEvent";

        when(jedisProxy.hget(anyString(), anyString())).thenReturn("");
        doNothing().when(jedisProxy).hdel(anyString(), anyString());

        redisEventHandlerDAO.removeEventHandler(eventName);

        // Test if event handler is successfully removed
        assertNull(redisEventHandlerDAO.getEventHandler("testEvent"));
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(null);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Test if all event handlers list is not null
        assertNotNull(allEventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "testEvent";
        boolean activeOnly = true;

        redisEventHandlerDAO.addEventHandler(new EventHandler("testEventHandler", eventName));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, activeOnly);

        // Test if event handlers list is not null
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandler() {
        String eventName = "testEvent";

        redisEventHandlerDAO.addEventHandler(new EventHandler("testEventHandler", eventName));

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventName);

        // Test if the fetched event handler is not null
        assertNotNull(eventHandler);
    }

}