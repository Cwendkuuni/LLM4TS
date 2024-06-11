import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.ImmutableMap;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.JsonUtil;
import com.netflix.conductor.redis.util.TestRedisUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    public void initMocks() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
        MockitoAnnotations.initMocks(this);

        TestRedisUtil.setUpTestRedis();
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testJson");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), eq(eventHandler.getName()), eq("testJson"));
        verify(jedisProxy).sadd(anyString(), eq(eventHandler.getName()));
        verify(conductorProperties).getEventHandlersNamespace();
        verify(redisProperties).getEventHandlersKey();
        verify(redisProperties).getEventHandlersByEventKey();
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existingName");
        existingEventHandler.setEvent("existingEvent");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("existingName");
        newEventHandler.setEvent("existingEvent");

        when(jedisProxy.hget(anyString(), eq(existingEventHandler.getName()))).thenReturn("existingJson");

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existingName");
        existingEventHandler.setEvent("existingEvent");
        existingEventHandler.setActive(true);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("existingName");
        updatedEventHandler.setEvent("updatedEvent");
        updatedEventHandler.setActive(false);

        when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn("updatedJson");

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy).hset(anyString(), eq(existingEventHandler.getName()), eq("updatedJson"));
        verify(jedisProxy).srem(anyString(), eq(existingEventHandler.getName()));
        verify(jedisProxy).sadd(anyString(), eq(updatedEventHandler.getName()));
        verify(conductorProperties).getEventHandlersNamespace();
        verify(redisProperties).getEventHandlersKey();
        verify(redisProperties).getEventHandlersByEventKey();
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String nonExistentName = "nonExistentName";

        redisEventHandlerDAO.removeEventHandler(nonExistentName);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(jedisProxy.hget(anyString(), eq(eventHandler.getName()))).thenReturn("testJson");

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy).hdel(anyString(), eq(eventHandler.getName()));
        verify(jedisProxy).srem(anyString(), eq(eventHandler.getName()));
        verify(conductorProperties).getEventHandlersNamespace();
        verify(redisProperties).getEventHandlersKey();
        verify(redisProperties).getEventHandlersByEventKey();
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("handler1");
        handler1.setEvent("event1");
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("handler2");
        handler2.setEvent("event2");
        handler2.setActive(false);

        ImmutableMap<String, String> expectedHandlers = ImmutableMap.of(
                "handler1", JsonUtil.writeValueAsBytes(handler1).toString(),
                "handler2", JsonUtil.writeValueAsBytes(handler2).toString()
        );

        when(jedisProxy.hgetAll(anyString())).thenReturn(expectedHandlers);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, handlers.size());
        assertEquals(handler1, handlers.get(0));
        assertEquals(handler2, handlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("handler1");
        handler1.setEvent("event1");
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("handler2");
        handler2.setEvent("event2");
        handler2.setActive(false);

        EventHandler handler3 = new EventHandler();
        handler3.setName("handler3");
        handler3.setEvent("event1");
        handler3.setActive(true);

        Set<String> expectedNames = ImmutableSet.of("handler1", "handler3");

        when(jedisProxy.smembers(anyString())).thenReturn(expectedNames);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        assertEquals(2, handlers.size());
        assertEquals(handler1, handlers.get(0));
        assertEquals(handler3, handlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("handler1");
        handler1.setEvent("event1");
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("handler2");
        handler2.setEvent("event2");
        handler2.setActive(false);

        EventHandler handler3 = new EventHandler();
        handler3.setName("handler3");
        handler3.setEvent("event1");
        handler3.setActive(true);

        Set<String> expectedNames = ImmutableSet.of("handler1", "handler3");

        when(jedisProxy.smembers(anyString())).thenReturn(expectedNames);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(2, handlers.size());
        assertEquals(handler1, handlers.get(0));
        assertEquals(handler3, handlers.get(1));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(jedisProxy.hget(anyString(), eq(eventHandler.getName()))).thenReturn("testJson");

        EventHandler resultEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());

        assertEquals(eventHandler, resultEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        String nonExistentName = "nonExistentName";

        redisEventHandlerDAO.getEventHandler(nonExistentName);
    }

    @Test
    public void testGetEventHandlerWithActiveFalse() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(false);

        String json = "testJson";

        when(jedisProxy.hget(anyString(), eq(eventHandler.getName()))).thenReturn(json);

        EventHandler resultEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());

        assertEquals(eventHandler, resultEventHandler);
        assertFalse(resultEventHandler.isActive());
    }
}