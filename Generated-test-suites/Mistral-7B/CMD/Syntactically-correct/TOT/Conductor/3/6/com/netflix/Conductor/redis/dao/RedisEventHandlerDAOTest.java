import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.junit.EmbeddedRedisServer;
import com.netflix.conductor.redis.jedis.JedisProxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RedisEventHandlerDAOTest {

    private AnnotationConfigApplicationContext context;
    private RedisProperties redisProperties;
    private JedisProxy jedisProxy;
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisProperties = new RedisProperties();
        context = new AnnotationConfigApplicationContext();
        context.register(RedisProperties.class);
        context.refresh();
        jedisProxy = context.getBean(JedisProxy.class);
        redisEventHandlerDAO = context.getBean(RedisEventHandlerDAO.class);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler() {{
            setName("testEventHandler");
            setEvent("testEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(eventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
        assertEquals(eventHandler.toString(), jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler() {{
            setName("existingEventHandler");
            setEvent("testEvent");
            setActive(true);
        }};
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler() {{
            setName("existingEventHandler");
            setEvent("testEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNonExistentName() {
        assertNull(redisEventHandlerDAO.getEventHandler("nonExistentEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler() {{
            setName("testEventHandler");
            setEvent("testEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler() {{
            setName("testEventHandler");
            setEvent("updatedEvent");
            setActive(false);
        }};

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertEquals(updatedEventHandler.toString(), jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler() {{
            setName("testEventHandler");
            setEvent("testEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
    }

    @EmbeddedRedisServer
    private EmbeddedRedisServer redisServer = new EmbeddedRedisServer();

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler() {{
            setName("eventHandler1");
            setEvent("testEvent");
            setActive(true);
        }};
        EventHandler eventHandler2 = new EventHandler() {{
            setName("eventHandler2");
            setEvent("testEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler() {{
            setName("eventHandler1");
            setEvent("testEvent");
            setActive(true);
        }};
        EventHandler eventHandler2 = new EventHandler() {{
            setName("eventHandler2");
            setEvent("testEvent");
            setActive(false);
        }};
        EventHandler eventHandler3 = new EventHandler() {{
            setName("eventHandler3");
            setEvent("anotherEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false);

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() {
        EventHandler eventHandler1 = new EventHandler() {{
            setName("eventHandler1");
            setEvent("testEvent");
            setActive(true);
        }};
        EventHandler eventHandler2 = new EventHandler() {{
            setName("eventHandler2");
            setEvent("testEvent");
            setActive(false);
        }};
        EventHandler eventHandler3 = new EventHandler() {{
            setName("eventHandler3");
            setEvent("anotherEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("testEvent", true);

        assertEquals(1, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNonExistentEvent() {
        assertThrows(NotFoundException.class, () -> redisEventHandlerDAO.getEventHandlersForEvent("nonExistentEvent", false));
    }

    @Test(expected = TransientException.class)
    public void testAddEventHandlerWithConnectionError() {
        Mockito.doThrow(new RuntimeException("Test Connection Error")).when(jedisProxy).hset(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        EventHandler eventHandler = new EventHandler() {{
            setName("testEventHandler");
            setEvent("testEvent");
            setActive(true);
        }};

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }
}