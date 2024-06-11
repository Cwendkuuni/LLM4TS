import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private RedisProperties redisProperties;
    private ConductorProperties conductorProperties;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        redisProperties = mock(RedisProperties.class);
        conductorProperties = mock(ConductorProperties.class);

        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        // Clear mocks
        ReflectionTestUtils.resetFields(redisEventHandlerDAO);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = createEventHandler("testHandler");

        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq(eventHandler.getName()), Mockito.anyString()))
                .thenReturn(1);
        when(jedisProxy.sadd(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(1, ReflectionTestUtils.getFieldValue(redisEventHandlerDAO, "size"));
        assertNotNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = createEventHandler("existingHandler");
        EventHandler newEventHandler = createEventHandler("testHandler");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(newEventHandler.getName())))
                .thenReturn(existingEventHandler.getName());

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = createEventHandler("existingHandler");
        EventHandler newEventHandler = createEventHandler("updatedHandler");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(newEventHandler.getName())))
                .thenReturn(existingEventHandler.getName());
        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq(newEventHandler.getName()), Mockito.anyString()))
                .thenReturn(1);
        when(jedisProxy.sadd(Mockito.anyString(), Mockito.eq(newEventHandler.getName())))
                .thenReturn(1);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        assertEquals(1, ReflectionTestUtils.getFieldValue(redisEventHandlerDAO, "size"));
        assertNotNull(redisEventHandlerDAO.getEventHandler(newEventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler newEventHandler = createEventHandler("updatedHandler");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(newEventHandler.getName())))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(newEventHandler.getName()));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = createEventHandler("testHandler");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(eventHandler.getName());
        when(jedisProxy.hdel(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(1);
        when(jedisProxy.srem(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        assertNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
        assertEquals(0, ReflectionTestUtils.getFieldValue(redisEventHandlerDAO, "size"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = "nonExistentHandler";

        redisEventHandlerDAO.removeEventHandler(name);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(name));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler handler1 = createEventHandler("handler1");
        EventHandler handler2 = createEventHandler("handler2");

        when(jedisProxy.hgetAll(Mockito.anyString()))
                .thenReturn(Map.of("handler1", toJson(handler1), "handler2", toJson(handler2)));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(handler1, eventHandlers.get(0));
        assertEquals(handler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler handler1 = createEventHandler("handler1");
        EventHandler handler2 = createEventHandler("handler2");

        String event = "testEvent";

        when(jedisProxy.smembers(Mockito.anyString()))
                .thenReturn(Set.of("handler1", "handler2"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(2, eventHandlers.size());
        assertEquals(handler1, eventHandlers.get(0));
        assertEquals(handler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        EventHandler activeHandler = createEventHandler("activeHandler");
        EventHandler inactiveHandler = createEventHandler("inactiveHandler");

        String event = "testEvent";

        when(jedisProxy.smembers(Mockito.anyString()))
                .thenReturn(Set.of("activeHandler", "inactiveHandler"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(activeHandler, eventHandlers.get(0));
    }

    private EventHandler createEventHandler(String name) {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        eventHandler.setEvent("testEvent");
        eventHandler.setIsActive(true);
        return eventHandler;
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return new ObjectMapper().writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}