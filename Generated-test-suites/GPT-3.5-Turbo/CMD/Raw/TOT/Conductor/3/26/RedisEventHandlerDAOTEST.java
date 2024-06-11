import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        JedisProxy jedisProxy = mock(JedisProxy.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        RedisProperties redisProperties = mock(RedisProperties.class);

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler("testEvent", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("testEvent"));

        // Edge case
        EventHandler nullEventHandler = new EventHandler(null, true);
        try {
            redisEventHandlerDAO.addEventHandler(nullEventHandler);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Name", e.getMessage());
        }

        // Error scenario
        EventHandler duplicateEventHandler = new EventHandler("testEvent", true);
        try {
            redisEventHandlerDAO.addEventHandler(duplicateEventHandler);
        } catch (ConflictException e) {
            assertEquals("EventHandler with name testEvent already exists!", e.getMessage());
        }
    }

    @Test
    public void testUpdateEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler("updateTestEvent", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setActive(false);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals(false, redisEventHandlerDAO.getEventHandler("updateTestEvent").isActive());

        // Edge case
        EventHandler nullEventHandler = new EventHandler(null, true);
        try {
            redisEventHandlerDAO.updateEventHandler(nullEventHandler);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Name", e.getMessage());
        }

        // Error scenario
        EventHandler nonExistingEventHandler = new EventHandler("nonExistingEvent", true);
        try {
            redisEventHandlerDAO.updateEventHandler(nonExistingEventHandler);
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name nonExistingEvent not found!", e.getMessage());
        }
    }

    @Test
    public void testRemoveEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler("removeTestEvent", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("removeTestEvent");
        assertNull(redisEventHandlerDAO.getEventHandler("removeTestEvent"));

        // Edge case
        try {
            redisEventHandlerDAO.removeEventHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Name", e.getMessage());
        }

        // Error scenario
        try {
            redisEventHandlerDAO.removeEventHandler("nonExistingEvent");
        } catch (NotFoundException e) {
            assertEquals("EventHandler with name nonExistingEvent not found!", e.getMessage());
        }
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Typical use case
        EventHandler eventHandler1 = new EventHandler("event1", true);
        EventHandler eventHandler2 = new EventHandler("event1", false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(2, redisEventHandlerDAO.getEventHandlersForEvent("event1", true).size());

        // Edge case
        try {
            redisEventHandlerDAO.getEventHandlersForEvent(null, true);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing Event", e.getMessage());
        }

        // Error scenario
        try {
            redisEventHandlerDAO.getEventHandlersForEvent("nonExistingEvent", true);
        } catch (NotFoundException e) {
            assertEquals("No matching event handler found for event: nonExistingEvent", e.getMessage());
        }
    }

    @Test
    public void testGetAllEventHandlers() {
        // Typical use case
        EventHandler eventHandler1 = new EventHandler("event1", true);
        EventHandler eventHandler2 = new EventHandler("event2", true);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(2, redisEventHandlerDAO.getAllEventHandlers().size());

        // Edge case
        // No edge case for this method

        // Error scenario
        // No error scenario for this method
    }
}