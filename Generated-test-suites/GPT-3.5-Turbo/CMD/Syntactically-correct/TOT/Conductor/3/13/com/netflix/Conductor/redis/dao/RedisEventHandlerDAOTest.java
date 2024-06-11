
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        when(jedisProxy.hget(anyString(), eq("testEvent"))).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq("testEvent"), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEvent");
        eventHandler.setEvent("test");
        when(jedisProxy.hget(anyString(), eq("testEvent"))).thenReturn("existingEventHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq("testEvent"), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxy.hget(anyString(), eq("testEvent"))).thenReturn("existingEventHandler");

        redisEventHandlerDAO.removeEventHandler("testEvent");

        verify(jedisProxy, times(1)).hdel(anyString(), eq("testEvent"));
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(mockEventHandlers());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(eventHandlers);
        assertEquals(2, eventHandlers.size());
    }

    private Map<String, String> mockEventHandlers() {
        Map<String, String> eventHandlers = new HashMap<>();
        eventHandlers.put("eventHandler1", "json1");
        eventHandlers.put("eventHandler2", "json2");
        return eventHandlers;
    }
}

