
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO eventHandlerDAO;

    @Before
    public void setUp() throws Exception {
        jedisProxy = Mockito.mock(JedisProxy.class);
        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        RedisProperties properties = Mockito.mock(RedisProperties.class);
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, properties);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        eventHandlerDAO.removeEventHandler("testEvent");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_Conflict() {
        EventHandler evtHandler = createEventHandler();
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn("test data");

        eventHandlerDAO.addEventHandler(evtHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        EventHandler evtHandler = createEventHandler();
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        eventHandlerDAO.updateEventHandler(evtHandler);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(new HashSet<String>(){{
            add("testName1");
            add("testName2");
        }});

        List<EventHandler> handlers = eventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(2, handlers.size());
    }

    private EventHandler createEventHandler() {
        EventHandler evtHandler = new EventHandler();
        evtHandler.setName("testName");
        evtHandler.setEvent("testEvent");
        evtHandler.setActive(true);

        return evtHandler;
    }
}
