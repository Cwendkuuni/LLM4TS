import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisProperties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private RedisProperties redisProperties;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Logger logger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("{}");
        when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);
        when(jedisProxy.sadd(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);
        when(jedisProxy.srem(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);
        when(jedisProxy.smembers(Mockito.anyString()))
                .thenReturn(Lists.newArrayList("handler1", "handler2"));
        when(objectMapper.readValue(Mockito.anyString(), EventHandler.class))
                .thenReturn(new EventHandler());
        when(logger.isDebugEnabled())
                .thenReturn(false);
        redisEventHandlerDAO = new RedisEventHandlerDAO(
                jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandlerSuccess() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals("handler1", redisEventHandlerDAO.getEventHandler("handler1").getName());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingNameThrowsConflictException() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertThrows(ConflictException.class, () -> redisEventHandlerDAO.addEventHandler(eventHandler));
    }

    @Test
    public void testUpdateEventHandlerSuccess() {
        EventHandler eventHandler = new EventHandler();
        EventHandler updatedEventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        assertEquals("handler1", redisEventHandlerDAO.getEventHandler("handler1").getName());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentNameThrowsNotFoundException() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.updateEventHandler(eventHandler));
    }

    @Test
    public void testRemoveEventHandlerSuccess() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("handler1");
        assertNull(redisEventHandlerDAO.getEventHandler("handler1"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentNameThrowsNotFoundException() {
        redisEventHandlerDAO.removeEventHandler("handler1");
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.removeEventHandler("handler1"));
    }

    @Test
    public void testGetAllEventHandlersReturnsAllEventHandlers() {
        EventHandler handler1 = new EventHandler();
        EventHandler handler2 = new EventHandler();
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);
        assertEquals(2, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEventReturnsEventHandlersForEvent() {
        EventHandler handler1 = new EventHandler().withEvent("event1");
        EventHandler handler2 = new EventHandler().withEvent("event1");
        EventHandler handler3 = new EventHandler().withEvent("event2");
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);
        redisEventHandlerDAO.addEventHandler(handler3);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventReturnsEmptyListForNonExistentEvent() {
        EventHandler handler1 = new EventHandler();
        EventHandler handler2 = new EventHandler();
        redisEventHandlerDAO.addEventHandler(handler1);
        redisEventHandlerDAO.addEventHandler(handler2);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false);
        assertEquals(0, eventHandlers.size());
    }
}