
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private static final String TEST_NAME = "test";

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private EventHandler eventHandler;

    @Mock
    private JedisProxy jedisProxy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
        when(eventHandler.getName()).thenReturn(TEST_NAME);
    }

    @Test
    public void testAddEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(TEST_NAME)).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_WhenHandlerExists() {
        when(redisEventHandlerDAO.getEventHandler(TEST_NAME)).thenReturn(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(TEST_NAME)).thenReturn(eventHandler);
        when(eventHandler.getEvent()).thenReturn("test_event");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_WhenHandlerNotExists() {
        when(redisEventHandlerDAO.getEventHandler(TEST_NAME)).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_WhenHandlerNotExists() {
        when(redisEventHandlerDAO.getEventHandler(TEST_NAME)).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler(TEST_NAME);
    }

    @Test
    public void testRemoveEventHandler() {
        when(redisEventHandlerDAO.getEventHandler(TEST_NAME)).thenReturn(eventHandler);
        redisEventHandlerDAO.removeEventHandler(TEST_NAME);
        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertTrue(allEventHandlers.isEmpty());
        verify(jedisProxy, times(1)).hgetAll(any());
    }


    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("test_event", false);
        assertTrue(eventHandlersForEvent.isEmpty());
        verify(jedisProxy, times(1)).smembers(any());
    }
  
  @After
  public void tearDown() {
    jedisProxy.close();
    redisEventHandlerDAO = null;
    eventHandler = null;
  }
}
