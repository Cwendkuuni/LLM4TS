package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;



public class RedisEventHandlerDAOTest {
    private RedisEventHandlerDAO dao;
    private JedisMock jedisMock;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisMock = new JedisMock();
        objectMapper = new ObjectMapper();
        conductorProperties = new ConductorProperties();
        redisProperties = new RedisProperties();
        dao = new RedisEventHandlerDAO(jedisMock, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        dao.addEventHandler(eventHandler);
        assertNotNull(dao.getEventHandler("test"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        dao.addEventHandler(eventHandler);
        dao.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        dao.addEventHandler(eventHandler);
        eventHandler.setEvent("updated");
        dao.updateEventHandler(eventHandler);
        assertEquals("updated", dao.getEventHandler("test").getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonexistingName() { 
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        dao.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");
        dao.addEventHandler(eventHandler);
        dao.removeEventHandler("test");
        assertNull(dao.getEventHandler("test"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        EventHandler eventHandler2 = new EventHandler();
        eventHandler1.setName("test1");
        eventHandler2.setName("test2");
        dao.addEventHandler(eventHandler1);
        dao.addEventHandler(eventHandler2);
        assertEquals(2, dao.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("test1");
        eventHandler1.setEvent("event1");
        dao.addEventHandler(eventHandler1);
        assertEquals(1, dao.getEventHandlersForEvent("event1", false).size());
    }
}

