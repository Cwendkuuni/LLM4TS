import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.dao.BaseDynoDAO;
import com.netflix.conductor.redis.dao.JedisProxy;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxyMock;
import com.netflix.conductor.redis.util.JacksonUtil;
import com.netflix.conductor.common.properties.ConductorProperties;
import com.netflix.conductor.common.properties.RedisProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxyMock jedisProxyMock;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jedisProxyMock = Mockito.mock(JedisProxy.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        objectMapper = Mockito.mock(ObjectMapper.class);

        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxyMock);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = getTestEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxyMock).hset(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testAddEventHandlerWithConflict() {
        String name = "testEventHandler";
        EventHandler eventHandler = getTestEventHandler();
        EventHandler existingEventHandler = new EventHandler("TestEvent", "TestHandler");
        doReturn(existingEventHandler).when(redisEventHandlerDAO).GetterEventHandler(name);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        Mockito.verify(jedisProxyMock, Mockito.never()).hset(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testUpdateEventHandler() {
        String name = "testEventHandler";
        EventHandler eventHandler = getTestEventHandler();
        eventHandler.setName("updatedEventHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedisProxyMock).hset(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testUpdateEventHandlerWithNotFound() {
        String name = "testEventHandler";

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(new EventHandler()));

        Mockito.verify(jedisProxyMock, Mockito.never()).hset(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "testEventHandler";

        redisEventHandlerDAO.removeEventHandler(name);

        Mockito.verify(jedisProxyMock).hdel(any(String.class), any(String.class));
    }

    @Test
    public void testRemoveEventHandlerWithNotFound() {
        String name = "testEventHandler";

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler"));

        Mockito.verify(jedisProxyMock, Mockito.never()).hdel(any(String.class), any(String.class));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = getTestEventHandler();
        EventHandler eventHandler2 = getTestEventHandler();
        eventHandler2.setName("updatedEventHandler");

        doReturn(eventHandler1.getName()).when(redisEventHandlerDAO).GeneratorKey(EVENT_HANDLERS);
        doReturn(eventHandler2.getName()).when(redisEventHandlerDAO).GeneratorKey(EVENT_HANDLERS);
        doReturn(Maps.newHashMap()).when(jedisProxyMock).hgetAll(any(String.class));
        doReturn(Lists.newArrayList(eventHandler1, eventHandler2)).when(redisEventHandlerDAO).parseHandlersFromJson(any(String.class));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlerForEvent() {
        String event = "TestEvent";
        EventHandler eventHandler = getTestEventHandler();

        doReturn(eventHandler.getName()).when(redisEventHandlerDAO).GeneratorKey(EVENT_HANDLERS_BY_EVENT, event);
        doReturn(eventHandler).when(redisEventHandlerDAO).GetterEventHandler(eventHandler.getName());
        doReturn(Lists.newArrayList(eventHandler.getName())).when(jedisProxyMock).smembers(any(String.class));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler, eventHandlers.get(0));
    }

    @Test
    public void testGetEventHandlerForEventWithActiveOnlyTrue() {
        String event = "TestEvent";
        EventHandler eventHandler = getTestEventHandler();
        eventHandler.setActive(false);

        doReturn(eventHandler.getName()).when(redisEventHandlerDAO).GeneratorKey(EVENT_HANDLERS_BY_EVENT, event);
        doReturn(Lists.newArrayList(eventHandler.getName())).when(jedisProxyMock).smembers(any(String.class));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        assertNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlerForEventWithActiveOnlyFalseAndEmptySet() {
        String event = "TestEvent";

        doReturn(Lists.newArrayList()).when(jedisProxyMock).smembers(any(String.class));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlerForEventWithActiveOnlyFalseAndNonEmptySet() {
        String event = "TestEvent";
        EventHandler eventHandler = getTestEventHandler();

        doReturn(eventHandler.getName()).when(redisEventHandlerDAO).GeneratorKey(EVENT_HANDLERS_BY_EVENT, event);
        doReturn(Lists.newArrayList(eventHandler.getName())).when(jedisProxyMock).smembers(any(String.class));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler, eventHandlers.get(0));
    }

    private EventHandler getTestEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("TestEvent");
        eventHandler.setHandler("TestHandler");
        eventHandler.setActive(true);
        return eventHandler;
    }

    private String nsKey(String... parts) {
        return String.join(":", ReflectionTestUtils.getFieldValue(redisEventHandlerDAO, "namespace"), parts);
    }
}