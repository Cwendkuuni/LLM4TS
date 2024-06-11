
package com.netflix.conductor.redis.dao;

import org.junit.Test;
import static org.mockito.Mockito.*;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

public class RedisEventHandlerDAOTest {

  @Test
  public void testAddEventHandler() {
    JedisProxy jedisProxy = mock(JedisProxy.class);
    ObjectMapper objectMapper = new ObjectMapper();
    ConductorProperties conductorProperties = new ConductorProperties();
    RedisProperties redisProperties = new RedisProperties();
    RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    
    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("test");
    dao.addEventHandler(eventHandler);
    
    verify(jedisProxy, times(1)).hset(any(String.class), eq("test"), any(String.class));
  }

  @Test(expected=NotFoundException.class)
  public void testUpdateEventHandler() {
    JedisProxy jedisProxy = mock(JedisProxy.class);
    ObjectMapper objectMapper = new ObjectMapper();
    ConductorProperties conductorProperties = new ConductorProperties();
    RedisProperties redisProperties = new RedisProperties();
    RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

    EventHandler eventHandler = new EventHandler();
    eventHandler.setName("testUpdate");
    dao.updateEventHandler(eventHandler);
  }

  @Test(expected=NotFoundException.class)
  public void testRemoveEventHandler() {
    JedisProxy jedisProxy = mock(JedisProxy.class);
    ObjectMapper objectMapper = new ObjectMapper();
    ConductorProperties conductorProperties = new ConductorProperties();
    RedisProperties redisProperties = new RedisProperties();
    RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

    dao.removeEventHandler("testRemove");
  }

  @Test
  public void testGetAllEventHandlers() {
    JedisProxy jedisProxy = mock(JedisProxy.class);
    ObjectMapper objectMapper = new ObjectMapper();
    ConductorProperties conductorProperties = new ConductorProperties();
    RedisProperties redisProperties = new RedisProperties();
    RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

    List<EventHandler> eventHandlers = dao.getAllEventHandlers();

    assertNotNull(eventHandlers);
    assertTrue(eventHandlers instanceof ArrayList);
  }

  @Test(expected=NotFoundException.class)
  public void testGetEventHandlersForEvent() {
    JedisProxy jedisProxy = mock(JedisProxy.class);
    ObjectMapper objectMapper = new ObjectMapper();
    ConductorProperties conductorProperties = new ConductorProperties();
    RedisProperties redisProperties = new RedisProperties();
    RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

    dao.getEventHandlersForEvent("nonExistingEvent", true);
  }

}
