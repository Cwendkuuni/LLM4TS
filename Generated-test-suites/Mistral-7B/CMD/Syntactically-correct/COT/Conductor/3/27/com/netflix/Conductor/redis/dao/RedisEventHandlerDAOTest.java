import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

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
import com.netflix.conductor.redis.test.BaseDynoDAOTest;
import com.netflix.conductor.test.TestObjects;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;
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

    // AddEventHandler test case
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = TestObjects.createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Check if the EventHandler is added to Redis
        Assert.assertEquals(eventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = TestObjects.createEventHandler();
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = TestObjects.createEventHandler();

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    // UpdateEventHandler test case
    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = TestObjects.createEventHandler();
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = TestObjects.createEventHandler();
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // Check if the EventHandler is updated in Redis
        Assert.assertEquals(updatedEventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), updatedEventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler updatedEventHandler = TestObjects.createEventHandler();

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    // RemoveEventHandler test case
    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = TestObjects.createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        // Check if the EventHandler is removed from Redis
        Assert.assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("non_existent_name");
    }

    // getAllEventHandlers test case
    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> eventHandlers = Lists.newArrayList(TestObjects.createEventHandler(), TestObjects.createEventHandler());
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(createMockMap(eventHandlers));

        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();

        Assert.assertEquals(eventHandlers, result);
    }

    // getEventHandlersForEvent test case
    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlers = Lists.newArrayList(TestObjects.createEventHandler(), TestObjects.createEventHandler());
        Set<String> names = new LinkedHashSet<>(Lists.newArrayList("name1", "name2"));
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event"))).thenReturn(names);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event", false);

        Assert.assertEquals(eventHandlers, result);
    }

    // getEventHandlersForEvent with activeOnly true test case
    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        List<EventHandler> eventHandlers = Lists.newArrayList(TestObjects.createEventHandler(), TestObjects.createEventHandler());
        Set<String> names = new LinkedHashSet<>(Lists.newArrayList("name1"));
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event"))).thenReturn(names);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event", true);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(eventHandlers.get(0).isActive(), result.get(0).isActive());
    }

    // getEventHandlersForEvent with empty set test case
    @Test
    public void testGetEventHandlersForEventEmptySet() {
        Set<String> names = new LinkedHashSet<>();
        when(jedisProxy.smembers(nsKey(EVENT_HANDLERS_BY_EVENT, "event"))).thenReturn(names);

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("event", false);

        Assert.assertEquals(0, result.size());
    }

    // getEventHandler test case
    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = TestObjects.createEventHandler();
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName())).thenReturn(toJson(eventHandler));

        EventHandler result = redisEventHandlerDAO.getEventHandler(eventHandler.getName());

        Assert.assertEquals(eventHandler, result);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "non_existent_name")).thenReturn(null);

        redisEventHandlerDAO.getEventHandler("non_existent_name");
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerTransientException() {
        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "name")).thenThrow(new RuntimeException());

        redisEventHandlerDAO.getEventHandler("name");
    }

    private Map<String, String> createMockMap(List<EventHandler> eventHandlers) {
        Map<String, String> result = mock(Map.class);
        for (EventHandler eventHandler : eventHandlers) {
            when(result.put(eventHandler.getName(), toJson(eventHandler))).thenReturn(null);
            when(result.get(eventHandler.getName())).thenReturn(toJson(eventHandler));
        }
        return result;
    }

    private String nsKey(String... keys) {
        return String.join(":", BaseDynoDAOTest.NS_KEY, String.join(":", keys));
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}