package com.netflix.conductor.redis.dao.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.testutils.TestRedisConnectionFactory;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private ObjectMapper objectMapper;
    private JedisProxy jedisProxy;

    @Before
    public void setup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(TestRedisConnectionFactory.class);
        context.refresh();

        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        JedisProxy jedisProxy = context.getBean(JedisProxy.class);
        ConductorProperties conductorProperties = context.getBean(ConductorProperties.class);
        RedisProperties redisProperties = context.getBean(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
        this.objectMapper = objectMapper;
        this.jedisProxy = jedisProxy;
    }

    @Test
    public void testAddEventHandler() {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        EventHandler eventHandler = createEventHandler(eventName, handlerName);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventName);
        assertEquals(handlerName, retrievedEventHandler.getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        String eventName = "testEvent";
        String handlerName = "testHandler";
        EventHandler existingEventHandler = createEventHandler(eventName, handlerName);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = createEventHandler(eventName, handlerName);
        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerNotFound() {
        String eventName = UUID.randomUUID().toString();
        redisEventHandlerDAO.getEventHandler(eventName);
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerException() {
        String eventName = "testEvent";
        Mockito.doThrow(new RuntimeException("Mock Exception")).when(jedisProxy).hget(Mockito.anyString(), Mockito.anyString());
        redisEventHandlerDAO.getEventHandler(eventName);
    }

    @Test
    public void testUpdateEventHandler() {
        String eventName = "testEvent";
        String oldHandlerName = "oldHandler";
        String newHandlerName = "newHandler";
        EventHandler existingEventHandler = createEventHandler(eventName, oldHandlerName);
        EventHandler newEventHandler = createEventHandler(eventName, newHandlerName);

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventName);
        assertEquals(newHandlerName, retrievedEventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        String eventName = UUID.randomUUID().toString();
        EventHandler newEventHandler = createEventHandler(eventName, "newHandler");
        redisEventHandlerDAO.updateEventHandler(newEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        String eventName = UUID.randomUUID().toString();
        redisEventHandlerDAO.removeEventHandler(eventName);
    }

    @Test
    public void testGetAllEventHandlers() {
        String eventName1 = "event1";
        String handlerName1 = "handler1";
        String eventName2 = "event2";
        String handlerName2 = "handler2";

        EventHandler eventHandler1 = createEventHandler(eventName1, handlerName1);
        EventHandler eventHandler2 = createEventHandler(eventName2, handlerName2);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "testEvent";
        String handlerName1 = "handler1";
        String handlerName2 = "handler2";

        EventHandler eventHandler1 = createEventHandler(eventName, handlerName1);
        EventHandler eventHandler2 = createEventHandler(eventName, handlerName2);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);
        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);
        assertEquals(2, activeEventHandlers.size());
        assertTrue(eventHandlers.containsAll(activeEventHandlers));
    }

    private EventHandler createEventHandler(String eventName, String handlerName) {
        return new EventHandler() {
            @Override
            public String getName() {
                return handlerName;
            }

            @Override
            public String getEvent() {
                return eventName;
            }

            @Override
            public Map<String, Object> getConfig() {
                return ImmutableMap.of();
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };
    }

    private void recordRedisDaoRequests(String methodName) {
        // TODO: Implement mocking or stubbing to record Redis Dao requests
    }
}