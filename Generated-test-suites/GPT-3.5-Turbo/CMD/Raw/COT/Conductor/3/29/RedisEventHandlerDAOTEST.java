import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(new JedisProxy(), new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event_handler", "test_event", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("test_event_handler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("test_event_handler", "test_event", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertFalse(redisEventHandlerDAO.getEventHandler("test_event_handler").isActive());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.removeEventHandler("non_existing_event_handler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(eventHandlers.isEmpty());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("test_event", true);
        assertTrue(eventHandlers.isEmpty());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerError() {
        redisEventHandlerDAO.getEventHandler("invalid_event_handler_name");
    }
}