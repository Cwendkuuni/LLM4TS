package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
public class RedisEventHandlerDAOTest {
    
    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    
    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private EventHandler eventHandler;
    
    @Test
    public void testAddEventHandler() {
        Mockito.doNothing().when(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        Mockito.doNothing().when(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
    
    @Test
    public void testRemoveEventHandler() {
        Mockito.doNothing().when(jedisProxy).hdel(Mockito.anyString(), Mockito.anyString());
        redisEventHandlerDAO.removeEventHandler("test");
        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        Map<String, String> all = new HashMap<>();
        all.put("test1", "value1");
        all.put("test2", "value2");
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(all);
        List<EventHandler> result = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        List<String> list = new ArrayList<>();
        list.add("test");
        Mockito.when(jedisProxy.smembers(Mockito.anyString())).thenReturn(list);
        List<EventHandler> result = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);
        assertEquals(1, result.size());
    }
}
