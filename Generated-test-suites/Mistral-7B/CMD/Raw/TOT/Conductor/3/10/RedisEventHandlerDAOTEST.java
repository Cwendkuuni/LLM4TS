import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.utils.RedisTestUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestSetup {

    @Bean
    public JedisProxy jedisProxy() {
        return mock(JedisProxy.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return mock(ObjectMapper.class);
    }

    @Bean
    public ConductorProperties conductorProperties() {
        return mock(ConductorProperties.class);
    }

    @Bean
    public RedisProperties properties() {
        return mock(RedisProperties.class);
    }

    @Bean
    @InjectMocks
    public RedisEventHandlerDAO redisEventHandlerDAO() {
        return new RedisEventHandlerDAO(
                Mockito.spy(jedisProxy()),
                Mockito.spy(objectMapper()),
                Mockito.spy(conductorProperties()),
                Mockito.spy(properties()));
    }
}

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;
    private ObjectMapper objectMapper;
    private JedisProxy jedisProxy;
    private ConductorProperties conductorProperties;
    private RedisProperties properties;
    private RedisTestUtils redisTestUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.objectMapper = Mockito.spy(objectMapper());
        this.jedisProxy = Mockito.spy(jedisProxy());
        this.conductorProperties = Mockito.spy(conductorProperties());
        this.properties = Mockito.spy(properties());
        this.redisTestUtils = new RedisTestUtils(jedisProxy);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals(eventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
        Mockito.verify(objectMapper).writeValueAsString(eventHandler);
        Mockito.verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), eventHandler.getName(), Mockito.anyString());
        Mockito.verify(redisEventHandlerDAO).index(eventHandler);
        Mockito.verify(redisEventHandlerDAO).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        String existingName = "existingHandler";
        EventHandler existingEventHandler = new EventHandler(existingName, "existingEvent");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        EventHandler newEventHandler = new EventHandler("existingHandler", "newEvent");
        redisEventHandlerDAO.addEventHandler(newEventHandler);
        Mockito.verify(jedisProxy, Mockito.atLeast(2)).hset(nsKey(EVENT_HANDLERS), existingName, Mockito.anyString());
        Mockito.verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), newEventHandler.getName(), Mockito.anyString());
        Mockito.verify(objectMapper, Mockito.atLeast(2)).writeValueAsString(existingEventHandler);
        Mockito.verify(objectMapper, Mockito.atLeast(1)).writeValueAsString(newEventHandler);
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(2)).index(existingEventHandler);
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(1)).index(newEventHandler);
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(2)).recordRedisDaoRequests("addEventHandler");
        Mockito.verify(redisEventHandlerDAO).removeEventHandler(existingName);
        Mockito.verify(redisEventHandlerDAO).addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        String newEvent = "newEvent";
        EventHandler updatedEventHandler = new EventHandler("testHandler", newEvent);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        assertEquals(updatedEventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), updatedEventHandler.getName()));
        assertEquals(newEvent, jedisProxy.hget(nsKey(EVENT_HANDLERS), updatedEventHandler.getName()).split(":")[1].split("\\}")[0]);
        Mockito.verify(objectMapper).writeValueAsString(updatedEventHandler);
        Mockito.verify(jedisProxy).hset(nsKey(EVENT_HANDLERS), updatedEventHandler.getName(), Mockito.anyString());
        Mockito.verify(redisEventHandlerDAO).removeIndex(eventHandler);
        Mockito.verify(redisEventHandlerDAO).index(updatedEventHandler);
        Mockito.verify(redisEventHandlerDAO).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        String nonExistentName = "nonExistentHandler";
        EventHandler updatedEventHandler = new EventHandler("nonExistentHandler", "newEvent");
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        Mockito.verify(jedisProxy, Mockito.atLeastOnce()).hget(nsKey(EVENT_HANDLERS), nonExistentName);
        Mockito.verifyNoMoreInteractions(jedisProxy);
        Mockito.verify(objectMapper, Mockito.atLeastOnce()).writeValueAsString(updatedEventHandler);
        Mockito.verifyNoMoreInteractions(objectMapper);
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeastOnce()).recordRedisDaoRequests("updateEventHandler");
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeastOnce()).removeEventHandler(nonExistentName);
        Mockito.verifyThrows(NotFoundException.class, () -> getEventHandler(nonExistentName));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler("testHandler", "testEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());
        assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
        Mockito.verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), eventHandler.getName());
        Mockito.verify(redisEventHandlerDAO).removeIndex(eventHandler);
        Mockito.verify(redisEventHandlerDAO).recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        String nonExistentName = "nonExistentHandler";
        redisEventHandlerDAO.removeEventHandler(nonExistentName);
        Mockito.verify(jedisProxy, Mockito.atLeastOnce()).hget(nsKey(EVENT_HANDLERS), nonExistentName);
        Mockito.verifyNoMoreInteractions(jedisProxy);
        Mockito.verifyNoMoreInteractions(redisEventHandlerDAO);
        Mockito.verifyThrows(NotFoundException.class, () -> getEventHandler(nonExistentName));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler("testHandler1", "testEvent1");
        EventHandler eventHandler2 = new EventHandler("testHandler2", "testEvent2");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        List<EventHandler> allEventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertEquals(2, allEventHandlers.size());
        assertEquals(eventHandler1, allEventHandlers.get(0));
        assertEquals(eventHandler2, allEventHandlers.get(1));
        Mockito.verify(jedisProxy).hgetAll(nsKey(EVENT_HANDLERS));
        Mockito.verify(objectMapper, Mockito.atLeast(2)).readValue(Mockito.anyString(), EventHandler.class);
        Mockito.verify(redisEventHandlerDAO).recordRedisDaoRequests("getAllEventHandlers");
    }

    @Test
    public void testGetEventHandlersForEventWithActiveHandlers() {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler("testHandler1", event);
        EventHandler eventHandler2 = new EventHandler("testHandler2", event);
        EventHandler eventHandler3 = new EventHandler("testHandler3", "otherEvent");
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);
        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        Mockito.verify(jedisProxy).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(2)).getEventHandler(eventHandler1.getName());
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(1)).getEventHandler(eventHandler2.getName());
        Mockito.verifyNoMoreInteractions(redisEventHandlerDAO);
    }

    @Test
    public void testGetEventHandlersForEventWithInactiveHandlers() {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler("testHandler1", event);
        EventHandler eventHandler2 = new EventHandler("testHandler2", event);
        EventHandler eventHandler3 = new EventHandler("testHandler3", event);
        EventHandler inactiveEventHandler = new EventHandler("testHandler4", event);
        inactiveEventHandler.setActive(false);
        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);
        redisEventHandlerDAO.addEventHandler(inactiveEventHandler);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(3, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
        assertEquals(eventHandler3, eventHandlers.get(2));
        Mockito.verify(jedisProxy).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(3)).getEventHandler(eventHandler1.getName());
        Mockito.verify(redisEventHandlerDAO, Mockito.atLeast(2)).getEventHandler(eventHandler2.getName());
        Mockito.verify(redisEventHandlerDAO).getEventHandler(inactiveEventHandler.getName());
    }

    @Test
    public void testGetEventHandlersForEventWithEmptyEventHandlers() {
        String event = "testEvent";
        redisEventHandlerDAO.addEventHandler(null);
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
        assertEquals(0, eventHandlers.size());
        Mockito.verify(jedisProxy).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, event));
        Mockito.verifyNoMoreInteractions(redisEventHandlerDAO);
    }

    private Map<String, String> nsKey(String... keys) {
        Map<String, String> namespace = new LinkedHashMap<>();
        for (String key : keys) {
            namespace.put(key, "");
        }
        return namespace;
    }

    private EventHandler getEventHandler(String name) {
        try {
            return redisEventHandlerDAO.getEventHandler(name);
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }
    }
}