package com.netflix.conductor.redis.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {
    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    RedisEventHandlerDAO subject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addEventHandler() {
        EventHandler handler = mock(EventHandler.class);
        when(handler.getName()).thenReturn("name");
        when(subject.getEventHandler("name")).thenReturn(null);
        try {
            subject.addEventHandler(handler);
            verify(jedisProxy).hset(anyString(), anyString(), anyString());
            assertNotEquals(null, subject.getEventHandler("name"));
        } catch(ConflictException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler_alreadyExists() {
        EventHandler handler = mock(EventHandler.class);
        when(handler.getName()).thenReturn("name");
        when(subject.getEventHandler("name")).thenReturn(handler);
        subject.addEventHandler(handler);
    }

    // The remaining methods would be tested in a similar manner...
}
