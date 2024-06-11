import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent", true);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("testEventHandler", "testEvent", true);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy).hset(any(), any(), any());
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testEventHandler";

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy).hdel(any(), any());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertNotNull(eventHandlers);
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        boolean activeOnly = true;

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);

        assertNotNull(eventHandlers);
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandler() {
        String name = "testEventHandler";

        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(name);

        assertNotNull(eventHandler);
        assertEquals(name, eventHandler.getName());
    }
}