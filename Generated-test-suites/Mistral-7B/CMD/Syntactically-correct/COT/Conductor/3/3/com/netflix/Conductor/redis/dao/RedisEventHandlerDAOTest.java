import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisTestUtil;
import com.netflix.conductor.testutils.TestObjectMapper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAO.class);

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = new TestObjectMapper();
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        String json = toJson(eventHandler);

        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq(eventHandler.getName()), Mockito.eq(json)))
                .thenReturn(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Assert.assertNotNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflictException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // ConflictException should be thrown
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(toJson(eventHandler));

        EventHandler result = redisEventHandlerDAO.getEventHandler(eventHandler.getName());

        Assert.assertEquals(eventHandler, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNotFoundException() {
        when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("NotFoundException"));

        redisEventHandlerDAO.getEventHandler("testEventHandler");

        // NotFoundException should be thrown
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("updatedTestEvent");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(existingEventHandler.getName())))
                .thenReturn(toJson(existingEventHandler));

        when(jedisProxy.hset(Mockito.anyString(), Mockito.eq(existingEventHandler.getName()), Mockito.anyString()))
                .thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        EventHandler result = redisEventHandlerDAO.getEventHandler(existingEventHandler.getName());

        Assert.assertEquals(updatedEventHandler, result);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFoundException() {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("updatedTestEvent");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(updatedEventHandler.getName())))
                .thenThrow(new RuntimeException("NotFoundException"));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // NotFoundException should be thrown
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(eventHandler.getName())))
                .thenReturn(toJson(eventHandler));

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        Assert.assertNull(redisEventHandlerDAO.getEventHandler(eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFoundException() {
        when(jedisProxy.hget(Mockito.anyString(), Mockito.eq(Mockito.anyString())))
                .thenThrow(new RuntimeException("NotFoundException"));

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        // NotFoundException should be thrown
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = Maps.newHashMap();
        all.put("testEventHandler1", toJson(new EventHandler()));
        all.put("testEventHandler2", toJson(new EventHandler()));

        when(jedisProxy.hgetAll(Mockito.anyString()))
                .thenReturn(all);

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        List<EventHandler> expected = Lists.newArrayList(
                new EventHandler(),
                new EventHandler()
        );

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        Map<String, Set<String>> eventHandlersByEvent = Maps.newHashMap();
        Set<String> testEventHandler1Names = Lists.newLinkedHashSet(Arrays.asList("testEventHandler1", "testEventHandler2"));
        Set<String> testEventHandler2Names = Lists.newLinkedHashSet(Arrays.asList("testEventHandler2"));

        eventHandlersByEvent.put(event, testEventHandler1Names);
        eventHandlersByEvent.put(event, testEventHandler2Names);

        when(jedisProxy.smembers(Mockito.anyString()))
                .thenAnswer(invocation -> invocation.getArguments()[1]);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        List<EventHandler> expected = Lists.newArrayList(
                new EventHandler(),
                new EventHandler()
        );

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        String event = "testEvent";
        Map<String, Set<String>> eventHandlersByEvent = Maps.newHashMap();
        Set<String> testEventHandler1Names = Lists.newLinkedHashSet(Arrays.asList("testEventHandler1"));
        Set<String> testEventHandler2Names = Lists.newLinkedHashSet(Arrays.asList("testEventHandler2"));

        eventHandlersByEvent.put(event, testEventHandler1Names);

        when(jedisProxy.smembers(Mockito.anyString()))
                .thenAnswer(invocation -> invocation.getArguments()[1]);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        List<EventHandler> expected = Lists.newArrayList(
                new EventHandler()
        );

        Assert.assertEquals(expected, result);
    }

    private String toJson(Object object) {
        return RedisTestUtil.toJson(objectMapper, object);
    }
}