package com.netflix.conductor.redis.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisTestUtils;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private EventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        conductorProperties = Mockito.spy(new ConductorProperties());
        redisProperties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
        RedisTestUtils.initializeMockedJedis(jedisProxy);
    }

    @After
    public void teardown() {
        RedisTestUtils.clearMockedJedis(jedisProxy);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");
        Preconditions.checkNotNull(eventHandler.getName());
        Preconditions.checkArgument(eventHandler.getName() != null, "Missing Name");

        when(objectMapper.writeValueAsString(eventHandler)).thenReturn("{}");
        when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler", "{}");
        Mockito.verify(objectMapper).writeValueAsString(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExists() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");
        Preconditions.checkNotNull(eventHandler.getName());
        Preconditions.checkArgument(eventHandler.getName() != null, "Missing Name");

        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testHandler");
        existingEventHandler.setEvent("testEvent");
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler")).thenReturn("{}");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler", "{}");
        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler", "{}");
        Mockito.verify(objectMapper, Mockito.times(1)).writeValueAsString(eventHandler);

        Mockito.verifyThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testHandler");
        existingEventHandler.setEvent("testEvent");
        Preconditions.checkNotNull(existingEventHandler.getName());
        Preconditions.checkArgument(existingEventHandler.getName() != null, "Missing Name");

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testHandler");
        newEventHandler.setEvent("testEventUpdated");
        Preconditions.checkNotNull(newEventHandler.getName());
        Preconditions.checkArgument(newEventHandler.getName() != null, "Missing Name");

        when(objectMapper.writeValueAsString(newEventHandler)).thenReturn("{}");

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler")).thenReturn("{}");
        when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler");
        Mockito.verify(jedisProxy).hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler", "{}");
        Mockito.verify(objectMapper).writeValueAsString(newEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithDoesNotExist() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testHandlerUpdated");
        newEventHandler.setEvent("testEventUpdated");
        Preconditions.checkNotNull(newEventHandler.getName());
        Preconditions.checkArgument(newEventHandler.getName() != null, "Missing Name");

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandlerUpdated")).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandlerUpdated");
        Mockito.verifyNoInteractions(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoInteractions(objectMapper);

        Mockito.verifyThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(newEventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");
        Preconditions.checkNotNull(eventHandler.getName());
        Preconditions.checkArgument(eventHandler.getName() != null, "Missing Name");

        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler")).thenReturn("{}");
        when(jedisProxy.hdel(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler")).thenReturn(1L);

        redisEventHandlerDAO.removeEventHandler("testHandler");

        Mockito.verify(jedisProxy).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler");
        Mockito.verify(jedisProxy).hdel(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithDoesNotExist() {
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler")).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testHandler");

        Mockito.verify(jedisProxy, Mockito.times(1)).hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler");
        Mockito.verifyNoInteractions(jedisProxy).hdel(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), "testHandler");

        Mockito.verifyThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("testHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testHandler1");
        eventHandler1.setEvent("testEvent1");
        Preconditions.checkNotNull(eventHandler1.getName());
        Preconditions.checkArgument(eventHandler1.getName() != null, "Missing Name");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testHandler2");
        eventHandler2.setEvent("testEvent2");
        Preconditions.checkNotNull(eventHandler2.getName());
        Preconditions.checkArgument(eventHandler2.getName() != null, "Missing Name");

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(Arrays.asMap(
                "testHandler1", "{}",
                "testHandler2", "{}"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        Mockito.verify(jedisProxy).hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
        assertThat(eventHandlers, is(notNullValue()));
        assertThat(eventHandlers.size(), is(2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setActive(true);
        Preconditions.checkNotNull(eventHandler1.getName());
        Preconditions.checkArgument(eventHandler1.getName() != null, "Missing Name");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setActive(false);
        Preconditions.checkNotNull(eventHandler2.getName());
        Preconditions.checkArgument(eventHandler2.getName() != null, "Missing Name");

        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Arrays.asList("testHandler1", "testHandler2"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        Mockito.verify(jedisProxy).smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"));
        assertThat(eventHandlers, is(notNullValue()));
        assertThat(eventHandlers.size(), is(2));
        for (EventHandler handler : eventHandlers) {
            assertThat(handler, is(notNullValue()));
        }
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("testHandler1");
        eventHandler1.setEvent("testEvent");
        eventHandler1.setActive(true);
        Preconditions.checkNotNull(eventHandler1.getName());
        Preconditions.checkArgument(eventHandler1.getName() != null, "Missing Name");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("testHandler2");
        eventHandler2.setEvent("testEvent");
        eventHandler2.setActive(false);
        Preconditions.checkNotNull(eventHandler2.getName());
        Preconditions.checkArgument(eventHandler2.getName() != null, "Missing Name");

        when(jedisProxy.smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"))).thenReturn(Arrays.asList("testHandler1", "testHandler2"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        Mockito.verify(jedisProxy).smembers(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT, "testEvent"));
        assertThat(eventHandlers, is(notNullValue()));
        assertThat(eventHandlers.size(), is(1));
        for (EventHandler handler : eventHandlers) {
            assertThat(handler, is(notNullValue()));
            assertThat(handler.isActive(), is(true));
        }
    }
}