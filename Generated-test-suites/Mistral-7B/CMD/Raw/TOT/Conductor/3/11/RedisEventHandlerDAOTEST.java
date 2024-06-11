package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupyter.annotations.DisplayName;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class RedisEventHandlerDAOTest implements EventHandlerDAO {

    @MockBean
    private JedisProxy jedisProxy;

    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private RedisProperties properties;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private EventHandler eventHandler;
    private String eventName = "testEvent";

    @Before
    public void setup() {
        eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent(eventName);
        eventHandler.setActive(true);
    }

    @Test
    @DisplayName("Test adding a new EventHandler")
    public void testAddEventHandler() {
        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(eventHandler.getName()), Mockito.anyString());
        Mockito.verify(redisTemplate).opsForSet().add(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)), Mockito.eq(eventHandler.getName()));
    }

    @Test
    @DisplayName("Test adding an EventHandler with a conflicting name")
    public void testAddEventHandlerWithConflictingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testHandler");
        existingEventHandler.setEvent(eventName);
        existingEventHandler.setActive(true);

        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));

        Mockito.verify(jedisProxy, Mockito.never()).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(redisTemplate, Mockito.never()).opsForSet().add(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    @DisplayName("Test getting all EventHandlers")
    public void testGetAllEventHandlers() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("handler1");
        handler1.setEvent("testEvent");
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("handler2");
        handler2.setEvent("testEvent");
        handler2.setActive(true);

        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));
        Mockito.when(redisTemplate.opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)))).thenReturn(Arrays.asList(
                toJson(handler1), toJson(handler2)
        ));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        Mockito.verify(redisTemplate).opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)));

        assertEquals(2, eventHandlers.size());
        assertEquals(handler1, eventHandlers.get(0));
        assertEquals(handler2, eventHandlers.get(1));
    }

    @Test
    @DisplayName("Test updating an EventHandler")
    public void testUpdateEventHandler() {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testHandler");
        updatedEventHandler.setEvent("updatedEvent");
        updatedEventHandler.setActive(true);

        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(updatedEventHandler.getName()), Mockito.anyString());
        Mockito.verify(redisTemplate).opsForSet().remove(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)), Mockito.eq(eventHandler.getName()));
        Mockito.verify(redisTemplate).opsForSet().add(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, "updatedEvent")), Mockito.eq(updatedEventHandler.getName()));
    }

    @Test
    @DisplayName("Test updating an EventHandler with a non-existent name")
    public void testUpdateEventHandlerWithNonExistentName() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));

        Mockito.verify(jedisProxy, Mockito.never()).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verify(redisTemplate, Mockito.never()).opsForSet().remove(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(redisTemplate, Mockito.never()).opsForSet().add(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    @DisplayName("Test removing an EventHandler")
    public void testRemoveEventHandler() {
        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        Mockito.verify(jedisProxy).hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(eventHandler.getName()));
        Mockito.verify(redisTemplate).opsForSet().remove(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)), Mockito.eq(eventHandler.getName()));
    }

    @Test
    @DisplayName("Test removing an EventHandler with a non-existent name")
    public void testRemoveEventHandlerWithNonExistentName() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler(eventHandler.getName()));

        Mockito.verify(jedisProxy, Mockito.never()).hdel(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(redisTemplate, Mockito.never()).opsForSet().remove(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    @DisplayName("Test getting EventHandlers for a specific event")
    public void testGetEventHandlersForEvent() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("handler1");
        handler1.setEvent(eventName);
        handler1.setActive(true);

        EventHandler handler2 = new EventHandler();
        handler2.setName("handler2");
        handler2.setEvent("otherEvent");
        handler2.setActive(true);

        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));
        Mockito.when(redisTemplate.opsForSet()).thenReturn(Mockito.mock(SetOperations.SetOperations.class));

        Mockito.when(redisTemplate.opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)))).thenReturn(Arrays.asList(
                toJson(handler1), toJson(handler2)
        ));
        Mockito.when(redisTemplate.opsForSet().members(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)))).thenReturn(Collections.singletonList(handler1.getName()));

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);

        Mockito.verify(redisTemplate).opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)));
        Mockito.verify(redisTemplate).opsForSet().members(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)));

        assertEquals(1, handlers.size());
        assertEquals(handler1, handlers.get(0));
    }

    @Test
    @DisplayName("Test getting EventHandlers for a specific event with active only = true")
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        EventHandler handler1 = new EventHandler();
        handler1.setName("handler1");
        handler1.setEvent(eventName);
        handler1.setActive(false);

        EventHandler handler2 = new EventHandler();
        handler2.setName("handler2");
        handler2.setEvent(eventName);
        handler2.setActive(true);

        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));
        Mockito.when(redisTemplate.opsForSet()).thenReturn(Mockito.mock(SetOperations.SetOperations.class));

        Mockito.when(redisTemplate.opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)))).thenReturn(Arrays.asList(
                toJson(handler1), toJson(handler2)
        ));
        Mockito.when(redisTemplate.opsForSet().members(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)))).thenReturn(Arrays.asList(handler2.getName()));

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

        Mockito.verify(redisTemplate).opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)));
        Mockito.verify(redisTemplate).opsForSet().members(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)));

        assertEquals(1, handlers.size());
        assertEquals(handler2, handlers.get(0));
    }

    @Test
    @DisplayName("Test getting EventHandlers for a non-existent event")
    public void testGetEventHandlersForNonExistentEvent() {
        Mockito.when(redisTemplate.opsForHash()).thenReturn(Mockito.mock(RedisHashOperations.HashOperations.class));
        Mockito.when(redisTemplate.opsForSet()).thenReturn(Mockito.mock(SetOperations.SetOperations.class));

        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent("nonexistentEvent", false));

        Mockito.verify(redisTemplate).opsForHash().values(Mockito.eq(nsKey(EVENT_HANDLERS)));
        Mockito.verify(redisTemplate).opsForSet().members(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, "nonexistentEvent")));
    }

    private String toJson(EventHandler eventHandler) {
        try {
            return objectMapper.writeValueAsString(eventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert EventHandler to JSON", e);
        }
    }
}