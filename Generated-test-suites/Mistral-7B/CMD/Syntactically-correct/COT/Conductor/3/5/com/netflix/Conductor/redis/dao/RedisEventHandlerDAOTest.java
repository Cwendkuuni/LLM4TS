import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.config.Properties;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.test.RedisTestUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private static final String NAMESPACE = "testNamespace";

    private JedisProxy jedisProxy;
    private RedisProperties redisProperties;
    private Properties conductorProperties;
    private EventHandlerDAO redisEventHandlerDAO;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        JedisProxy jedisMock = Mockito.mock(JedisProxy.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        conductorProperties = Mockito.mock(Properties.class);
        objectMapper = Mockito.mock(ObjectMapper.class);

        RedisTestUtil.setupJedisProxy(jedisMock);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(RedisEventHandlerDAO.class);
        context.register(JedisProxy.class);
        context.register(ObjectMapper.class);
        context.register(ConductorProperties.class);
        context.register(RedisProperties.class);
        context.refresh();

        redisEventHandlerDAO = context.getBean(EventHandlerDAO.class);
        this.jedisProxy = Mockito.spy(jedisMock);
        Mockito.when(jedisProxy.nsKey(ArgumentMatchers.anyString())).then(invocation -> NAMESPACE + "_" + invocation.getArgument(0));
        this.redisProperties = Mockito.argumentCaptor(RedisProperties.class).capture(Mockito.mockCalls());
        this.conductorProperties = Mockito.argumentCaptor(Properties.class).capture(Mockito.mockCalls());
        this.objectMapper = Mockito.argumentCaptor(ObjectMapper.class).capture(Mockito.mockCalls());
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"), ArgumentMatchers.eq("testEventHandler"),
                ArgumentMatchers.anyString());
        Mockito.verify(jedisProxy).sadd(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_testEvent"),
                ArgumentMatchers.eq("testEventHandler"));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.never()).hset(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"),
                ArgumentMatchers.eq("testEventHandler"), ArgumentMatchers.anyString());
        Mockito.verify(jedisProxy, Mockito.never()).sadd(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_testEvent"),
                ArgumentMatchers.eq("testEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName("testEventHandler");
        existingEventHandler.setEvent("testEvent");

        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("updatedEvent");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy).hset(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"), ArgumentMatchers.eq("testEventHandler"),
                ArgumentMatchers.anyString());
        Mockito.verify(jedisProxy, Mockito.never()).sadd(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_testEvent"),
                ArgumentMatchers.eq("testEventHandler"));
        Mockito.verify(jedisProxy).srem(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_testEvent"),
                ArgumentMatchers.eq("testEventHandler"));
        Mockito.verify(jedisProxy).sadd(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_updatedEvent"),
                ArgumentMatchers.eq("testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler newEventHandler = new EventHandler();
        newEventHandler.setName("testEventHandler");
        newEventHandler.setEvent("updatedEvent");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.never()).hset(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"),
                ArgumentMatchers.eq("testEventHandler"), ArgumentMatchers.anyString());
        Mockito.verify(jedisProxy, Mockito.never()).srem(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_testEvent"),
                ArgumentMatchers.eq("testEventHandler"));
        Mockito.verify(jedisProxy, Mockito.never()).sadd(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_updatedEvent"),
                ArgumentMatchers.eq("testEventHandler"));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy).hdel(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"), ArgumentMatchers.eq("testEventHandler"));
        Mockito.verify(jedisProxy).srem(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_"),
                ArgumentMatchers.eq("testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistentName() {
        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy, Mockito.never()).hdel(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"),
                ArgumentMatchers.eq("testEventHandler"));
        Mockito.verify(jedisProxy, Mockito.never()).srem(ArgumentMatchings.startsWith(NAMESPACE + "_EVENT_HANDLERS_BY_EVENT_"),
                ArgumentMatchers.eq("testEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals(eventHandler1, eventHandlers.get(0));
        assertEquals(eventHandler2, eventHandlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event1");
        eventHandler2.setActive(false);

        EventHandler eventHandler3 = new EventHandler();
        eventHandler3.setName("eventHandler3");
        eventHandler3.setEvent("event2");
        eventHandler3.setActive(true);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);
        redisEventHandlerDAO.addEventHandler(eventHandler3);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", false);

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent("event1", true);

        assertEquals(1, activeEventHandlers.size());
        assertTrue(activeEventHandlers.contains(eventHandler1));
    }

    @Test
    public void testGetEventHandlerByNonExistentName() {
        Mockito.when(jedisProxy.hget(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"), ArgumentMatchers.anyString())).thenReturn(null);

        try {
            redisEventHandlerDAO.getEventHandler("nonExistentName");
            fail();
        } catch (NotFoundException e) {
            // expected
        }
    }

    @Test
    public void testGetEventHandlerForExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        String json = "{\"name\":\"testEventHandler\",\"event\":\"testEvent\",\"active\":true}";

        Mockito.when(jedisProxy.hget(ArgumentMatchers.eq(NAMESPACE + "_EVENT_HANDLERS"), ArgumentMatchers.eq("testEventHandler")))
                .thenReturn(json);

        EventHandler result = redisEventHandlerDAO.getEventHandler("testEventHandler");

        assertEquals(eventHandler, result);
    }

    @Test
    public void testRecordRedisDaoRequests() {
        String requestName = "addEventHandler";
        redisEventHandlerDAO.recordRedisDaoRequests(requestName);

        Mockito.verify(redisProperties).getRedisMetricsPrefix();
        Mockito.verify(redisProperties).getRedisMetricsNamespace();
        Mockito.verify(conductorProperties).getApplicationName();
        Mockito.verify(objectMapper).writeValueAsString(requestName);
        Mockito.verify(jedisProxy).rpush(ArgumentMatchers.eq(NAMESPACE + "_REDIS_DAO_REQUESTS"), ArgumentMatchers.eq(requestName));
    }
}