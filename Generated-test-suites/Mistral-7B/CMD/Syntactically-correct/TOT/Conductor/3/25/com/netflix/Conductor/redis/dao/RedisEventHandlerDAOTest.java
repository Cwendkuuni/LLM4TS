import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.util.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@Configuration
@Import({ RedisProperties.class, ConductorProperties.class })
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        TestUtils.initMocks(jedisProxy, objectMapper, conductorProperties, redisProperties);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @After
    public void teardown() {
        TestUtils.teardownMocks(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
        assertNotNull(jedisProxy.hget(redisEventHandlerDAO.getNamespaceKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        EventHandler existingEventHandler = new EventHandler("existingHandler", eventName);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName, eventName));
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        String eventName1 = "event1";
        String eventName2 = "event2";
        String handlerName1 = "handler1";
        String handlerName2 = "handler2";

        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName1, eventName1));
        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName2, eventName1));
        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName1, eventName2));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(3, eventHandlers.size());
        assertEquals(handlerName1, eventHandlers.get(0).getName());
        assertEquals(eventName1, eventHandlers.get(0).getEvent());
        assertEquals(handlerName2, eventHandlers.get(1).getName());
        assertEquals(eventName1, eventHandlers.get(1).getEvent());
        assertEquals(handlerName1, eventHandlers.get(2).getName());
        assertEquals(eventName2, eventHandlers.get(2).getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNonExistentName() throws Exception {
        redisEventHandlerDAO.getEventHandler("nonExistentName");
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        String eventName = "testEvent";
        String oldHandlerName = "oldHandler";
        String newHandlerName = "newHandler";
        EventHandler existingEventHandler = new EventHandler(oldHandlerName, eventName);
        EventHandler newEventHandler = new EventHandler(newHandlerName, eventName);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        assertNull(redisEventHandlerDAO.getEventHandler(oldHandlerName));
        assertNotNull(redisEventHandlerDAO.getEventHandler(newHandlerName));
        assertNotNull(jedisProxy.hget(redisEventHandlerDAO.getNamespaceKey(EVENT_HANDLERS), newHandlerName));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() throws Exception {
        redisEventHandlerDAO.updateEventHandler(new EventHandler("nonExistentName", "testEvent"));
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String eventName = "testEvent";
        String handlerName = "testHandler";

        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName, eventName));
        redisEventHandlerDAO.removeEventHandler(handlerName);

        assertNull(redisEventHandlerDAO.getEventHandler(handlerName));
        assertNull(jedisProxy.hget(redisEventHandlerDAO.getNamespaceKey(EVENT_HANDLERS), handlerName));
        assertFalse(jedisProxy.sismember(redisEventHandlerDAO.getNamespaceKey(EVENT_HANDLERS_BY_EVENT), eventName));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() throws Exception {
        redisEventHandlerDAO.removeEventHandler("nonExistentName");
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        String eventName = "testEvent";
        String handlerName1 = "handler1";
        String handlerName2 = "handler2";

        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName1, eventName));
        redisEventHandlerDAO.addEventHandler(new EventHandler(handlerName2, eventName));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);
        assertEquals(2, eventHandlers.size());
        assertEquals(handlerName1, eventHandlers.get(0).getName());
        assertEquals(eventName, eventHandlers.get(0).getEvent());
        assertEquals(handlerName2, eventHandlers.get(1).getName());
        assertEquals(eventName, eventHandlers.get(1).getEvent());

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
        assertEquals(1, activeEventHandlers.size());
        assertEquals(handlerName1, activeEventHandlers.get(0).getName());
        assertEquals(eventName, activeEventHandlers.get(0).getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNonExistentEvent() throws Exception {
        redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false);
    }
}