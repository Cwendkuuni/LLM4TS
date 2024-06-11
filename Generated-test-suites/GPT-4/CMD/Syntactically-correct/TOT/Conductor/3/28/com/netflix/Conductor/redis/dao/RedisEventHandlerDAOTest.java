package com.netflix.conductor.redis.dao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler_testExistingEventHandler() throws Exception {
        EventHandler existingHandler = new EventHandler();
        existingHandler.setName("existingHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(existingHandler);

        redisEventHandlerDAO.addEventHandler(existingHandler);
    }

    @Test
    public void addEventHandler_testNewEventHandler() throws Exception {
        EventHandler newHandler = new EventHandler();
        newHandler.setName("newHandler");

        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        when(redisEventHandlerDAO.getEventHandler(anyString())).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(newHandler);
    }
}