import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    private String nsKey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(redisProperties.getNamespace()).thenReturn("test");
        nsKey = "test:EVENT_HANDLERS";
        Mockito.when(conductorProperties.getMetadataStoreNamespace()).thenReturn("test");
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    // Test addEventHandler method
    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = getTestEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), eq(toJson(eventHandler)));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExistingHandler() throws Exception {
        EventHandler existingHandler = getTestEventHandler();
        redisEventHandlerDAO.addEventHandler(existingHandler);
        redisEventHandlerDAO.addEventHandler(getTestEventHandler());
    }

    // Test updateEventHandler method
    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = getTestEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler updatedEventHandler = getTestEventHandlerWithUpdatedName("updatedName");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisProxy, times(1)).hset(nsKey(EVENT_HANDLERS), updatedEventHandler.getName(), eq(toJson(updatedEventHandler)));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNonExistingHandler() throws Exception {
        EventHandler updatedEventHandler = getTestEventHandlerWithUpdatedName("updatedName");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    // Test removeEventHandler method
    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = getTestEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        verify(jedisProxy, times(1)).hdel(nsKey(EVENT_HANDLERS), eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNonExistingHandler() throws Exception {
        redisEventHandlerDAO.removeEventHandler("nonExistingHandler");
    }

    // Test getAllEventHandlers method
    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler handler1 = getTestEventHandler();
        EventHandler handler2 = getTestEventHandler();
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);

        List<EventHandler> allHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, allHandlers.size());
        verify(jedisProxy, times(1)).hgetAll(nsKey(EVENT_HANDLERS));
    }

    // Test getEventHandlersForEvent method with empty set
    @Test
    public void testGetEventHandlersForEventEmptySet() throws Exception {
        String event = "testEvent";
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(0, handlers.size());
    }

    // Test getEventHandlersForEvent method with non-empty set
    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        String event = "testEvent";
        EventHandler handler1 = getTestEventHandler();
        EventHandler handler2 = getTestEventHandlerWithUpdatedName("updatedName");
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(2, handlers.size());
        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
    }

    // Test getEventHandlersForEvent method with activeOnly = true
    @Test
    public void testGetEventHandlersForEventActiveOnly() throws Exception {
        String event = "testEvent";
        EventHandler handler1 = getTestEventHandler();
        EventHandler handler2 = getTestEventHandlerWithActive(false);
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(1, handlers.size());
        verify(jedisProxy, times(1)).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
    }

    private EventHandler getTestEventHandler() {
        return new EventHandler() {{
            setName("testHandler");
            setEvent("testEvent");
            setActive(true);
        }};
    }

    private EventHandler getTestEventHandlerWithUpdatedName(String name) {
        return new EventHandler() {{
            setName(name);
            setEvent("testEvent");
            setActive(true);
        }};
    }

    private EventHandler getTestEventHandlerWithActive(boolean active) {
        return new EventHandler() {{
            setName("testHandler");
            setEvent("testEvent");
            setActive(active);
        }};
    }

    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String nsKey(String... parts) {
        return String.join(":", Lists.asList(nsKey, String.join(":", Lists.newArrayList(parts))));
    }

}