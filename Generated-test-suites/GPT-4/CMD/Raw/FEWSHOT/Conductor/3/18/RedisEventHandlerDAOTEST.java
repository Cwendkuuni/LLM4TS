
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

public class RedisEventHandlerDAOTest {

    @Mock
    JedisProxy jedisProxy;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ConductorProperties conductorProperties;

    @Mock
    RedisProperties properties;

    @InjectMocks
    RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = getDummyEventHandler();
        String name = eventHandler.getName();
        doReturn(null).when(jedisProxy).hget(nsKey("EVENT_HANDLERS"), name);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(nsKey("EVENT_HANDLERS"), name, toJson(eventHandler));
        // You can add some AssertEquals here, checking the current status of redis/eventhandler after the call.
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = getDummyEventHandler();
        String name = eventHandler.getName();
        doReturn(eventHandler).when(jedisProxy).hget(nsKey("EVENT_HANDLERS"), name);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(nsKey("EVENT_HANDLERS"), name, toJson(eventHandler));
        // You can add some AssertEquals here, checking the current status of redis/eventhandler after the call.
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = getDummyEventHandler();
        String name = eventHandler.getName();
        doReturn(eventHandler).when(jedisProxy).hget(nsKey("EVENT_HANDLERS"), name);
        redisEventHandlerDAO.removeEventHandler(name);
        verify(jedisProxy, times(1)).hdel(nsKey("EVENT_HANDLERS"), name);
        // You can add some AssertEquals here, checking the current status of redis/eventhandler after the call.
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = Arrays.asList(getDummyEventHandler(), getDummyEventHandler());
        doReturn(handlers).when(jedisProxy).hgetAll(nsKey("EVENT_HANDLERS"));
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(handlers.size(), result.size());
    }

    @Test
    public void testgetEventHandlersForEvent() {
        EventHandler eventHandler = getDummyEventHandler();
        String event = eventHandler.getEvent();
        String name = eventHandler.getName();
        Set<String> names = Set.of(name);
        doReturn(names).when(jedisProxy).smembers(nsKey("EVENT_HANDLERS_BY_EVENT", event));
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(names.size(), result.size());
    }

    private void nsKey(String s) {
        return s;
    }

    private void toJson(EventHandler eh) {
        return eh;
    }

    private EventHandler getDummyEventHandler() {
        EventHandler eh = new EventHandler();
        eh.setName("DummyEventHandler");
        eh.setEvent("DummyEvent");
        eh.setActive(true);
        return eh;
    }
}
