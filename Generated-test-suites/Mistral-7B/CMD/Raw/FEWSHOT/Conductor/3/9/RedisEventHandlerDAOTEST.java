package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisPropertiesTest;
import com.netflix.conductor.redis.TestJsonMapper;
import com.netflix.conductor.redis.dao.BaseDynoDAOTest;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest extends BaseDynoDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(properties.getNamespace()).thenReturn("test:namespace");
        when(conductorProperties.getNamespace()).thenReturn("test:namespace");
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy, new TestJsonMapper(), conductorProperties, properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandler.getName()), anyString());
        verify(jedisProxy, times(1)).sadd(anyString(), eq(eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = createEventHandler();
        EventHandler existingEventHandler = createEventHandler();
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName())).thenReturn(toJson(existingEventHandler));
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = createEventHandler();
        EventHandler eventHandler2 = createEventHandler();
        List<EventHandler> eventHandlers = Lists.newLinkedList();
        eventHandlers.add(eventHandler1);
        eventHandlers.add(eventHandler2);

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(mapHandlerEventHandler(eventHandlers));
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(eventHandlers, result);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandlerToUpdate = createEventHandler();
        EventHandler existingEventHandler = createEventHandler();
        existingEventHandler.setName("existingName");
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandlerToUpdate.getName()))
                .thenReturn(toJson(existingEventHandler));

        EventHandler updatedEventHandler = createEventHandler();
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        verify(jedisProxy).hset(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerToUpdate.getName()), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandlerToUpdate = createEventHandler();
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandlerToUpdate.getName())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandlerToUpdate);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandlerToRemove = createEventHandler();
        redisEventHandlerDAO.removeEventHandler(eventHandlerToRemove.getName());
        verify(jedisProxy).hdel(eq(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS)), eq(eventHandlerToRemove.getName()));
        verify(jedisProxy, times(1)).srem(anyString(), eq(eventHandlerToRemove.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String name = UUID.randomUUID().toString();
        redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = createEventHandler();
        EventHandler eventHandler2 = createEventHandler();

        String event = "testEvent";
        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event)))
                .thenReturn(Set.of(eventHandler1.getName(), eventHandler2.getName()));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(Lists.newLinkedList(eventHandler1, eventHandler2), result);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        EventHandler eventHandler1 = createEventHandler();
        EventHandler eventHandler2 = createEventHandler();
        eventHandler2.setActive(false);

        String event = "testEvent";
        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, event)))
                .thenReturn(Set.of(eventHandler1.getName(), eventHandler2.getName()));

        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(Lists.newLinkedList(eventHandler1), result);
    }

    private EventHandler createEventHandler() {
        return new EventHandler() {
            @Override
            public String getName() {
                return "testName";
            }

            @Override
            public String getEvent() {
                return "testEvent";
            }

            @Override
            public String getVersion() {
                return null;
            }

            @Override
            public Map<String, Object> getConfig() {
                return Map.of();
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public String getClassName() {
                return "TestEventHandler";
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };
    }

    private Map<String, String> mapHandlerEventHandler(List<EventHandler> handlers) {
        Map<String, String> result = new LinkedHashMap<>();
        for (EventHandler handler : handlers) {
            result.put(handler.getName(), toJson(handler));
        }
        return result;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}