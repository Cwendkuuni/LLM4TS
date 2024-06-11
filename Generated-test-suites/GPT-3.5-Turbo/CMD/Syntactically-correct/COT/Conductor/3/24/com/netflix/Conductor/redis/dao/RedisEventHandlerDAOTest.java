import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("testName"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals("testEvent", redisEventHandlerDAO.getEventHandler("testName").getEvent());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("testName");
        assertNull(redisEventHandlerDAO.getEventHandler("testName"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testName1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testName2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        assertTrue(redisEventHandlerDAO.getAllEventHandlers().size() == 2);
        assertTrue(redisEventHandlerDAO.getAllEventHandlers().contains(eventHandler1));
        assertTrue(redisEventHandlerDAO.getAllEventHandlers().contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testName1");
        eventHandler1.setEvent("testEvent");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testName2");
        eventHandler2.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        assertTrue(redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true).contains(eventHandler1));
        assertTrue(redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true).contains(eventHandler2));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("testName"));
    }

    @Test
    public void testAddEventHandlerConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandlerNotFoundException() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("nonExistingName"));
    }

    @Test
    public void testGetEventHandlerTransientException() {
        assertThrows(TransientException.class, () -> redisEventHandlerDAO.getEventHandler("nonExistingName"));
    }

    @Test
    public void testGetEventHandlersForEventNoMatchingEventHandler() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent("nonExistingEvent", true));
    }
}