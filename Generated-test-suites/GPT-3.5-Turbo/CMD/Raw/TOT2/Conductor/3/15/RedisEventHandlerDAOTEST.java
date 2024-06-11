import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");

        doReturn(null).when(redisEventHandlerDAO).getEventHandler("test-event-handler");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test-event-handler");

        doReturn(eventHandler).when(redisEventHandlerDAO).getEventHandler("test-event-handler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        doReturn(new EventHandler()).when(redisEventHandlerDAO).getEventHandler(anyString());

        redisEventHandlerDAO.removeEventHandler("test-event-handler");

        verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> data = Map.of("test-event-handler", "{\"name\":\"test-event-handler\"}");
        doReturn(data).when(jedisProxy).hgetAll(anyString());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(eventHandlers);
        assertEquals(1, eventHandlers.size());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_NotFound() {
        doReturn(null).when(jedisProxy).hget(anyString(), anyString());

        redisEventHandlerDAO.getEventHandlersForEvent("test-event", true);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandler_TransientException() {
        doThrow(new RuntimeException()).when(jedisProxy).hget(anyString(), anyString());

        redisEventHandlerDAO.getEventHandler("test-event-handler");
    }
}