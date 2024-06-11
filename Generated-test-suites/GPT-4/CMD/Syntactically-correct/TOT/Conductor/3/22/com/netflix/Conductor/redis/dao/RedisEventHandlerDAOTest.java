package com.netflix.conductor.redis.dao;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest{
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private EventHandler eventHandler;
    private String name;

    @Before
    public void setup(){
        jedisProxy = Mockito.mock(JedisProxy.class);
        eventHandler = createEventHandlerForTest();
        name = "eventHandlerName";
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void addEventHandlerTest(){
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflictTest(){
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("Existing Value");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void updateEventHandlerTest(){
      when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));
      redisEventHandlerDAO.updateEventHandler(eventHandler);
      verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandlerNotFoundTest(){
      when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
      redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void removeEventHandlerTest(){
      when(jedisProxy.hget(anyString(), anyString())).thenReturn(toJson(eventHandler));
      redisEventHandlerDAO.removeEventHandler(name);
      verify(jedisProxy, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFoundTest(){
      when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
      redisEventHandlerDAO.removeEventHandler(name);
    }

    @Test(expected = TransientException.class)
    public void getEventHandlerTransientExceptionTest() {
      when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
      redisEventHandlerDAO.getEventHandler(name);
    }

    // remaining tests for the other methods and edge cases.

    private EventHandler createEventHandlerForTest(){
        // implementation here to return an EventHandler object
    }
}