import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;

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
import com.netflix.conductor.redis.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = Mockito.mock(Logger.class);
    private static final ObjectMapper OBJECT_MAPPER = mock(ObjectMapper.class);
    private static final JedisProxy JEDIS_PROXY = mock(JedisProxy.class);
    private static final ConductorProperties CONDUCTOR_PROPERTIES = mock(ConductorProperties.class);
    private static final RedisProperties REDIS_PROPERTIES = mock(RedisProperties.class);

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        Mockito.when(LOGGER.isDebugEnabled()).thenReturn(true);
        Mockito.when(LOGGER.isInfoEnabled()).thenReturn(true);
        Preconditions.checkState(CONDUCTOR_PROPERTIES != null, "CONDUCTOR_PROPERTIES must not be null");
        Preconditions.checkState(REDIS_PROPERTIES != null, "REDIS_PROPERTIES must not be null");

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                JEDIS_PROXY, OBJECT_MAPPER, CONDUCTOR_PROPERTIES, REDIS_PROPERTIES);
    }

    @Test
    public void testAddEventHandler_NewEventHandler_Success() {
        // Given
        EventHandler eventHandler = new EventHandler("test-name", "test-event");

        // When
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Then
        verify(JEDIS_PROXY).hset(anyString(), anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void testAddEventHandler_EventHandlerAlreadyExists_ConflictException() {
        // Given
        String existingEventHandlerName = "existing-name";
        EventHandler existingEventHandler = new EventHandler("existing-name", "test-event");
        EventHandler newEventHandler = new EventHandler("new-name", "test-event");

        // When
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        // Then
        verify(JEDIS_PROXY).hset(anyString(), anyString(), ArgumentMatchers.anyString());

        doThrow(new ConflictException("EventHandler with name %s already exists!", "new-name"))
                .when(redisEventHandlerDAO).addEventHandler(newEventHandler);

        // When & Then
        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(newEventHandler));
    }

    @Test
    public void testGetEventHandler_EventHandlerExists_Success() {
        // Given
        String eventHandlerName = "test-name";
        String eventHandlerJson = JsonUtil.writeValueAsString(new EventHandler("test-name", "test-event"));

        // When
        Mockito.when(JEDIS_PROXY.hget(anyString(), ArgumentMatchers.eq(eventHandlerName)))
                .thenReturn(eventHandlerJson);

        // When
        EventHandler eventHandler = redisEventHandlerDAO.getEventHandler(eventHandlerName);

        // Then
        assertEquals("test-name", eventHandler.getName());
        assertEquals("test-event", eventHandler.getEvent());
    }

    @Test
    public void testGetEventHandler_EventHandlerDoesNotExist_NotFoundException() {
        // Given
        String eventHandlerName = "non-existent-name";

        // When & Then
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandler(eventHandlerName));
    }

    @Test
    public void testUpdateEventHandler_EventHandlerExists_Success() {
        // Given
        String existingEventHandlerName = "existing-name";
        String existingEventHandlerJson = JsonUtil.writeValueAsString(new EventHandler("existing-name", "test-event"));
        EventHandler updatedEventHandler = new EventHandler("existing-name", "updated-event");

        // When
        Mockito.when(JEDIS_PROXY.hget(anyString(), ArgumentMatchers.eq(existingEventHandlerName)))
                .thenReturn(existingEventHandlerJson);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // Then
        verify(JEDIS_PROXY).hset(anyString(), anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void testUpdateEventHandler_EventHandlerDoesNotExist_NotFoundException() {
        // Given
        String eventHandlerName = "non-existent-name";
        EventHandler eventHandler = new EventHandler("non-existent-name", "test-event");

        // When & Then
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandler_EventHandlerExists_Success() {
        // Given
        String eventHandlerName = "test-name";

        // When
        redisEventHandlerDAO.removeEventHandler(eventHandlerName);

        // Then
        verify(JEDIS_PROXY).hdel(anyString(), ArgumentMatchers.eq(eventHandlerName));
    }

    @Test
    public void testRemoveEventHandler_EventHandlerDoesNotExist_NotFoundException() {
        // Given
        String eventHandlerName = "non-existent-name";

        // When & Then
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(eventHandlerName));
    }

    @Test
    public void testGetAllEventHandlers_EventHandlersExists_Success() {
        // Given
        String eventHandler1Json = JsonUtil.writeValueAsString(new EventHandler("eventHandler1", "test-event1"));
        String eventHandler2Json = JsonUtil.writeValueAsString(new EventHandler("eventHandler2", "test-event2"));

        Map<String, String> eventHandlers = Maps.newLinkedHashMap();
        eventHandlers.put("eventHandler1", eventHandler1Json);
        eventHandlers.put("eventHandler2", eventHandler2Json);

        // When
        Mockito.when(JEDIS_PROXY.hgetAll(anyString()))
                .thenReturn(eventHandlers);

        // When
        List<EventHandler> eventHandlersList = redisEventHandlerDAO.getAllEventHandlers();

        // Then
        assertEquals(2, eventHandlersList.size());
        assertEquals("eventHandler1", eventHandlersList.get(0).getName());
        assertEquals("test-event1", eventHandlersList.get(0).getEvent());
        assertEquals("eventHandler2", eventHandlersList.get(1).getName());
        assertEquals("test-event2", eventHandlersList.get(1).getEvent());
    }

    @Test
    public void testGetEventHandlersForEvent_EventHandlersExist_Success() {
        // Given
        String event = "test-event";
        String eventHandler1Json = JsonUtil.writeValueAsString(new EventHandler("eventHandler1", "test-event"));
        String eventHandler2Json = JsonUtil.writeValueAsString(new EventHandler("eventHandler2", "test-event"));

        Map<String, Set<String>> eventHandlersByNameEvent = Maps.newLinkedHashMap();
        eventHandlersByNameEvent.put("test-event", Lists.newLinkedHashSet("eventHandler1", "eventHandler2"));

        // When
        Mockito.when(JEDIS_PROXY.smembers(ArgumentMatchers.anyString()))
                .then(invocation -> eventHandlersByNameEvent.get(invocation.getArgument(0)));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        // Then
        assertEquals(2, eventHandlers.size());
        assertEquals("eventHandler1", eventHandlers.get(0).getName());
        assertEquals("test-event", eventHandlers.get(0).getEvent());
        assertEquals("eventHandler2", eventHandlers.get(1).getName());
        assertEquals("test-event", eventHandlers.get(1).getEvent());
    }

    @Test
    public void testGetEventHandlersForEvent_EventHandlersDoNotExist_EmptyList() {
        // Given
        String event = "test-event";

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        // Then
        assertNull(eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent_ActiveOnlyTrue_EventHandlersExistButInactive_EmptyList() {
        // Given
        String event = "test-event";
        String eventHandler1Json = JsonUtil.writeValueAsString(new EventHandler("eventHandler1", "test-event"));
        String eventHandler2Json = JsonUtil.writeValueAsString(new EventHandler("eventHandler2", "test-event"));

        Map<String, Set<String>> eventHandlersByNameEvent = Maps.newLinkedHashMap();
        eventHandlersByNameEvent.put("test-event", Lists.newLinkedHashSet("eventHandler1", "eventHandler2"));

        EventHandler eventHandler1 = new EventHandler("eventHandler1", "test-event");
        eventHandler1.setActive(false);

        // When
        Mockito.when(JEDIS_PROXY.smembers(ArgumentMatchers.anyString()))
                .then(invocation -> eventHandlersByNameEvent.get(invocation.getArgument(0)));

        // When
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        // Then
        assertNull(eventHandlers);
    }
}