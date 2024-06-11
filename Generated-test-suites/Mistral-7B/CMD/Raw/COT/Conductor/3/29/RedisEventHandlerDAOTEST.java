import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.jedis.JedisProxyMock;

@SuppressWarnings("unchecked")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    private JedisProxyMock jedisProxyMock;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private RedisTemplate<String, String> redisTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        jedisProxyMock = new JedisProxyMock();
        objectMapper = new ObjectMapper();
        conductorProperties = new ConductorProperties();
        redisProperties = new RedisProperties();
        redisTemplate = new RedisTemplate<>(new JedisConnectionFactory(jedisProxyMock));
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, objectMapper, conductorProperties, redisProperties);
        redisEventHandlerDAO.setRedisTemplate(redisTemplate);
    }

    // Test for addEventHandler method
    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("name1", "event1", "handler1");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        Assert.assertEquals(1, jedisProxyMock.hLen(redisEventHandlerDAO.namespaceKey(EVENT_HANDLERS)));
    }

    // Test for addEventHandler with existing name
    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler("name1", "event1", "handler1");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler eventHandler1 = new EventHandler("name1", "event2", "handler2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
    }

    // Test for updateEventHandler method
    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("name1", "event1", "handler1");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler eventHandler1 = new EventHandler("name1", "event2", "handler2");
        redisEventHandlerDAO.updateEventHandler(eventHandler1);
        Assert.assertEquals(1, jedisProxyMock.hLen(redisEventHandlerDAO.namespaceKey(EVENT_HANDLERS)));
    }

    // Test for updateEventHandler with non-existent name
    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler eventHandler = new EventHandler("name1", "event1", "handler1");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        EventHandler eventHandler1 = new EventHandler("name2", "event1", "handler2");
        redisEventHandlerDAO.updateEventHandler(eventHandler1);
    }

    // Test for removeEventHandler method
    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("name1", "event1", "handler1");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("name1");
        Assert.assertEquals(0, jedisProxyMock.hLen(redisEventHandlerDAO.namespaceKey(EVENT_HANDLERS)));
    }

    // Test for removeEventHandler with non-existent name
    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        redisEventHandlerDAO.removeEventHandler("name1");
    }

    // Test for getAllEventHandlers method
    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("name1", "event1", "handler1");
        EventHandler eventHandler2 = new EventHandler("name2", "event1", "handler2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        Assert.assertEquals(2, allEventHandlers.size());
    }

    // Test for getEventHandlersForEvent method with activeOnly=false
    @Test
    public void testGetEventHandlersForEventWithActiveOnlyFalse() {
        EventHandler eventHandler1 = new EventHandler("name1", "event1", "handler1", true);
        EventHandler eventHandler2 = new EventHandler("name2", "event1", "handler2", false);
        EventHandler eventHandler3 = new EventHandler("name3", "event1", "handler3", true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);
        Assert.assertEquals(3, eventHandlersForEvent.size());
    }

    // Test for getEventHandlersForEvent method with activeOnly=true
    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        EventHandler eventHandler1 = new EventHandler("name1", "event1", "handler1", true);
        EventHandler eventHandler2 = new EventHandler("name2", "event1", "handler2", false);
        EventHandler eventHandler3 = new EventHandler("name3", "event1", "handler3", true);
        EventHandler eventHandler4 = new EventHandler("name4", "event1", "handler4", true);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        redisEventHandlerDAO.addEventHandler(eventHandler4);
        List<EventHandler> eventHandlersForEvent = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);
        Assert.assertEquals(3, eventHandlersForEvent.size());
    }

    // Test for getEventHandlersForEvent method with non-existent event
    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNonExistentEvent() {
        EventHandler eventHandler1 = new EventHandler("name1", "event1", "handler1");
        EventHandler eventHandler2 = new EventHandler("name2", "event1", "handler2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.getEventHandlersForEvent("event2", false);
    }
}