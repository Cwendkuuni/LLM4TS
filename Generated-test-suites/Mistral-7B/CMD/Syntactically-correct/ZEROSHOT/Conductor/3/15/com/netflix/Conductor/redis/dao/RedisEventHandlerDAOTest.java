import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

import com.google.common.base.Preconditions;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private static final String EVENT_HANDLERS = "EVENT_HANDLERS";
    private static final String EVENT_HANDLERS_BY_EVENT = "EVENT_HANDLERS_BY_EVENT";

    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String EVENT = "event";

    private EventHandler eventHandler1;
    private EventHandler eventHandler2;
    private EventHandler eventHandler3;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        eventHandler1 = new EventHandler();
        eventHandler1.setName(NAME_1);
        eventHandler1.setEvent(EVENT);
        eventHandler1.setActive(true);

        eventHandler2 = new EventHandler();
        eventHandler2.setName(NAME_2);
        eventHandler2.setEvent(EVENT);
        eventHandler2.setActive(false);

        eventHandler3 = new EventHandler();
        eventHandler3.setName(NAME_3);
        eventHandler3.setEvent(EVENT);
        eventHandler3.setActive(true);

        when(objectMapper.readValue(anyString(), EquivalentType.EventHandler.class)).thenAnswer(
                invocation -> invocation.getArgument(0) != null ? eventHandler1 : null);

        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("eventHandler1", "{}"));
        when(jedisProxy.hget(anyString(), anyString())).thenAnswer(i -> i.getArguments()[1].equals(NAME_1) ? "{}" : null);
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1);
        when(jedisProxy.sadd(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of(NAME_1, NAME_2, NAME_3));
        when(jedisProxy.srem(anyString(), anyString())).thenReturn(1);
        when(jedisProxy.hdel(anyString(), anyString())).thenReturn(1);
    }

    @Test
    public void testAddEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertEquals(NAME_1, jedisProxy.hkeys(nsKey(EVENT_Handlers))[0]);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    @Test
    public void testUpdateEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.updateEventHandler(eventHandler2);
        assertEquals(1, jedisProxy.hlen(nsKey(EVENT_HANDLERS)));
        assertEquals(NAME_2, jedisProxy.hkeys(nsKey(EVENT_Handlers))[0]);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        redisEventHandlerDAO.updateEventHandler(eventHandler2);
    }

    @Test
    public void testRemoveEventHandler() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.removeEventHandler(NAME_1);
        assertEquals(0, jedisProxy.hlen(nsKey(EVENT_Handlers)));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        redisEventHandlerDAO.removeEventHandler(NAME_4);
    }

    @Test
    public void testGetAllEventHandlers() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);
        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);
        assertEquals(2, activeEventHandlers.size());
        assertEquals(eventHandler1, activeEventHandlers.get(0));
        assertEquals(eventHandler3, activeEventHandlers.get(1));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNonExistingEvent() {
        redisEventHandlerDAO.getEventHandlersForEvent("non-existing-event", false);
    }
}