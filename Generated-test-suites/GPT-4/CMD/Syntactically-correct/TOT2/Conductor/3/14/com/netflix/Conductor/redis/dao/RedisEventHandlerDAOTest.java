package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private EventHandler testEventHandler;
    
    @Before
    public void setUp() {
      jedisProxy = mock(JedisProxy.class);
      redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());

      testEventHandler = new EventHandler();
      testEventHandler.setName("Test");
    }
    
    @Test
    public void addEventHandlerTest(){
      assertNull(redisEventHandlerDAO.getEventHandler(testEventHandler.getName()));
      redisEventHandlerDAO.addEventHandler(testEventHandler);
      Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
      assertNotNull(redisEventHandlerDAO.getEventHandler(testEventHandler.getName()));
    }
    
    @Test(expected = ConflictException.class)
    public void addEventHandlerConflictTest() {
      redisEventHandlerDAO.addEventHandler(testEventHandler);
      redisEventHandlerDAO.addEventHandler(testEventHandler);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addNullEventHandlerTest() {
      EventHandler nullEvent = new EventHandler();
      nullEvent.setName(null);
      redisEventHandlerDAO.addEventHandler(nullEvent);
    }
}