import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JsonUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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

    @Value("${namespaces.default}")
    private String defaultNamespace;

    private static final String EVENT_NAME = "testEvent";
    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String EVENT_HANDLER_JSON = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedisProxy.hget(anyString(), anyString())).thenAnswer(i -> i.callRealMethod(jedisProxy, i.getArguments()[0], i.getArguments()[1]));
        when(jedisProxy.hgetAll(anyString())).thenReturn(Maps.newHashMap());
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenAnswer(i -> i.callRealMethod(jedisProxy, i.getArguments()[0], i.getArguments()[1], i.getArguments()[2]));
        when(jedisProxy.sadd(anyString(), anyString())).thenAnswer(i -> i.callRealMethod(jedisProxy, i.getArguments()[0], i.getArguments()[1]));
        when(jedisProxy.srem(anyString(), anyString())).thenAnswer(i -> i.callRealMethod(jedisProxy, i.getArguments()[0], i.getArguments()[1]));
        when(jedisProxy.smembers(anyString())).thenReturn(Sets.newLinkedHashSet());
        when(objectMapper.readValue(anyString(), EventHandler.class)).thenAnswer(i -> readValue(i.callRealMethod(objectMapper, i.getArguments()[0]), EventHandler.class));
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenAnswer(i -> EVENT_HANDLER_JSON);
        when(conductorProperties.getNamespaces()).thenReturn(Maps.newHashMap());
        when(redisProperties.getNamespace()).thenReturn(defaultNamespace);
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerExistingEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT_NAME);
        existingEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName(EVENT_HANDLER_NAME);
        newEventHandler.setEvent(EVENT_NAME);
        newEventHandler.setActive(true);

        try {
            redisEventHandlerDAO.addEventHandler(newEventHandler);
        } catch (Exception e) {
            assertTrue(e instanceof ConflictException);
            verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
        }
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(EVENT_HANDLER_NAME);
        existingEventHandler.setEvent(EVENT_NAME);
        existingEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(EVENT_HANDLER_NAME);
        updatedEventHandler.setEvent(EVENT_NAME + "_updated");
        updatedEventHandler.setActive(false);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        verify(jedisProxy, times(2)).hset(anyString(), anyString(), anyString());
        verify(jedisProxy, times(1)).srem(anyString(), eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNonExistentEventHandler() {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("nonExistentEventHandler");
        updatedEventHandler.setEvent(EVENT_NAME);
        updatedEventHandler.setActive(false);

        try {
            redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        } catch (Exception e) {
            assertTrue(e instanceof NotFoundException);
            verify(jedisProxy, never()).hset(anyString(), anyString(), anyString());
        }
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(EVENT_HANDLER_NAME);
        eventHandler.setEvent(EVENT_NAME);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(EVENT_HANDLER_NAME);
        verify(jedisProxy, times(2)).hdel(anyString(), eq(EVENT_HANDLER_NAME));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNonExistentEventHandler() {
        redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");

        verify(jedisProxy, never()).hdel(anyString(), eq("nonExistentEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(true);

        jedisProxy.hgetAll(nsKey(EVENT_HANDLERS));
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Maps.newHashMap());
        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(Maps.newHashMap());
        when(objectMapper.readValue(anyString(), EventHandler[].class)).thenReturn(new EventHandler[0]);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(0, eventHandlers.size());

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(EVENT_NAME);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("otherEvent");
        eventHandler2.setActive(true);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("eventHandler3");
        eventHandler3.setEvent(EVENT_NAME);
        eventHandler3.setActive(false);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler3, eventHandlers.get(1));

        eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, false);
        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_NAME, true);
        assertEquals(0, eventHandlers.size());
    }
}