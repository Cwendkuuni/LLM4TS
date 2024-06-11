import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.junit.RedisTestUtils;

import static com.netflix.conductor.common.metadata.events.EventHandlerTestData.createEventHandler;
import static com.netflix.conductor.common.metadata.events.EventHandlerTestData.createEventHandlerList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private RedisProperties redisProperties;
    private ConductorProperties conductorProperties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        jedisProxy = mock(JedisProxy.class);
        objectMapper = mock(ObjectMapper.class);
        redisProperties = mock(RedisProperties.class);
        conductorProperties = mock(ConductorProperties.class);

        when(jedisProxy.hgetAll(anyString())).then(returnValueCaptor -> returnValueCaptor.getValue());
        when(jedisProxy.hget(anyString(), anyString())).then(returnValueCaptor -> returnValueCaptor.getValue());
        when(jedisProxy.sadd(anyString(), anyString())).then(invocation -> invocation.getArgument(1));
        when(jedisProxy.srem(anyString(), anyString())).then(invocation -> invocation.getArgument(1));
        when(jedisProxy.smembers(anyString())).then(returnValueCaptor -> returnValueCaptor.getValue());

        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy,
                objectMapper,
                conductorProperties,
                redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        Map<String, String> all = Maps.newHashMap();
        all.put(eventHandler.getName(), toJson(eventHandler));
        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(all);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertThat(retrievedEventHandler, is(notNullValue()));
        assertThat(retrievedEventHandler, is(equalTo(eventHandler)));

        verifyAdditions(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = createEventHandler();
        String name = eventHandler.getName();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler existingEventHandler = createEventHandler();
        existingEventHandler.setName(name);

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(Maps.newHashMap());
        when(jedisProxy.hget(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name)).thenReturn(toJson(eventHandler));

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler(eventHandler.getName(), "updatedEvent");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        Map<String, String> all = Maps.newHashMap();
        all.put(updatedEventHandler.getName(), toJson(updatedEventHandler));
        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(all);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(updatedEventHandler.getName());
        assertThat(retrievedEventHandler, is(notNullValue()));
        assertThat(retrievedEventHandler, is(equalTo(updatedEventHandler)));

        verifyUpdates(updatedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler updatedEventHandler = new EventHandler("nonExistentName", "updatedEvent");

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(Maps.newHashMap());

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        Map<String, String> all = Maps.newHashMap();
        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(all);

        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler(eventHandler.getName());
        assertThat(retrievedEventHandler, is(nullValue()));

        verifyRemovals(eventHandler.getName());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        String name = "nonExistentName";
        redisEventHandlerDAO.removeEventHandler(name);

        Map<String, String> all = Maps.newHashMap();
        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(all);

        redisEventHandlerDAO.getEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> allEventHandlers = createEventHandlerList();

        Map<String, String> all = Maps.newHashMap();
        for (EventHandler eventHandler : allEventHandlers) {
            all.put(eventHandler.getName(), toJson(eventHandler));
        }

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(all);

        List<EventHandler> retrievedEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertThat(retrievedEventHandlers, is(notNullValue()));
        assertThat(retrievedEventHandlers, is(equalTo(allEventHandlers)));

        verifyGetAll();
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        EventHandler firstEventHandler = createEventHandlerWithEvent(event);
        EventHandler secondEventHandler = createEventHandlerWithEvent(event);
        List<EventHandler> eventHandlers = Lists.newArrayList(firstEventHandler, secondEventHandler);

        Map<String, String> eventHandlersByEvent = Maps.newHashMap();
        Map<String, Set<String>> eventHandlersByNameEvent = Maps.newHashMap();
        eventHandlersByNameEvent.put(event, createStringSet(firstEventHandler.getName(), secondEventHandler.getName()));
        eventHandlersByEvent.put(EVENT_HANDLERS_BY_EVENT + ":" + event, toJson(eventHandlersByNameEvent));

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT) + ":" + event)).thenReturn(eventHandlersByEvent);

        List<EventHandler> retrievedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertThat(retrievedEventHandlers, is(notNullValue()));
        assertThat(retrievedEventHandlers, is(equalTo(eventHandlers)));

        verifyGetEventHandlersForEvent(event, false);
    }

    @Test
    public void testGetEventHandlersForEventActiveOnly() {
        String event = "testEvent";
        EventHandler firstEventHandler = createEventHandlerWithEvent(event);
        EventHandler secondEventHandler = createEventHandlerWithEvent(event);
        firstEventHandler.setActive(false);
        List<EventHandler> eventHandlers = Lists.newArrayList(firstEventHandler, secondEventHandler);

        Map<String, String> eventHandlersByEvent = Maps.newHashMap();
        Map<String, Set<String>> eventHandlersByNameEvent = Maps.newHashMap();
        eventHandlersByNameEvent.put(event, createStringSet(firstEventHandler.getName(), secondEventHandler.getName()));
        eventHandlersByEvent.put(EVENT_HANDLERS_BY_EVENT + ":" + event, toJson(eventHandlersByNameEvent));

        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT) + ":" + event)).thenReturn(eventHandlersByEvent);

        List<EventHandler> retrievedEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertThat(retrievedEventHandlers, is(notNullValue()));
        assertThat(retrievedEventHandlers, hasSize(1));
        assertThat(retrievedEventHandlers.get(0), is(equalTo(secondEventHandler)));

        verifyGetEventHandlersForEvent(event, true);
    }

    private void verifyAdditions(EventHandler eventHandler) {
        OngoingStubbing<Set<String>> setAdd = jedisProxy.sadd(anyString(), eventHandler.getName());
        setAdd.thenReturn(eventHandler.getName());

        jedisProxy.hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), eventHandler.getName(), toJson(eventHandler));
    }

    private void verifyUpdates(EventHandler updatedEventHandler) {
        jedisProxy.hdel(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), updatedEventHandler.getName());

        Map<String, String> all = Maps.newHashMap();
        all.put(updatedEventHandler.getName(), toJson(updatedEventHandler));
        when(jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS))).thenReturn(all);

        jedisProxy.hset(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), updatedEventHandler.getName(), toJson(updatedEventHandler));
    }

    private void verifyRemovals(String name) {
        jedisProxy.hdel(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS), name);

        jedisProxy.srem(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT) + ":" + event, name);
    }

    private void verifyGetAll() {
        jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS));
    }

    private void verifyGetEventHandlersForEvent(String event, boolean activeOnly) {
        jedisProxy.hgetAll(nsKey(RedisEventHandlerDAO.EVENT_HANDLERS_BY_EVENT) + ":" + event);
    }
}