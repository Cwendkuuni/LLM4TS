package com.netflix.conductor.redis.dao;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
  
  RedisEventHandlerDAO dao;
  
  @Before
  public void setup() {
      // Initialize JedisProxy, ObjectMapper, ConductorProperties, and RedisProperties Objects
      JedisProxy jedisProxy = mock(JedisProxy.class);
      ObjectMapper objectMapper = mock(ObjectMapper.class);
      ConductorProperties conductorProperties = mock(ConductorProperties.class);
      RedisProperties redisProperties = mock(RedisProperties.class);

      dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
  }

  @Test
  public void testAddEventHandler() {
      // Create EventHandler and add
      EventHandler handler = new EventHandler();
      dao.addEventHandler(handler);

      verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
  }

  @Test
  public void testUpdateEventHandler() {
      // Create EventHandler and update
      EventHandler handler = new EventHandler();
      dao.updateEventHandler(handler);

      verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
  }

  @Test
  public void testRemoveEventHandler() {
      // Remove event Handler
      dao.removeEventHandler("TestHandler");

      verify(jedisProxy, times(1)).hdel(anyString(), anyString());
  }

  @Test
  public void testGetAllEventHandlers() {
      // Test getAllEventHandlers
      dao.getAllEventHandlers();
      
      verify(jedisProxy, times(1)).hgetAll(anyString());
  }

  @Test
  public void testGetEventHandlersForEvent() {
      // Test getEventHandlersForEvent
      dao.getEventHandlersForEvent("TestEvent", true);
      
      verify(jedisProxy, times(1)).smembers(anyString());
  }
  
}