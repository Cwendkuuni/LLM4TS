import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
import com.netflix.conductor.redis.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = Mockito.spy(new EventHandler("testName", "testEvent"));
        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("testJson");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Map<String, String> handlersMap = jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
        Map<String, String> expectedHandlersMap = new LinkedHashMap<>();
        expectedHandlersMap.put("testName", "testJson");
        Assert.assertEquals(expectedHandlersMap, handlersMap);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithNameExists() {
        EventHandler eventHandler = Mockito.spy(new EventHandler("testName", "testEvent"));
        when(redisEventHandlerDAO.getEventHandler("testName")).thenReturn(new EventHandler("testName", "testEvent"));

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        String json = JsonUtils.toJson(eventHandler);

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testName")).thenReturn(json);
        when(objectMapper.readValue(Mockito.anyString(), EventHandler.class)).thenReturn(eventHandler);

        EventHandler result = redisEventHandlerDAO.getEventHandler("testName");
        Assert.assertEquals(eventHandler, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNameNotFound() {
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testName")).thenReturn(null);

        redisEventHandlerDAO.getEventHandler("testName");
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("testName", "testEvent");
        EventHandler newEventHandler = new EventHandler("testName", "testEvent2");
        String json = JsonUtils.toJson(existingEventHandler);

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testName")).thenReturn(json);
        when(objectMapper.readValue(Mockito.anyString(), EventHandler.class)).thenReturn(existingEventHandler);
        when(objectMapper.writeValueAsString(newEventHandler)).thenReturn("testJson2");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Map<String, String> handlersMap = jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
        Map<String, String> expectedHandlersMap = new LinkedHashMap<>();
        expectedHandlersMap.put("testName", "testJson2");
        Assert.assertEquals(expectedHandlersMap, handlersMap);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNameNotFound() {
        EventHandler newEventHandler = new EventHandler("testName", "testEvent2");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent");
        String json = JsonUtils.toJson(eventHandler);

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testName")).thenReturn(json);
        when(objectMapper.readValue(Mockito.anyString(), EventHandler.class)).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testName");

        Map<String, String> handlersMap = jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
        Assert.assertNull(handlersMap.get("testName"));
    }

    @Test(expected = NotFoundException.class)
    public
    void testRemoveEventHandlerWithNameNotFound() {
        redisEventHandlerDAO.removeEventHandler("testName");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testName1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testName2", "testEvent2");

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(new HashMap<String, String>() {{
            put("testName1", JsonUtils.toJson(eventHandler1));
            put("testName2", JsonUtils.toJson(eventHandler2));
        }});

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        List<EventHandler> expectedHandlers = Lists.newArrayList(eventHandler1, eventHandler2);
        Assert.assertEquals(expectedHandlers, result);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("testName1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testName2", "testEvent1");
        EventHandler eventHandler3 = new EventHandler("testName3", "testEvent2");

        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent1"))).thenReturn(Sets.newLinkedHashSet("testName1", "testName2"));
        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent2"))).thenReturn(Sets.newLinkedHashSet("testName3"));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);
        List<EventHandler> expectedHandlers = Lists.newArrayList(eventHandler1, eventHandler2);
        Assert.assertEquals(expectedHandlers, result);

        result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent2", true);
        List<EventHandler> expectedActiveHandlers = Lists.newArrayList(eventHandler3);
        Assert.assertEquals(expectedActiveHandlers, result);
    }

    @Test
    public void testGetEventHandlersForEventWithEmptySets() {
        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Sets.newLinkedHashSet());

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testAddEventHandlerWithDuplicateEvent() {
        EventHandler eventHandler = Mockito.spy(new EventHandler("testName", "testEvent"));

        when(redisEventHandlerDAO.getEventHandler("testName")).thenReturn(eventHandler);
        doThrow(new ConflictException("Duplicate event handler name")).when(redisEventHandlerDAO).addEventHandler(eventHandler);

        try {
            redisEventHandlerDAO.addEventHandler(eventHandler);
            Assert.fail();
        } catch (ConflictException e) {
            // expected
        }
    }

    @Test
    public void testUpdateEventHandlerWithNullEventHandler() {
        doThrow(new IllegalArgumentException("EventHandler cannot be null")).when(redisEventHandlerDAO).updateEventHandler(null);

        try {
            redisEventHandlerDAO.updateEventHandler(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testRemoveEventHandlerWithNullName() {
        doThrow(new IllegalArgumentException("Name cannot be null")).when(redisEventHandlerDAO).removeEventHandler(null);

        try {
            redisEventHandlerDAO.removeEventHandler(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetEventHandlerWithNullName() {
        doThrow(new IllegalArgumentException("Name cannot be null")).when(redisEventHandlerDAO).getEventHandler(null);

        try {
            redisEventHandlerDAO.getEventHandler(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetAllEventHandlersWithEmptyMap() {
        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(new HashMap<>());

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testGetEventHandlersForEventWithEmptySet() {
        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Sets.newLinkedHashSet());

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        Assert.assertEquals(0, result.size());
    }
}