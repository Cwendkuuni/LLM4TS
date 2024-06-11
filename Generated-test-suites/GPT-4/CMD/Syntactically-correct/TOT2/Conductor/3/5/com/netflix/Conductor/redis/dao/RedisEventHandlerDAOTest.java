package com.netflix.conductor.redis.dao;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;  
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    
    private EventHandler dummyHandler;

    @Before
    public void setUp() {
        dummyHandler = new EventHandler();
        dummyHandler.setName("dummyHandler");
    }

    @Test
    public void testAddEventHandler_normalCase() {
       // Given that the event handler does not exist yet
       when(daoUnderTest.getEventHandler(anyString())).thenReturn(null);

       // When adding the event handler
       daoUnderTest.addEventHandler(dummyHandler);

       // Then the event handler should have been saved correctly 
       verify(jedisProxy).hset(anyString(), anyString(), anyString());
       verify(jedisProxy).sadd(anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_AlreadyExists() {
        // Given that the event handler already exists
        when(daoUnderTest.getEventHandler(anyString())).thenReturn(dummyHandler);

        // When adding the event handler, exception is thrown
        daoUnderTest.addEventHandler(dummyHandler);
    }
}

// Other test cases would follow similarly...