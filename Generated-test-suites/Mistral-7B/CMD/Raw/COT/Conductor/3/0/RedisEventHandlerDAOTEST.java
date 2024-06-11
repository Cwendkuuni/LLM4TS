import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.util.TestJsonUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
    }

    @Test
    public void testAddEventHandler() throws ConflictException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setConfig(new Object());
        eventHandler.setActive(true);

        when(jedisProxy.hset(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler", TestJsonUtil.writeValueAsString(eventHandler))).thenReturn(true);
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testJsonValue");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Assert.assertFalse(jedisProxy.exists(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler"));
        Assert.assertTrue(jedisProxy.exists(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() throws ConflictException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setConfig(new Object());
        existingEventHandler.setActive(true);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEvent");
        newEventHandler.setConfig(new Object());
        newEventHandler.setActive(true);

        when(jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler")).thenReturn("existingJsonValue");
        when(objectMapper.readValue(TestJsonUtil.writeValueAsString(existingEventHandler), EventHandler.class)).thenReturn(existingEventHandler);

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws NotFoundException, ConflictException {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setConfig(new Object());
        existingEventHandler.setActive(true);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEventUpdated");
        newEventHandler.setConfig(new Object());
        newEventHandler.setActive(true);

        when(jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler")).thenReturn("existingJsonValue");
        when(objectMapper.readValue(TestJsonUtil.writeValueAsString(existingEventHandler), EventHandler.class)).thenReturn(existingEventHandler);
        when(jedisProxy.hset(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler", TestJsonUtil.writeValueAsString(newEventHandler))).thenReturn(true);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Assert.assertEquals(existingEventHandler.getEvent(), newEventHandler.getEvent());
        Assert.assertNotEquals(existingEventHandler.getEvent(), newEventHandler.getEvent());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFound() throws NotFoundException {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEventUpdated");
        newEventHandler.setConfig(new Object());
        newEventHandler.setActive(true);

        when(jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler")).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws NotFoundException {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setConfig(new Object());
        eventHandler.setActive(true);

        when(jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler")).thenReturn("testJsonValue");
        when(objectMapper.readValue(TestJsonUtil.writeValueAsString(eventHandler), EventHandler.class)).thenReturn(eventHandler);
        when(jedisProxy.hdel(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler")).thenReturn(1);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Assert.assertFalse(jedisProxy.exists(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFound() throws NotFoundException {
        when(jedisProxy.hget(RedisEventHandlerDAO.EVENT_HANDLERS, "testEventHandler")).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent1");
        eventHandler1.setConfig(new Object());
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent2");
        eventHandler2.setConfig(new Object());
        eventHandler2.setActive(true);

        Map<String, String> all = Lists.newLinkedHashMap();
        all.put("testEventHandler1", TestJsonUtil.writeValueAsString(eventHandler1));
        all.put("testEventHandler2", TestJsonUtil.writeValueAsString(eventHandler2));

        when(jedisProxy.hgetAll(RedisEventHandlerDAO.EVENT_HANDLERS)).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        Assert.assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() throws NotFoundException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setConfig(new Object());
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setConfig(new Object());
        eventHandler2.setActive(false);

        Map<String, String> eventHandlersByEvent = Lists.newLinkedHashMap();
        eventHandlersByEvent.put("testEventHandler1", TestJsonUtil.writeValueAsString(eventHandler1));
        eventHandlersByEvent.put("testEventHandler2", TestJsonUtil.writeValueAsString(eventHandler2));

        when(jedisProxy.smembers(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")).thenReturn(eventHandlersByEvent.keySet().toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        Assert.assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly() throws NotFoundException {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setConfig(new Object());
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setConfig(new Object());
        eventHandler2.setActive(false);

        Map<String, String> eventHandlersByEvent = Lists.newLinkedHashMap();
        eventHandlersByEvent.put("testEventHandler1", TestJsonUtil.writeValueAsString(eventHandler1));
        eventHandlersByEvent.put("testEventHandler2", TestJsonUtil.writeValueAsString(eventHandler2));

        when(jedisProxy.smembers(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent")).thenReturn(eventHandlersByEvent.keySet().toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        Assert.assertEquals(1, eventHandlers.size());
    }
}