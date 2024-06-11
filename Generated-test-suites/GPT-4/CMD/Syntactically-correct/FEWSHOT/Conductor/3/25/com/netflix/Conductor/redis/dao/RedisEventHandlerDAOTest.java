
package com.netflix.conductor.redis.dao;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO dao;
    private JedisProxy jedisProxy;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        dao = new RedisEventHandlerDAO(jedisProxy, mock(ObjectMapper.class), new ConductorProperties(), new RedisProperties());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("event-handler");

        dao.addEventHandler(eventHandler);

        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = mock(EventHandler.class);
        dao.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(any(), any(), any());
    }

    @Test
    public void testRemoveEventHandler() {
        dao.removeEventHandler("event-handler");
        verify(jedisProxy, times(1)).hdel(any(), any());
    }

    @Test
    public void testGetAllEventHandlers() {
        dao.getAllEventHandlers();
        verify(jedisProxy, times(1)).hgetAll(any());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        dao.getEventHandlersForEvent("event", true);
        verify(jedisProxy, times(1)).smembers(any());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithSameName(){
        EventHandler handler = new EventHandler();
        handler.setName("handler-name");

        when(dao.getEventHandler(any())).thenReturn(handler);
        dao.addEventHandler(handler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistingEventHandler(){
        EventHandler handler = new EventHandler();
        handler.setName("handler-name");

        when(dao.getEventHandler(any())).thenReturn(null);
        dao.updateEventHandler(handler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistingEventHandler(){
        when(dao.getEventHandler(any())).thenReturn(null);
        dao.removeEventHandler("handler-name");
    }
}
