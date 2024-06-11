import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RedisEventHandlerDAO.class)
public class RedisEventHandlerDAOTest {

    @Autowired
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Autowired
    private JedisProxy jedisProxy;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConductorProperties conductorProperties;

    @Autowired
    private RedisProperties redisProperties;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // Clear Redis data after each test
        jedisProxy.flushDB();
    }

    // Add test case for addEventHandler method
    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertEquals(eventHandler, retrievedEventHandler);
    }

    // Add test case for updateEventHandler method
    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("updatedTestEvent");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertEquals(updatedEventHandler, retrievedEventHandler);
    }

    // Add test case for removeEventHandler method
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        redisEventHandlerDAO.getEventHandler(eventHandler.getName());
    }

    // Add test case for getAllEventHandlers method
    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    // Add test case for getEventHandlersForEvent method with active only false
    @Test
    public void testGetEventHandlersForEventWithActiveOnlyFalse() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    // Add test case for getEventHandlersForEvent method with active only true
    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
    }

    // Add test case for getEventHandler method
    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() throws Exception {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.getEventHandler("nonExistentEventHandlerName");
    }
}