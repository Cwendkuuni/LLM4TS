
package com.netflix.conductor.redis.dao;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO dao;
    private JedisProxy jedisProxy;

    @Before
    public void setUp() {
        jedisProxy = mock(JedisProxy.class);
        dao = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void addEventHandlerTest() { 
        // Given
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test");

        // When
        dao.addEventHandler(eventHandler);

        // Then
        verify(jedisProxy).hset(nsKey("EVENT_HANDLERS"), eventHandler.getName(), toJson(eventHandler));
    }

    @Test(expected=ConflictException.class)
    public void addEventHandlerConflictTest() {
        // TBC - Prepare DAO to contain EventHandler with same name
    }

    // Continue in similar fashion for updateEventHandler, removeEventHandler and  getEventHandlerForEvent

    @Test(expected=NotFoundException.class)
    public void updateEventHandlerNotFoundTest() {
        // TBC - Do not prepare DAO, no EventHandler should be found
    }
    
    @Test(expected=NotFoundException.class)
    public void removeEventHandlerNotFoundTest() {
        // TBC - Do not prepare DAO, no EventHandler should be found
    }

    @Test(expected=TransitException.class)
    public void getEventHandlerForEventTransientExceptionTest() {
        // TBC - Prepare JedisProxy to throw a runtime exception
    }
}
