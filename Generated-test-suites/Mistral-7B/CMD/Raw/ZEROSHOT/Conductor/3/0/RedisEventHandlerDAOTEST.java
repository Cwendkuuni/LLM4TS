import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.junit.JedisTestRule;
import com.netflix.conductor.redis.util.JsonUtil;
import com.netflix.conductor.testing.TestObjects;
import com.netflix.conductor.util.properties.Configurable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
    private static final String NAMESPACE = "test:namespace:";

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        // given
        EventHandler eventHandler = TestObjects.generateEventHandler();

        // when
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // then
        then(jedisProxy).hset(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), Mockito.eq(eventHandler.getName()),
                Mockito.eq(toJson(eventHandler)));
        verify(objectMapper).writeValueAsString(eventHandler);
        verify(jedisProxy).sadd(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT"),
                Mockito.eq(eventHandler.getEvent()), Mockito.eq(eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() {
        // given
        String name = "testName";
        EventHandler eventHandler = TestObjects.generateEventHandler();
        given(jedisProxy.hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name)).willReturn(toJson(TestObjects.generateEventHandler()));

        // when, then
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testGetEventHandlerByName() {
        // given
        String name = "testName";
        EventHandler eventHandler = TestObjects.generateEventHandler();
        given(jedisProxy.hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name)).willReturn(toJson(eventHandler));

        // when
        EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler(name);

        // then
        assertEquals(eventHandler, actualEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerByNameNotFound() {
        // given
        String name = "testName";

        // when
        redisEventHandlerDAO.getEventHandler(name);

        // then
        verify(jedisProxy).hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name);
    }

    @Test
    public void testUpdateEventHandler() {
        // given
        String name = "testName";
        EventHandler existingEventHandler = TestObjects.generateEventHandler();
        EventHandler updatedEventHandler = TestObjects.generateEventHandlerWithName(name);

        // when
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // then
        then(jedisProxy).hset(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), Mockito.eq(name),
                Mockito.eq(toJson(updatedEventHandler)));
        verify(objectMapper).writeValueAsString(updatedEventHandler);
        verify(jedisProxy).srem(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT"), Mockito.eq(existingEventHandler.getEvent()),
                Mockito.eq(existingEventHandler.getName()));
        verify(jedisProxy).sadd(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT"), Mockito.eq(updatedEventHandler.getEvent()),
                Mockito.eq(updatedEventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        // given
        String name = "testName";
        EventHandler updatedEventHandler = TestObjects.generateEventHandlerWithName(name);

        // when
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // then
        verify(jedisProxy).hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name);
    }

    @Test
    public void testRemoveEventHandler() {
        // given
        String name = "testName";
        EventHandler eventHandler = TestObjects.generateEventHandler();

        // when
        redisEventHandlerDAO.removeEventHandler(name);

        // then
        then(jedisProxy).hdel(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name);
        verify(jedisProxy).srem(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT"), Mockito.eq(eventHandler.getEvent()),
                Mockito.eq(eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        // given
        String name = "testName";

        // when
        redisEventHandlerDAO.removeEventHandler(name);

        // then
        verify(jedisProxy).hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name);
    }

    @Test
    public void testGetAllEventHandlers() {
        // given
        Map<String, String> handlersJson = Maps.newHashMap();
        handlersJson.put("handler1", toJson(TestObjects.generateEventHandler()));
        handlersJson.put("handler2", toJson(TestObjects.generateEventHandler()));
        given(jedisProxy.hgetAll(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"))).willReturn(handlersJson);

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // then
        assertEquals(2, eventHandlers.size());
        for (EventHandler eventHandler : eventHandlers) {
            assertTrue(eventHandler.getName().startsWith("handler"));
        }
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // given
        String event = "testEvent";
        Set<String> names = Sets.newHashSet("handler1", "handler2");

        given(jedisProxy.smembers(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT"), Mockito.eq(event)))
                .willReturn(names.toArray(new String[0]));

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        // then
        assertEquals(2, eventHandlers.size());
        for (EventHandler eventHandler : eventHandlers) {
            assertTrue(StringUtils.isNotBlank(eventHandler.getEvent()));
            assertTrue(StringUtils.isNotBlank(eventHandler.getName()));
        }
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        // given
        String event = "testEvent";
        Set<String> names = Sets.newHashSet("handler1", "handler2");

        given(jedisProxy.smembers(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT"), Mockito.eq(event)))
                .willReturn(names.toArray(new String[0]));

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        // then
        assertEquals(1, eventHandlers.size());
        for (EventHandler eventHandler : eventHandlers) {
            assertTrue(StringUtils.isNotBlank(eventHandler.getEvent()));
            assertTrue(StringUtils.isNotBlank(eventHandler.getName()));
            assertTrue(eventHandler.isActive());
        }
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        // given
        String event = "testEvent";

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        // then
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventEmptyActiveOnly() {
        // given
        String event = "testEvent";

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        // then
        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlerByEventAndName() {
        // given
        String event = "testEvent";
        String name = "testName";
        EventHandler eventHandler = TestObjects.generateEventHandler();

        given(jedisProxy.hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name)).willReturn(toJson(eventHandler));

        // when
        EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler(event, name);

        // then
        assertEquals(eventHandler, actualEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerByEventAndNameNotFound() {
        // given
        String event = "testEvent";
        String name = "testName";

        // when
        redisEventHandlerDAO.getEventHandler(event, name);

        // then
        verify(jedisProxy).hget(Mockito.eq(NAMESPACE + "EVENT_HANDLERS"), name);
    }

    private static String toJson(Object obj) {
        return new String(JsonUtil.writeValueAsBytes(obj));
    }

    @Test
    public void testInit() {
        // given
        Configurable conductorProperties = Mockito.mock(Configurable.class);
        Configurable redisProperties = Mockito.mock(Configurable.class);

        // when
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

        // then
        verify(conductorProperties).getConfig();
        verify(redisProperties).getConfig();
    }

    @Test
    public void testRecordRedisDaoRequests() {
        // given
        String requestName = "testRequest";

        // when
        redisEventHandlerDAO.recordRedisDaoRequests(requestName);

        // then
        verify(jedisProxy).incr(Mockito.eq(NAMESPACE + requestName + ":requests"));
    }

    @Test
    public void testRecordRedisDaoEventRequests() {
        // given
        String requestName = "testRequest";
        String eventName = "testEvent";

        // when
        redisEventHandlerDAO.recordRedisDaoEventRequests(requestName, eventName);

        // then
        verify(jedisProxy).incr(Mockito.eq(NAMESPACE + requestName + ":requests"));
        verify(jedisProxy).incr(Mockito.eq(NAMESPACE + "EVENT_HANDLERS_BY_EVENT:" + eventName + ":requests"));
    }
}