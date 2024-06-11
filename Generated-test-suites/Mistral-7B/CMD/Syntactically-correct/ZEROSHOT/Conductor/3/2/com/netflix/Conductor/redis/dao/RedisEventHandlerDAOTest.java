import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);

        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // Clear any mocks and reset any stubs here to ensure tests are independent
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler1");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq("testEventHandler1"), Mockito.anyString()))
                .thenReturn(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals("testEventHandler1", redisEventHandlerDAO.getAllEventHandlers().get(0).getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflictingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler1");
        existingEventHandler.setEvent("testEvent");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler1");
        newEventHandler.setEvent("testEvent2");

        when(jedisProxy.hget(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS")), Mockito.eq("testEventHandler1")))
                .thenReturn("{}");
        when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IllegalStateException());

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler1");
        existingEventHandler.setEvent("testEvent");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler1");
        newEventHandler.setEvent("testEvent2");

        when(jedisProxy.hget(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS")), Mockito.eq("testEventHandler1")))
                .thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        assertEquals("testEventHandler1", redisEventHandlerDAO.getEventHandler("testEventHandler1").getName());
        assertEquals("testEvent2", redisEventHandlerDAO.getEventHandler("testEventHandler1").getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentEventHandler() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler1");
        newEventHandler.setEvent("testEvent2");

        when(jedisProxy.hget(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS")), Mockito.eq("testEventHandler1")))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler1");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS")), Mockito.eq("testEventHandler1")))
                .thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        when(jedisProxy.hdel(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);

        redisEventHandlerDAO.removeEventHandler("testEventHandler1");

        assertNull(redisEventHandlerDAO.getEventHandler("testEventHandler1"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentEventHandler() {
        redisEventHandlerDAO.removeEventHandler("testEventHandler1");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");

        when(jedisProxy.hgetAll(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS"))))
                .thenReturn(Map.of("testEventHandler1", "{}", "testEventHandler2", "{}"));

        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, allEventHandlers.size());
        assertEquals("testEventHandler1", allEventHandlers.get(0).getName());
        assertEquals("testEventHandler2", allEventHandlers.get(1).getName());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("testEventHandler3");
        eventHandler3.setEvent("testEvent2");
        eventHandler3.setActive(true);

        when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS_BY_EVENT", "testEvent1"))))
                .thenReturn(Arrays.asList("testEventHandler1", "testEventHandler2"));

        when(jedisProxy.smembers(Mockito.eq(redisEventHandlerDAO.namespaceKey("EVENT_HANDLERS_BY_EVENT", "testEvent2"))))
                .thenReturn(Arrays.asList("testEventHandler3"));

        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(2, eventHandlersForEvent.size());
        assertEquals("testEventHandler1", eventHandlersForEvent.get(0).getName());
        assertEquals("testEventHandler2", eventHandlersForEvent.get(1).getName());

        List<EventHandler> eventHandlersForEventActive = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);

        assertEquals(1, eventHandlersForEventActive.size());
        assertEquals("testEventHandler1", eventHandlersForEventActive.get(0).getName());
    }
}