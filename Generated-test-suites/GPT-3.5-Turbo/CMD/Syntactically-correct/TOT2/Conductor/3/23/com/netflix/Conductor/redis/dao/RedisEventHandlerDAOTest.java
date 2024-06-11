import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        JedisProxy jedisProxy = mock(JedisProxy.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        RedisProperties properties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        // Test adding a new event handler
        EventHandler eventHandler = new EventHandler("test_event", "test_handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("test_handler"));
    }

    @Test
    public void testUpdateEventHandler() {
        // Test updating an existing event handler
        EventHandler eventHandler = new EventHandler("test_event", "test_handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("test_handler");
        assertFalse(updatedEventHandler.isActive());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        // Test removing an existing event handler
        EventHandler eventHandler = new EventHandler("test_event", "test_handler", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("test_handler");
        redisEventHandlerDAO.getEventHandler("test_handler"); // This should throw NotFoundException
    }

    @Test
    public void testGetAllEventHandlers() {
        // Add some event handlers and test getting all of them
        EventHandler eventHandler1 = new EventHandler("event1", "handler1", true);
        EventHandler eventHandler2 = new EventHandler("event2", "handler2", false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, allEventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Add event handlers for a specific event and test getting them back
        EventHandler eventHandler1 = new EventHandler("event1", "handler1", true);
        EventHandler eventHandler2 = new EventHandler("event1", "handler2", false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        assertEquals(1, eventHandlers.size());
        assertEquals("handler1", eventHandlers.get(0).getName());
    }
}