// Import statements
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

// Test class declaration
public class RedisEventHandlerDAOTest {

    // Test data
    private static final String EVENT_NAME = "testEvent";
    private static final EventHandler TEST_EVENT_HANDLER = new EventHandler("testHandler", EVENT_NAME, true);

    // Initialize RedisEventHandlerDAO object
    RedisEventHandlerDAO redisEventHandlerDAO;

    // Setup method
    @Before
    public void setup() {
        JedisProxy jedisProxy = new JedisProxy();
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties properties = new RedisProperties();

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        // Add event handler
        redisEventHandlerDAO.addEventHandler(TEST_EVENT_HANDLER);

        // Get all event handlers
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Check if added event handler is present in the list
        assertTrue(eventHandlers.contains(TEST_EVENT_HANDLER));
    }

    @Test
    public void testUpdateEventHandler() {
        // Update event handler
        TEST_EVENT_HANDLER.setActive(false);
        redisEventHandlerDAO.updateEventHandler(TEST_EVENT_HANDLER);

        // Get updated event handler
        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER.getName());

        // Check if event handler is updated
        assertFalse(updatedEventHandler.isActive());
    }

    @Test
    public void testRemoveEventHandler() {
        // Remove event handler
        redisEventHandlerDAO.removeEventHandler(TEST_EVENT_HANDLER.getName());

        // Get all event handlers
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Check if removed event handler is not present in the list
        assertFalse(eventHandlers.contains(TEST_EVENT_HANDLER));
    }

    @Test
    public void testGetAllEventHandlers() {
        // Get all event handlers
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Check if the list is not null
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Get event handlers for a specific event
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);

        // Check if the list is not null
        assertNotNull(eventHandlers);
    }

    @Test
    public void testGetEventHandler() {
        // Get event handler by name
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(TEST_EVENT_HANDLER.getName());

        // Check if the retrieved event handler is the same as the added event handler
        assertEquals(TEST_EVENT_HANDLER.getName(), eventHandler.getName());
    }

}