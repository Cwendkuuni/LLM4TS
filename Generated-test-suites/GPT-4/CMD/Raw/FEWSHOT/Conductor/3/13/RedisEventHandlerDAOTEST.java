package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Conditional;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy = mock(JedisProxy.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private ConductorProperties conductorProperties = new ConductorProperties();
    private RedisProperties properties = Mockito.mock(RedisProperties.class);

    private RedisEventHandlerDAO eventHandlerDAO
            = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);


    @Test(expected = ConflictException.class)
    public void addEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Handler");

        when(jedisProxy.hget(any(), anyString())).thenReturn("Handler");
        eventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandlerNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Handler");

        eventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void removeExistingEventHandler() {
        eventHandlerDAO.removeEventHandler("Handler");
    }

    @Test
    public void getAllEventHandlers() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Handler");

        when(jedisProxy.hgetAll(any())).thenReturn(Collections.singletonMap("eventHandler", toJson(eventHandler)));
        List<EventHandler> handlers = eventHandlerDAO.getAllEventHandlers();

        assertNotNull(handlers);
        assertEquals(1, handlers.size());
        assertEquals("Handler", handlers.get(0).getName());
    }

    @Test
    public void getEventHandlersForEvent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("Handler");
        eventHandler.setActive(true);
        eventHandler.setEvent("event");

        when(jedisProxy.smembers(any())).thenReturn(new HashSet<>(Arrays.asList("Handler", "OtherHandler")));
        when(jedisProxy.hget(any(), eq("Handler"))).thenReturn(toJson(eventHandler));
        when(jedisProxy.hget(any(), eq("OtherHandler"))).thenReturn(null);

        List<EventHandler> handlers = eventHandlerDAO.getEventHandlersForEvent("event", true);

        assertNotNull(handlers);
        assertEquals(1, handlers.size());
        assertEquals("Handler", handlers.get(0).getName());
    }

    private String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}