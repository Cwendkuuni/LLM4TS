import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.dao.BaseDynoDAO;

public class RedisEventHandlerDAOTest {

    private static final String NAMESPACE = "test";
    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        jedisProxy = mock(JedisProxy.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), keyCaptor.capture(), valueCaptor.capture());
        assertEquals(eventHandler.getName(), keyCaptor.getValue());
        String json = valueCaptor.getValue();
        assertEquals(eventHandler.toString(), json);
    }

    @Test
    public void testAddEventHandlerWithConflict() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler existingEventHandler = createEventHandler("testEvent", "testName", true);
        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(existingEventHandler));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName", true);
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()))).thenReturn(toJson(eventHandler));

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test
    public void testGetEventHandlerNotFound() {
        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), anyString())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler("nonExistentName"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = createEventHandler("testEvent", "testName", true);
        EventHandler updatedEventHandler = createEventHandler("testEvent", "testName", false);

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(existingEventHandler.getName()))).thenReturn(toJson(existingEventHandler));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(2)).hset(eq(nsKey(EVENT_HANDLERS)), anyString(), anyString());
        verify(jedisProxy).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")), eq(existingEventHandler.getName()));
        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eq(existingEventHandler.getName()), toJson(updatedEventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = createEventHandler("testEvent", "testName", true);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        verify(jedisProxy).hdel(eq(nsKey(EVENT_HANDLERS)), eq(eventHandler.getName()));
        verify(jedisProxy).srem(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")), eq(eventHandler.getName()));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = createEventHandler("testEvent1", "testName1", true);
        EventHandler eventHandler2 = createEventHandler("testEvent1", "testName2", false);

        Map<String, String> handlers = Maps.newHashMap();
        handlers.put(eventHandler1.getName(), toJson(eventHandler1));
        handlers.put(eventHandler2.getName(), toJson(eventHandler2));

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(handlers);

        List<EventHandler> retrievedHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, retrievedHandlers.size());
        assertEquals(eventHandler1, retrievedHandlers.get(0));
        assertEquals(eventHandler2, retrievedHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = createEventHandler("testEvent", "testName1", true);
        EventHandler eventHandler2 = createEventHandler("testEvent", "testName2", false);

        Set<String> names = Sets.newHashSet("testName1", "testName2");

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(names.toArray(new String[0]));

        List<EventHandler> retrievedHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(2, retrievedHandlers.size());
        assertEquals(eventHandler1, retrievedHandlers.get(0));
        assertEquals(eventHandler2, retrievedHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, "testEvent")))).thenReturn(new String[0]);

        List<EventHandler> retrievedHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);
        assertEquals(0, retrievedHandlers.size());
    }

    private void verify(JedisProxy jedisProxy, int times, InvocationOnMock invocation) {
        verify(jedisProxy, times(times), invocation);
    }

    private void verify(JedisProxy jedisProxy, int times, Method method, Object... arguments) {
        verify(jedisProxy, times(times), method, arguments);
    }

    private void verify(JedisProxy jedisProxy, int times, String methodName, Object... arguments) {
        verify(jedisProxy, times(times), eq(methodName), arguments);
    }

    private void verify(JedisProxy jedisProxy, int times, Answer<String> answer, String methodName, Object... arguments) {
        verify(jedisProxy, times(times), answer, eq(methodName), arguments);
    }

    private void verify(JedisProxy jedisProxy, int times, Method method, Object arg1, Object arg2) {
        verify(jedisProxy, times(times), method, arg1, arg2);
    }

    private void verify(JedisProxy jedisProxy, int times, String methodName, Object arg1, Object arg2) {
        verify(jedisProxy, times(times), eq(methodName), arg1, arg2);
    }

    private String nsKey(String... parts) {
        return NAMESPACE + StringUtils.join(parts, ":");
    }

    private EventHandler createEventHandler(String event, String name, boolean active) {
        return new EventHandler() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getEvent() {
                return event;
            }

            @Override
            public boolean isActive() {
                return active;
            }
        };
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}