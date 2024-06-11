package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.common.metadata.events.EventHandler;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void testAddEventHandler_ExistingHandler_ConflictException() {
        // create the DAO
        RedisEventHandlerDAO redisDao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
        
        // Mock the eventHandler already exist
        EventHandler handler = new EventHandler();
        handler.setName("MyHandler");
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("notEmpty");

        // Test
        assertThrows(ConflictException.class, () -> {
          redisDao.addEventHandler(handler);
        });
      }

      @Test
      public void testRemoveEventHandler_NotExistingHandler_NotFoundException() {
        // create the DAO
        RedisEventHandlerDAO redisDao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);

        // Mock the eventHandler does not exist
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(null);
        
        // Test
        assertThrows(NotFoundException.class, () -> {
          redisDao.removeEventHandler("MyHandler");
        });
      }
// Similarly write for other methods
}