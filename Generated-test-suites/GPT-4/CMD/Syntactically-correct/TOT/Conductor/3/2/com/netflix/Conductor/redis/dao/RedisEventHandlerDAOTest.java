package com.netflix.conductor.redis.dao;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

import java.util.Map;

public class RedisEventHandlerDAOTest {
    @Mock 
    private JedisProxy jedisProxy;
    @Mock
    private Map<String, String> mockMap;
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private EventHandler eventHandler;
    private final String handlerName = "testEventHandler";
    private final String jsonHandler = "jsonStringRepresentation";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        eventHandler.setName(handlerName);
        eventHandler.setEvent("example");

        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerThrowsExceptionExistingHandler() {
        when(redisEventHandlerDAO.get()).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testAddEventHandler() {
        when(redisEventHandlerDAO.get()).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy).hset("EVENT_HANDLERS", handlerName, jsonHandler);
        verify(jedisProxy).sadd("EVENT_HANDLERS_BY_EVENT_"+ eventHandler.getEvent(), handlerName);
    }
    
    //other tests...
}
