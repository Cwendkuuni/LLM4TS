import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Before
    public void setup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(RedisEventHandlerDAO.class);
        context.refresh();

        redisEventHandlerDAO = context.getBean(RedisEventHandlerDAO.class);
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        conductorProperties = Mockito.mock(ConductorProperties.class);
        redisProperties = Mockito.mock(RedisProperties.class);
        redisEventHandlerDAO.setJedisProxy(jedisProxy);
        redisEventHandlerDAO.setObjectMapper(objectMapper);
        redisEventHandlerDAO.setConductorProperties(conductorProperties);
        redisEventHandlerDAO.setRedisProperties(redisProperties);

        // Initialize mocks
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.anyString())).thenReturn((String) null);
        Mockito.when(jedisProxy.hgetAll(Mockito.anyString())).thenReturn(Map.of());
        Mockito.when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        Mockito.when(jedisProxy.sadd(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        Mockito.when(jedisProxy.srem(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.eq(EventHandler.class))).then(invocation -> {
            String json = invocation.getArguments()[0];
            return new EventHandler(); // Replace this with actual deserialization logic
        });
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(eventHandler.getName()), Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler existingEventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(newEventHandler.getName()), Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(newEventHandler.getName()));
        Mockito.verify(jedisProxy, Mockito.times(1)).throwException(Mockito.eq(new ConflictException("EventHandler with name %s already exists!", newEventHandler.getName())));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler existingEventHandler = new EventHandler();
        EventHandler newEventHandler = new EventHandler();

        redisEventHandlerDAO.addEventHandler(existingEventHandler);
        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hset(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(newEventHandler.getName()), Mockito.anyString());
        Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingName() {
        EventHandler newEventHandler = new EventHandler();

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        Mockito.verify(jedisProxy, Mockito.times(1)).hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(newEventHandler.getName()));
        Mockito.verify(jedisProxy, Mockito.times(1)).throwException(Mockito.eq(new NotFoundException("EventHandler with name %s not found!", newEventHandler.getName())));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        Mockito.verify(jedisProxy, Mockito.times(1)).hdel(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq(eventHandler.getName()));
        Mockito.verify(jedisProxy, Mockito.times(1)).recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingName() {
        redisEventHandlerDAO.removeEventHandler("nonExistentName");

        Mockito.verify(jedisProxy, Mockito.times(1)).hget(Mockito.eq(nsKey(EVENT_HANDLERS)), Mockito.eq("nonExistentName"));
        Mockito.verify(jedisProxy, Mockito.times(1)).throwException(Mockito.eq(new NotFoundException("EventHandler with name %s not found!", "nonExistentName")));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        EventHandler eventHandler2 = new EventHandler();

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        Mockito.verify(jedisProxy, Mockito.times(1)).hgetAll(Mockito.eq(nsKey(EVENT_HANDLERS)));
        Assert.assertEquals(Lists.newArrayList(eventHandler1, eventHandler2), eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler().withEvent(event);
        EventHandler eventHandler2 = new EventHandler().withEvent(event);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        Mockito.verify(jedisProxy, Mockito.times(1)).smembers(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        Assert.assertEquals(Lists.newArrayList(eventHandler1, eventHandler2), eventHandlers);
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly() {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler().withActive(true).withEvent(event);
        EventHandler eventHandler2 = new EventHandler().withActive(false).withEvent(event);

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        Mockito.verify(jedisProxy, Mockito.times(1)).smembers(Mockito.eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        Assert.assertEquals(Lists.newArrayList(eventHandler1), eventHandlers);
    }
}