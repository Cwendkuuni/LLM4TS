
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO dao;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        dao = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test
    public void addEventHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("dummyName");

        dao.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(anyString(), eq("dummyName"), anyString());
    }

    @Test(expected = ConflictException.class)
    public void addEventHandlerConflictTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("dummyName");

        when(jedisProxy.hget(anyString(), eq("dummyName"))).thenReturn("existing dummy data");

        dao.addEventHandler(eventHandler);
    }

    @Test
    public void removeEventHandlerTest() {
        when(jedisProxy.hget(anyString(), anyString())).thenReturn("dummyData");

        dao.removeEventHandler("dummyName");

        verify(jedisProxy, times(1)).hdel(anyString(), eq("dummyName"));
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerNotFoundTest() {
        dao.removeEventHandler("dummyName");
    }

    @Test
    public void getEventHandlerForEventTest() {
        when(jedisProxy.smembers(anyString())).thenReturn(Mockito.mock(Set.class));
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("dummyEventHandlerName");
        eventHandler.setEvent("dummyEvent");
        eventHandler.setActive(true);
        String jsonDummyData = "dummyData";
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(jsonDummyData);

        List<EventHandler> eventHandlers = dao.getEventHandlersForEvent("dummyEvent", true);

        assertEquals(1, eventHandlers.size());
    }

    @Test(expected = NotFoundException.class)
    public void getEventHandlerForEventNotFoundTest() {
        when(jedisProxy.smembers(anyString())).thenReturn(Mockito.mock(Set.class));
        dao.getEventHandlersForEvent("dummyEvent", true);
    }
}
