package com.netflix.conductor.redis.dao;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.JacksonUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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

    private static final String NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String JSON = "{\"name\":\"%s\",\"event\":\"%s\",\"active\":true}";

    @Before
    public void setup() {
        doReturn(redisProperties).when(conductorProperties).getRedisProperties();
        doReturn(jedisProxy).when(redisProperties).getJedisProxy();
        doReturn(objectMapper).when(conductorProperties).getObjectMapper();
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(anyString(), anyString(), eq(JSON.formatted(NAME, EVENT)));
    }

    @Test
    public void testAddEventHandlerWithConflict() throws Exception {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(NAME);
        existingEventHandler.setEvent(EVENT);
        existingEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(existingEventHandler));

        verify(jedisProxy, never()).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(NAME);
        updatedEventHandler.setEvent("updatedEvent");
        updatedEventHandler.setActive(false);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy).hset(anyString(), anyString(), eq(JSON.formatted(NAME, "updatedEvent")));
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);
        eventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(NAME);

        verify(jedisProxy).hdel(anyString(), anyString());
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(EVENT);
        eventHandler2.setActive(false);

        Map<String, String> all = mock(Map.class);
        doReturn(all).when(jedisProxy).hgetAll(anyString());
        doReturn(Lists.newArrayList(eventHandler1, eventHandler2)).when(objectMapper).readValue(anyString(), List.class);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertFalse(eventHandlers.contains(eventHandler2.getEvent()));
    }

    @Test
    public void testGetEventHandler() throws Exception {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(NAME);
        eventHandler.setEvent(EVENT);
        eventHandler.setActive(true);

        String json = JacksonUtils.writeValueAsString(eventHandler);

        Map<String, String> all = mock(Map.class);
        doReturn(all).when(jedisProxy).hgetAll(anyString());
        doReturn(json).when(all).get(NAME);
        doReturn(eventHandler).when(objectMapper).readValue(anyString(), EventHandler.class);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(NAME);

        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test
    public void testGetEventHandlerNotFound() throws Exception {
        assertNull(redisEventHandlerDAO.getEventHandler("nonExistentEventHandler"));
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("differentEvent");
        eventHandler2.setActive(false);

        Set<String> set = mock(Set.class);
        doReturn(set).when(jedisProxy).smembers(anyString());
        doReturn(Collections.singletonList(eventHandler1)).when(objectMapper).readValue(anyString(), List.class);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() throws Exception {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(EVENT);
        eventHandler2.setActive(false);

        Set<String> set = mock(Set.class);
        doReturn(set).when(jedisProxy).smembers(anyString());
        doReturn(Lists.newArrayList(eventHandler1, eventHandler2)).when(objectMapper).readValue(anyString(), List.class);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() throws Exception {
        Set<String> set = mock(Set.class);
        doReturn(Collections.emptySet()).when(jedisProxy).smembers(anyString());

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(0, eventHandlers.size());
    }
}