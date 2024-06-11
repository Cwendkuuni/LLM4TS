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
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.JsonUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        jedisProxy = MockitoAnnotations.mock(JedisProxy.class);
        objectMapper = MockitoAnnotations.mock(ObjectMapper.class);
        conductorProperties = MockitoAnnotations.mock(ConductorProperties.class);
        redisProperties = MockitoAnnotations.mock(RedisProperties.class);

        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        String json = JsonUtils.writeValueAsString(eventHandler);

        ReflectionTestUtils.setFieldValue(jedisProxy, "hset", null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertTrue(jedisProxy.hset.wasCalledeOnce());
        assertEquals(json, jedisProxy.hset.getArgument(0)[1]);
        assertEquals(EVENT_HANDLERS, jedisProxy.hset.getArgument(1)[0]);
        assertEquals(eventHandler.getName(), jedisProxy.hset.getArgument(1)[1]);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflictingName() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler");
        eventHandler1.setEvent("TestEvent");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler");
        eventHandler2.setEvent("TestEvent2");

        String json1 = JsonUtils.writeValueAsString(eventHandler1);
        String json2 = JsonUtils.writeValueAsString(eventHandler2);

        ReflectionTestUtils.setFieldValue(jedisProxy, "hset", null);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        ReflectionTestUtils.setFieldValue(jedisProxy, "hset", null);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        eventHandler1.setEvent("TestEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent2");

        Map<String, String> all = Maps.newHashMap();
        all.put(eventHandler1.getName(), JsonUtils.writeValueAsString(eventHandler1));
        all.put(eventHandler2.getName(), JsonUtils.writeValueAsString(eventHandler2));

        ReflectionTestUtils.setFieldValue(jedisProxy, "hgetAll", all);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlerForEvent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");
        eventHandler.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent");
        eventHandler2.setActive(false);

        Set<String> names = Lists.newLinkedHashSet();
        names.add(eventHandler.getName());
        names.add(eventHandler2.getName());

        ReflectionTestUtils.setFieldValue(jedisProxy, "smembers", names);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", false);
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler));
        assertFalse(eventHandlers.contains(eventHandler2));

        ReflectionTestUtils.setFieldValue(jedisProxy, "smembers", Lists.newLinkedHashSet(eventHandler.getName()));
        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true);
        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        String json = JsonUtils.writeValueAsString(eventHandler);

        ReflectionTestUtils.setFieldValue(jedisProxy, "hget", null);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        ReflectionTestUtils.setFieldValue(jedisProxy, "hget", JsonUtils.writeValueAsString(eventHandler));
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        assertTrue(jedisProxy.hget.wasCalledeOnce());
        assertNull(jedisProxy.hget.getArgument(0)[1]);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFoundName() {
        ReflectionTestUtils.setFieldValue(jedisProxy, "hget", null);
        redisEventHandlerDAO.removeEventHandler("TestEventHandler");
    }
}