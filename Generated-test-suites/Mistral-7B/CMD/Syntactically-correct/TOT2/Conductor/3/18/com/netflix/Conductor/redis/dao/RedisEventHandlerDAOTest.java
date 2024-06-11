import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private RedisProperties redisProperties;
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedisProxy.getJedis()).thenReturn(mock(javax.annotation.Resource.class));
        when(conductorProperties.getNameSpace()).thenReturn("testNamespace");
        when(redisProperties.getKeyPrefix()).thenReturn("testPrefix");
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisTemplate", redisTemplate);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent", true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), eq(eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler("testName", "testEvent", true);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler("testName", "testEvent2", true);

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(newEventHandler.getName()), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), eq(newEventHandler.getName()));
        verify(jedisProxy, times(1)).hset(anyString(), eq(existingEventHandler.getName()), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEventHandlerNullEventHandler() {
        redisEventHandlerDAO.addEventHandler(null);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler("testName", "testEvent", true);
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = new EventHandler("testName", "testEvent2", true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq(existingEventHandler.getName()), anyString());
        verify(jedisProxy, times(1)).hset(anyString(), eq(updatedEventHandler.getName()), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNonExistentEventHandler() {
        EventHandler updatedEventHandler = new EventHandler("testName2", "testEvent2", true);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testName", "testEvent", true);
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testName");

        verify(jedisProxy, times(1)).hdel(anyString(), eq("testName"));
        verify(jedisProxy, times(1)).srem(anyString(), eq("testName"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNonExistentEventHandler() {
        redisEventHandlerDAO.removeEventHandler("testName2");
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testName1", "testEvent1", true);
        EventHandler eventHandler2 = new EventHandler("testName2", "testEvent2", true);

        Map<String, String> all = Map.of("testName1", toJson(eventHandler1), "testName2", toJson(eventHandler2));
        when(jedisProxy.hgetAll(anyString())).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetAllEventHandlersEmpty() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(0, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler("testName1", "testEvent1", true);
        EventHandler eventHandler2 = new EventHandler("testName2", "testEvent2", true);

        Set<String> names = new HashSet<>(Arrays.asList("testName1", "testName2"));
        when(jedisProxy.smembers(anyString())).thenReturn(names.toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        Set<String> names = new HashSet<>();
        when(jedisProxy.smembers(anyString())).thenReturn(names.toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent1", false);

        assertEquals(0, eventHandlers.size());
    }
}