import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
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
    public void setup() {
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

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(true);
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenAnswer(invocation -> JsonUtils.toJsonString(invocation.getArgument(0)));

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("testHandler"));
        assertEquals("testEvent", redisEventHandlerDAO.getEventHandler("testHandler").getEvent());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithDuplicateName() {
        EventHandler existingEventHandler = new EventHandler("testHandler", "testEvent");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler("testHandler", "newEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Conflict Exception"));

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("testHandler", "testEvent");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = new EventHandler("testHandler", "updatedEvent");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertEquals("updatedEvent", redisEventHandlerDAO.getEventHandler("testHandler").getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFoundName() {
        EventHandler updatedEventHandler = new EventHandler("testHandlerNotFound", "updatedEvent");

        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.srem(anyString(), anyString())).thenReturn(true);

        redisEventHandlerDAO.removeEventHandler("testHandler");

        assertNull(redisEventHandlerDAO.getEventHandler("testHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFoundName() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testHandler1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testHandler2", "testEvent2");

        Map<String, String> eventHandlers = Maps.newHashMap();
        eventHandlers.put("testHandler1", JsonUtils.toJsonString(eventHandler1));
        eventHandlers.put("testHandler2", JsonUtils.toJsonString(eventHandler2));

        when(jedisProxy.hgetAll(anyString())).thenReturn(eventHandlers);

        List<EventHandler> eventHandlersList = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlersList.size());
        assertEquals("testEvent1", eventHandlersList.get(0).getEvent());
        assertEquals("testEvent2", eventHandlersList.get(1).getEvent());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("testHandler1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testHandler2", "testEvent1");
        EventHandler eventHandler3 = new EventHandler("testHandler3", "testEvent2");

        Map<String, Set<String>> eventHandlersByEvent = Maps.newHashMap();
        Set<String> eventHandlerNames1 = Lists.newArrayList("testHandler1", "testHandler2").asList().iterator().next();
        Set<String> eventHandlerNames2 = Lists.newArrayList("testHandler3").asList().iterator().next();

        eventHandlersByEvent.put("testEvent1", Lists.newHashSet(eventHandlerNames1));
        eventHandlersByEvent.put("testEvent2", Lists.newHashSet(eventHandlerNames2));

        when(jedisProxy.smembers(anyString())).thenAnswer(invocation -> eventHandlersByEvent.get(invocation.getArgument(1)));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(2, eventHandlers.size());
        assertEquals("testEvent1", eventHandlers.get(0).getEvent());
        assertTrue(eventHandlers.get(1).isActive());

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", true);

        assertEquals(1, activeEventHandlers.size());
        assertEquals("testEvent1", activeEventHandlers.get(0).getEvent());
        assertTrue(activeEventHandlers.get(0).isActive());
    }

    @Test
    public void testGetEventHandlerForInvalidEvent() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlerForEvent("testEvent"));
    }
}