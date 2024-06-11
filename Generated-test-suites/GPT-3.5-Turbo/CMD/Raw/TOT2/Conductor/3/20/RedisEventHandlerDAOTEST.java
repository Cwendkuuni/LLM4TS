import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        JedisProxy jedisProxy = Mockito.mock(JedisProxy.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties properties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertNotNull(retrievedEventHandler);
        assertEquals("testEventHandler", retrievedEventHandler.getName());
        assertEquals("testEvent", retrievedEventHandler.getEvent());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setEvent("updatedEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        EventHandler updatedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertNotNull(updatedEventHandler);
        assertEquals("testEventHandler", updatedEventHandler.getName());
        assertEquals("updatedEvent", updatedEventHandler.getEvent());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        EventHandler removedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertNull(removedEventHandler);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertTrue(allEventHandlers.stream().anyMatch(handler -> handler.getName().equals("eventHandler1")));
        assertTrue(allEventHandlers.stream().anyMatch(handler -> handler.getName().equals("eventHandler2")));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(1, eventHandlersForEvent.size());
        assertTrue(eventHandlersForEvent.get(0).isActive());
    }

    @Test
    public void testGetEventHandlersForEvent_NoMatchingEventHandler() {
        try {
            redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", true);
            fail("NotFoundException not thrown for non-existent event");
        } catch (NotFoundException e) {
            // Pass
        }
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");

        assertNotNull(retrievedEventHandler);
        assertEquals("testEventHandler", retrievedEventHandler.getName());
        assertEquals("testEvent", retrievedEventHandler.getEvent());
    }

    @Test
    public void testGetEventHandler_NonExistentHandler() {
        EventHandler nonExistentHandler = redisEventHandlerDAO.getEventHandler("nonExistentHandler");

        assertNull(nonExistentHandler);
    }

    @Test
    public void testAddEventHandler_AlreadyExisting() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("existingEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            fail("ConflictException not thrown for already existing event handler");
        } catch (ConflictException e) {
            // Pass
        }
    }

    @Test
    public void testUpdateEventHandler_NonExistent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonExistentHandler");
        eventHandler.setEvent("testEvent");

        try {
            redisEventHandlerDAO.updateEventHandler(eventHandler);
            fail("NotFoundException not thrown for non-existent event handler");
        } catch (NotFoundException e) {
            // Pass
        }
    }

}