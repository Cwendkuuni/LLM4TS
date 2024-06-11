
package com.netflix.conductor.redis.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

public class RedisEventHandlerDAOTest {

  @Mock
  private JedisProxy jedisProxy;

  @InjectMocks
  private RedisEventHandlerDAO redisEventHandlerDAO;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void addEventHandlerTest() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("event_handler");
    
    when(jedisProxy.hget("EVENT_HANDLERS", "event_handler")).thenReturn(null);
    doNothing().when(jedisProxy).hset("EVENT_HANDLERS", "event_handler", "event_handler_json");

    redisEventHandlerDAO.addEventHandler(eventHandler);
    
    verify(jedisProxy).hset("EVENT_HANDLERS", "event_handler", "event_handler_json");
  }

  @Test(expected = ConflictException.class)
  public void addEventHandlerConflictTest() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("event_handler");
    
    when(jedisProxy.hget("EVENT_HANDLERS", "event_handler")).thenReturn("event_handler_json");

    redisEventHandlerDAO.addEventHandler(eventHandler);
  }

  @Test
  public void updateEventHandlerTest() {
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("event_handler");
    eventHandler.setEvent("event");
    
    // divide and conquer
    // ...
  }

  @Test
  public void removeEventHandlerTest() {

    // divide and conquer
    // ...
  }

  @Test
  public void getAllEventHandlersTest() {

    // divide and conquer
    // ...
  }

  @Test
  public void getEventHandlersForEventTest() {

    // divide and conquer
    // ...
  }

  private Map<String, String> getEventHandlerMap() {
    Map<String, String> all = new HashMap<>();
    all.put("event_handler", "event_handler_json");
    
    return all;
  }

  // add more helper methods as needed
}
