import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

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
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);

        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("testHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);

        when(jedisProxy.hget(any(), any())).thenReturn("existingHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("testHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);

        when(jedisProxy.hget(any(), any())).thenReturn("existingHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("testHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);

        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("testHandler"));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testEvent", "testHandler", true);

        when(jedisProxy.hget(any(), any())).thenReturn("existingHandler");

        redisEventHandlerDAO.removeEventHandler("testHandler");

        assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxy.hget(any(), any())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testHandler");

        assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertNotNull(eventHandlers);
    }
}