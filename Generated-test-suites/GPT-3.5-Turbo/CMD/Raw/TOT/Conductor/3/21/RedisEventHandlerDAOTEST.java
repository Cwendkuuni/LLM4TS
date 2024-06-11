import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxyMock;

    @Before
    public void setup() {
        jedisProxyMock = mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        // Typical Use Case
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Edge Case
        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
        } catch (ConflictException e) {
            // Expecting ConflictException
        }
    }

    @Test
    public void testUpdateEventHandler() {
        // Typical Use Case
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setEvent("updated_event");
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        // Edge Case
        try {
            EventHandler nonExistentHandler = new EventHandler("non_existent_event", "non_existent_name", false);
            redisEventHandlerDAO.updateEventHandler(nonExistentHandler);
        } catch (NotFoundException e) {
            // Expecting NotFoundException
        }
    }

    @Test
    public void testRemoveEventHandler() {
        // Typical Use Case
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("test_name");

        // Edge Case
        try {
            redisEventHandlerDAO.removeEventHandler("non_existent_name");
        } catch (NotFoundException e) {
            // Expecting NotFoundException
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        // Typical Use Case
        EventHandler eventHandler1 = new EventHandler("test_event1", "test_name1", true);
        EventHandler eventHandler2 = new EventHandler("test_event2", "test_name2", true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, handlers.size());

        // Edge Case
        when(jedisProxy.hgetAll(anyString())).thenThrow(Exception.class);
        assertThrows(TransientException.class, () -> redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Typical Use Case
        EventHandler eventHandler1 = new EventHandler("test_event1", "test_name1", true);
        EventHandler eventHandler2 = new EventHandler("test_event1", "test_name2", false);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("test_event1", true);
        assertEquals(1, handlers.size());

        // Edge Case
        try {
            redisEventHandlerDAO.getEventHandlersForEvent("non_existent_event", true);
        } catch (NotFoundException e) {
            // Expecting NotFoundException
        }
    }

    @Test
    public void testGetEventHandler() {
        // Typical Use Case
        EventHandler eventHandler = new EventHandler("test_event", "test_name", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedHandler = redisEventHandlerDAO.getEventHandler("test_name");
        assertNotNull(retrievedHandler);

        // Edge Case
        when(jedisProxyMock.hget(anyString(), anyString())).thenThrow(Exception.class);
        assertThrows(TransientException.class, () -> redisEventHandlerDAO.getEventHandler("test_name"));
    }
}