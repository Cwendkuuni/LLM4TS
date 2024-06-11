package com.netflix.conductor.redis.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy = Mockito.mock(JedisProxy.class);
    private ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
    private ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
    private RedisProperties properties = Mockito.mock(RedisProperties.class);

    @Before
    public void setup() {
        this.redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        // given
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        // when
        redisEventHandlerDAO.addEventHandler(eventHandler);

        // then
        EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler("testEventHandler");
        assertNotNull(actualEventHandler);
        assertNotNull(actualEventHandler.getName());
        assertNull(actualEventHandler.getEvent());

        String json = jedisProxy.hget(nsKey(EVENT_HANDLERS), "testEventHandler");
        EventHandler expectedEventHandler = new EventHandler();
        expectedEventHandler.setName("testEventHandler");
        Mockito.when(objectMapper.readValue(json, EventHandler.class)).thenReturn(expectedEventHandler);
        assertNotNull(expectedEventHandler);
        assertNotNull(expectedEventHandler.getName());
        assertNull(expectedEventHandler.getEvent());
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        // given
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("existingEventHandler");
        existingEventHandler.setEvent("event1");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("existingEventHandler");
        newEventHandler.setEvent("event2");

        Mockito.when(jedisProxy.hget(nsKey(EVENT_HANDLERS), "existingEventHandler")).thenReturn("{\"name\":\"existingEventHandler\",\"event\":\"event1\"}");

        // when
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        // then
        EventHandler actualEventHandler = redisEventHandlerDAO.getEventHandler("existingEventHandler");
        assertNotNull(actualEventHandler);
        assertNotNull(actualEventHandler.getName());
        assertNotNull(actualEventHandler.getEvent());
        assertEquals("event2", actualEventHandler.getEvent());
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        // given
        String name = "testEventHandler";

        Mockito.when(jedisProxy.hget(nsKey(EVENT_HANDLERS), name)).thenReturn("{\"name\":\"testEventHandler\",\"event\":\"event1\"}");

        // when
        redisEventHandlerDAO.removeEventHandler(name);

        // then
        assertNull(redisEventHandlerDAO.getEventHandler(name));
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        // given
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("event1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("event1");

        Map<String, String> all = Maps.newLinkedHashMap();
        all.put("testEventHandler1", "{\"name\":\"testEventHandler1\",\"event\":\"event1\"}");
        all.put("testEventHandler2", "{\"name\":\"testEventHandler2\",\"event\":\"event1\"}");
        Mockito.when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // then
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        // given
        String event = "event1";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testEventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testEventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        String key = nsKey(EVENT_HANDLERS_BY_EVENT, event);
        Set<String> names = Set.of("testEventHandler1", "testEventHandler2");

        Mockito.when(jedisProxy.smembers(key)).thenReturn(names);

        // when
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        // then
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }
}