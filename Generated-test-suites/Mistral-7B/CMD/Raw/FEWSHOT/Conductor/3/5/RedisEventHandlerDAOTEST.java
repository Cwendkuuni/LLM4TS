package com.netflix.conductor.redis.dao;

import static com.netflix.conductor.common.metadata.events.EventHandler.Status.ACTIVE;
import static com.netflix.conductor.common.metadata.events.EventHandler.Status.INACTIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.common.metadata.events.EventHandler.Status;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.utils.TestUtils;
import com.netflix.conductor.redis.utils.json.JacksonObjectMapper;

@SuppressWarnings({ "unchecked" })
public class RedisEventHandlerDAOTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventHandlerDAOTest.class);
    private static final String NAMESPACE = "testNamespace";
    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";
    private static final String EVENT_1 = "event1";
    private static final String EVENT_2 = "event2";
    private static final String HANDLER_1_NAME = "handler1";
    private static final String HANDLER_2_NAME = "handler2";
    private static final String HANDLER_1_JSON = "{\"name\":\"" + HANDLER_1_NAME + "\",\"event\":\"" + EVENT_1 + "\",\"status\":\""
            + Status.ACTIVE.name() + "\"}";
    private static final String HANDLER_2_JSON = "{\"name\":\"" + HANDLER_2_NAME + "\",\"event\":\"" + EVENT_1 + "\",\"status\":\""
            + Status.INACTIVE.name() + "\"}";
    private static final EventHandler HANDLER_1 = new HandlerBuilder().setName(HANDLER_1_NAME).setEvent(EVENT_1).setStatus(ACTIVE)
            .build();
    private static final EventHandler HANDLER_2 = new HandlerBuilder().setName(HANDLER_2_NAME).setEvent(EVENT_1).setStatus(INACTIVE)
            .build();
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        JedisProxy jedisMock = mock(JedisProxy.class);
        when(jedisMock.hget(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_1_NAME)).thenReturn(HANDLER_1_JSON);
        when(jedisMock.hget(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_2_NAME)).thenReturn(HANDLER_2_JSON);
        when(jedisMock.hgetAll(NAMESPACE + ":" + EVENT_HANDLERS)).thenReturn(new HashMap<>());
        when(jedisMock.sadd(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, HANDLER_1_NAME)).thenReturn(1);
        when(jedisMock.sadd(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, HANDLER_2_NAME)).thenReturn(1);
        when(jedisMock.smembers(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1)).thenReturn(ImmutableSet.of(HANDLER_1_NAME,
                HANDLER_2_NAME));
        when(jedisMock.hset(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_1_NAME, HANDLER_1_JSON)).thenReturn(1);
        when(jedisMock.hset(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_2_NAME, HANDLER_2_JSON)).thenReturn(1);
        when(jedisMock.hdel(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_1_NAME)).thenReturn(1);
        when(jedisMock.hdel(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_2_NAME)).thenReturn(1);
        when(jedisMock.srem(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, HANDLER_1_NAME)).thenReturn(1);
        when(jedisMock.srem(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, HANDLER_2_NAME)).thenReturn(1);

        objectMapper = new JacksonObjectMapper();
        conductorProperties = mock(ConductorProperties.class);
        redisProperties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisMock, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void addEventHandler() {
        EventHandler handler = new HandlerBuilder().setName(UUID.randomUUID().toString()).setEvent(EVENT_2).setStatus(ACTIVE)
                .build();
        redisEventHandlerDAO.addEventHandler(handler);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisMock).hset(NAMESPACE + ":" + EVENT_HANDLERS, keyCaptor.capture(), jsonCaptor.capture());
        assertEquals(handler.getName(), keyCaptor.getValue());
        assertEquals(HANDLER_1_JSON, jsonCaptor.getValue());
        verify(jedisMock).sadd(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_2, handler.getName());
        verifyZeroInteractions(jedisMock).hdel(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_2);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflict() {
        EventHandler handler = new HandlerBuilder().setName(HANDLER_1_NAME).setEvent(EVENT_1).setStatus(ACTIVE).build();
        redisEventHandlerDAO.addEventHandler(handler);
    }

    @Test
    public void updateEventHandler() {
        EventHandler handler = new HandlerBuilder().setName(HANDLER_1_NAME).setEvent(EVENT_1).setStatus(INACTIVE).build();
        redisEventHandlerDAO.updateEventHandler(handler);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(jedisMock).hset(NAMESPACE + ":" + EVENT_HANDLERS, keyCaptor.capture(), jsonCaptor.capture());
        assertEquals(HANDLER_1_NAME, keyCaptor.getValue());
        assertEquals(HANDLER_1_JSON, jsonCaptor.getValue());
        verify(jedisMock).sadd(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, handler.getName());
        verifyZeroInteractions(jedisMock).hdel(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1);
        verify(jedisMock).hdel(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_1_NAME);
        verify(jedisMock).srem(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, HANDLER_1_NAME);
        verifyZeroInteractions(jedisMock).hset(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_1_NAME, handler.toJson());
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandlerNotFound() {
        EventHandler handler = new HandlerBuilder().setName("non-existent-handler").setEvent(EVENT_1).setStatus(INACTIVE).build();
        redisEventHandlerDAO.updateEventHandler(handler);
    }

    @Test
    public void removeEventHandler() {
        redisEventHandlerDAO.removeEventHandler(HANDLER_1_NAME);

        verify(jedisMock).hdel(NAMESPACE + ":" + EVENT_HANDLERS, HANDLER_1_NAME);
        verify(jedisMock).srem(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1, HANDLER_1_NAME);
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFound() {
        redisEventHandlerDAO.removeEventHandler("non-existent-handler");
    }

    @Test
    public void getAllEventHandlers() {
        Map<String, String> handlersMap = new HashMap<>();
        handlersMap.put(HANDLER_1_NAME, HANDLER_1_JSON);
        handlersMap.put(HANDLER_2_NAME, HANDLER_2_JSON);

        when(jedisMock.hgetAll(NAMESPACE + ":" + EVENT_HANDLERS)).thenReturn(handlersMap);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, handlers.size());
        List<EventHandler> expectedHandlers = ImmutableList.of(HANDLER_1, HANDLER_2);
        verifyListEquals(expectedHandlers, handlers);
    }

    @Test
    public void getEventHandlersForEvent() {
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_1, true);
        assertEquals(2, handlers.size());
        List<EventHandler> expectedHandlers = ImmutableList.of(HANDLER_1, HANDLER_2);
        verifyListEquals(expectedHandlers, handlers);
    }

    @Test
    public void getEventHandlersForEventActiveOnly() {
        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_1, true);
        assertEquals(1, handlers.size());
        List<EventHandler> expectedHandlers = ImmutableList.of(HANDLER_1);
        verifyListEquals(expectedHandlers, handlers);
    }

    @Test
    public void getEventHandlersForEventEmptySet() {
        Set<String> emptySet = Sets.newHashSet();
        when(jedisMock.smembers(NAMESPACE + ":" + EVENT_HANDLERS_BY_EVENT + ":" + EVENT_1)).thenReturn(emptySet);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT_1, true);
        assertEquals(0, handlers.size());
    }

    private void verifyListEquals(List<EventHandler> expectedHandlers, List<EventHandler> actualHandlers) {
        for (int i = 0; i < expectedHandlers.size(); i++) {
            assertEquals(expectedHandlers.get(i).getName(), actualHandlers.get(i).getName());
            assertEquals(expectedHandlers.get(i).getEvent(), actualHandlers.get(i).getEvent());
            assertEquals(expectedHandlers.get(i).getStatus(), actualHandlers.get(i).getStatus());
        }
    }

    private static class HandlerBuilder {
        private String name;
        private String event;
        private Status status;

        public HandlerBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public HandlerBuilder setEvent(String event) {
            this.event = event;
            return this;
        }

        public HandlerBuilder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public EventHandler build() {
            return new EventHandler() {
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public String getEvent() {
                    return event;
                }

                @Override
                public Status getStatus() {
                    return status;
                }

                @Override
                public String toJson() {
                    Map<String, Object> json = new HashMap<>();
                    json.put("name", name);
                    json.put("event", event);
                    json.put("status", status.name());
                    return TestUtils.mapToJsonString(json);
                }
            };
        }
    }
}