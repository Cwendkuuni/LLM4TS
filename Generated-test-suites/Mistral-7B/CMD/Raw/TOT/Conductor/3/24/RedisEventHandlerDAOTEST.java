package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Component
@Conditional(AnyRedisCondition.class)
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandlerSuccess() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(redisEventHandlerDAO.toJson(eventHandler)).thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(redisEventHandlerDAO.jedisProxy()).hset(eq("EVENT_HANDLERS:testEventHandler"), anyString(), eq("{}"));
        verify(redisEventHandlerDAO.jedisProxy()).sadd(eq("EVENT_HANDLERS_BY_EVENT:testEvent"), eq("testEventHandler"));
        verify(redisEventHandlerDAO).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(true);

        when(redisEventHandlerDAO.toJson(existingEventHandler)).thenReturn("{}");
        when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("testEvent");
        newEventHandler.setActive(true);

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        verify(redisEventHandlerDAO, times(1)).getEventHandler("testEventHandler");
        verify(redisEventHandlerDAO).throwConflictException("EventHandler with name testEventHandler already exists!");
    }

    @Test
    public void testUpdateEventHandlerSuccess() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");
        existingEventHandler.setActive(false);

        when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(existingEventHandler);
        when(redisEventHandlerDAO.toJson(existingEventHandler)).thenReturn("{}");

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("updatedTestEvent");
        updatedEventHandler.setActive(true);

        when(redisEventHandlerDAO.toJson(updatedEventHandler)).thenReturn("{}");

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(redisEventHandlerDAO.jedisProxy()).hset(eq("EVENT_HANDLERS:testEventHandler"), anyString(), eq("{}"));
        verify(redisEventHandlerDAO.jedisProxy()).sadd(eq("EVENT_HANDLERS_BY_EVENT:updatedTestEvent"), eq("testEventHandler"));
        verify(redisEventHandlerDAO.jedisProxy()).srem(eq("EVENT_HANDLERS_BY_EVENT:testEvent"), eq("testEventHandler"));
        verify(redisEventHandlerDAO).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("testEventHandler");
        updatedEventHandler.setEvent("updatedTestEvent");
        updatedEventHandler.setActive(true);

        when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(redisEventHandlerDAO).throwNotFoundException("EventHandler with name testEventHandler not found!");
    }

    @Test
    public void testRemoveEventHandlerSuccess() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        eventHandler.setActive(true);

        when(redisEventHandlerDAO.getEventHandler("testEventHandler")).thenReturn(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(redisEventHandlerDAO.jedisProxy()).hdel(eq("EVENT_HANDLERS:testEventHandler"), anyString());
        verify(redisEventHandlerDAO).recordRedisDaoRequests("removeEventHandler");
        verify(redisEventHandlerDAO).removeIndex(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        verify(redisEventHandlerDAO).getEventHandler("testEventHandler");
        verify(redisEventHandlerDAO).throwNotFoundException("EventHandler with name testEventHandler not found!");
    }

    @Test
    public void testGetAllEventHandlersSuccess() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        Map<String, String> all = Mockito.mock(Map.class);
        when(redisEventHandlerDAO.jedisProxy().hgetAll(eq("EVENT_HANDLERS"))).thenReturn(all);

        when(redisEventHandlerDAO.readValue((String) all.get("eventHandler1"), EventHandler.class)).thenReturn(eventHandler1);
        when(redisEventHandlerDAO.readValue((String) all.get("eventHandler2"), EventHandler.class)).thenReturn(eventHandler2);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventSuccess() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("eventHandler3");
        eventHandler3.setEvent("event2");
        eventHandler3.setActive(true);

        Set<String> names = Mockito.mock(Set.class);
        when(redisEventHandlerDAO.jedisProxy().smembers(eq("EVENT_HANDLERS_BY_EVENT:event1"))).thenReturn(names);

        when(redisEventHandlerDAO.getEventHandler("eventHandler1")).thenReturn(eventHandler1);
        when(redisEventHandlerDAO.getEventHandler("eventHandler2")).thenReturn(eventHandler2);
        when(redisEventHandlerDAO.getEventHandler("eventHandler3")).thenReturn(eventHandler3);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));

        List<EventHandler> activeHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(1, activeHandlers.size());
        assertEquals(eventHandler1, activeHandlers.get(0));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() {
        Set<String> names = Mockito.mock(Set.class);
        when(redisEventHandlerDAO.jedisProxy().smembers(eq("EVENT_HANDLERS_BY_EVENT:event1"))).thenReturn(names);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        assertEquals(0, handlers.size());
    }
}