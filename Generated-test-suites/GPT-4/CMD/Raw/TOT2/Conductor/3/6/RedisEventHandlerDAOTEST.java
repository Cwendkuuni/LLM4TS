package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @InjectMocks
    private RedisEventHandlerDAO daoUnderTest;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler_alreadyExists() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("event1");
        doReturn(eventHandler).when(jedisProxy).hget("EVENT_HANDLERS", "event1");
        daoUnderTest.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandler_doesNotExistThrowsNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("event1");
        doReturn(null).when(jedisProxy).hget("EVENT_HANDLERS", "event1");
        daoUnderTest.updateEventHandler(eventHandler);
    }

    @Test
    public void removeEventHandler_existingHandler_success() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("event1");
        doReturn(eventHandler).when(jedisProxy).hget("EVENT_HANDLERS", "event1");
        daoUnderTest.removeEventHandler("event1");
        verify(jedisProxy).hdel("EVENT_HANDLERS", "event1");
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandler_doesNotExistThrowsNotFoundException() {
        doReturn(null).when(jedisProxy).hget("EVENT_HANDLERS", "event1");
        daoUnderTest.removeEventHandler("event1");
    }

    @Test
    public void getAllEventHandlers_returnSuccessfully() {
        daoUnderTest.getAllEventHandlers();
        verify(jedisProxy).hgetAll("EVENT_HANDLERS");
    }

    @Test
    public void getEventHandlersForEvent_returnSuccessfully() {
        doReturn(new HashSet<>(Arrays.asList("handler1"))).when(jedisProxy).smembers("EVENT_HANDLERS_BY_EVENT", "event1");
        List<EventHandler> list = daoUnderTest.getEventHandlersForEvent("event1", false);
        assertNotNull(list);
        assertEquals(1, list.size());
    }
}