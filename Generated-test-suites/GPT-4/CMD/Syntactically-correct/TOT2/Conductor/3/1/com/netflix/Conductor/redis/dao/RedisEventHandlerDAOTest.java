package com.netflix.conductor.redis.dao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
    
    @Mock
    RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    // test for addEventHandler method
    @Test
    public void addEventHandlerTest() {
        // construct an eventHandler

        // call the method

        // validate the outcomes
    }

    // test for updateEventHandler method
    @Test
    public void updateEventHandlerTest() {
        // construct an existing and an updated eventHandler

        // call the update method

        // validate the outcomes
    }

    // test for removeEventHandler method
    @Test
    public void removeEventHandlerTest() {
        // construct an eventHandler to be removed

        // call the remove method

        // validate the outcomes
    }

    // test for getAllEventHandlers method
    @Test
    public void getAllEventHandlersTest() {
        // add a few eventHandlers

        // call the getAll method

        // validate the outcomes
    }

    // test for getEventHandlersForEvent method
    @Test
    public void getEventHandlersForEventTest() {
        // add a few eventHandlers

        // call the getEventHandlersForEvent method

        // validate the outcomes
    }

    // test for getEventHandler method
    @Test
    public void getEventHandlerTest() {
        // construct an eventHandler

        // call the getEventHandler method

        // validate the outcomes
    }
}