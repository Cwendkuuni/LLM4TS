import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.redis.RedisTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Configuration
@Import(RedisEventHandlerDAO.class)
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

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
    public RedisProperties redisProperties() {
        return mock(RedisProperties.class);
    }

    @Bean
    public BaseDynoDAO baseDynoDAO() {
        return mock(BaseDynoDAO.class);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandlerSuccess() throws Exception {
        String name = "testEventHandler";
        EventHandler eventHandler = new EventHandler("testEvent", "testEndpoint");

        when(jedisProxy.hset(any(), any(), any()))
                .thenReturn(true);
        when(baseDynoDAO().index(eventHandler)).thenReturn(1);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eq(name), anyString());
        verify(baseDynoDAO()).index(eventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerNameAlreadyExists() throws Exception {
        String name = "testEventHandler";
        EventHandler existingEventHandler = new EventHandler("testEvent", "testEndpoint");
        EventHandler newEventHandler = new EventHandler("testEvent", "testEndpoint2");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name)))
                .thenReturn(toJson(existingEventHandler).getBytes());

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        verify(jedisProxy, never()).hset(eq(nsKey(EVENT_HANDLERS)), eq(name), anyString());
        verify(baseDynoDAO(), never()).index(newEventHandler);
    }

    @Test
    public void testUpdateEventHandlerSuccess() throws Exception {
        String name = "testEventHandler";
        String newName = "newTestEventHandler";
        EventHandler oldEventHandler = new EventHandler("testEvent", "testEndpoint");
        EventHandler newEventHandler = new EventHandler("testEvent", "testEndpoint2");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name)))
                .thenReturn(toJson(oldEventHandler).getBytes());
        when(baseDynoDAO().removeIndex(oldEventHandler)).thenReturn(true);
        when(jedisProxy.hset(any(), any(), any()))
                .thenReturn(true);
        when(baseDynoDAO().index(newEventHandler)).thenReturn(2);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy).hset(eq(nsKey(EVENT_HANDLERS)), eq(newName), anyString());
        verify(baseDynoDAO()).removeIndex(oldEventHandler);
        verify(baseDynoDAO()).index(newEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNameNotFound() throws Exception {
        String name = "testEventHandler";
        EventHandler newEventHandler = new EventHandler("testEvent", "testEndpoint2");

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name)))
                .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy, never()).hset(any(), any(), anyString());
        verify(baseDynoDAO(), never()).removeIndex(any());
        verify(baseDynoDAO(), never()).index(newEventHandler);
    }

    @Test
    public void testRemoveEventHandlerSuccess() throws Exception {
        String name = "testEventHandler";

        when(jedisProxy.hdel(eq(nsKey(EVENT_HANDLERS)), eq(name)))
                .thenReturn(1);
        when(baseDynoDAO().removeIndex(any()))
                .thenReturn(true);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy).hdel(eq(nsKey(EVENT_HANDLERS)), eq(name));
        verify(baseDynoDAO()).removeIndex(any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNameNotFound() throws Exception {
        String name = "testEventHandler";

        when(jedisProxy.hget(eq(nsKey(EVENT_HANDLERS)), eq(name)))
                .thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(name);

        verify(jedisProxy, never()).hdel(any(), any());
        verify(baseDynoDAO(), never()).removeIndex(any());
    }

    @Test
    public void testGetAllEventHandlersSuccess() throws Exception {
        EventHandler eventHandler1 = new EventHandler("testEvent1", "testEndpoint1");
        EventHandler eventHandler2 = new EventHandler("testEvent1", "testEndpoint2");

        Map<String, String> all = Maps.newHashMap();
        all.put("testEventHandler1", toJson(eventHandler1).getBytes());
        all.put("testEventHandler2", toJson(eventHandler2).getBytes());

        when(jedisProxy.hgetAll(eq(nsKey(EVENT_HANDLERS)))).thenReturn(all);

        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy).hgetAll(eq(nsKey(EVENT_HANDLERS)));
        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventSuccess() throws Exception {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler("testEvent", "testEndpoint1");
        EventHandler eventHandler2 = new EventHandler("testEvent", "testEndpoint2");
        EventHandler eventHandler3 = new EventHandler("testEvent2", "testEndpoint3");

        Set<String> names = Lists.newLinkedHashSet();
        names.add("testEventHandler1");
        names.add("testEventHandler2");

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names.toArray(new String[0]));

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertEquals(2, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
        assertEquals(eventHandler2, handlers.get(1));
    }

    @Test
    public void testGetEventHandlersForEventEmpty() throws Exception {
        String event = "testEvent";

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(new String[0]);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertEquals(0, handlers.size());
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlySuccess() throws Exception {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler("testEvent", "testEndpoint1");
        EventHandler eventHandler2 = new EventHandler("testEvent", "testEndpoint2");

        Set<String> names = Lists.newLinkedHashSet();
        names.add("testEventHandler1");

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(names.toArray(new String[0]));
        when(redisEventHandlerDAO.getEventHandler("testEventHandler1")).thenReturn(eventHandler1);
        when(eventHandler1.isActive()).thenReturn(true);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertEquals(1, handlers.size());
        assertEquals(eventHandler1, handlers.get(0));
    }

    @Test
    public void testGetEventHandlersForEventActiveOnlyEmpty() throws Exception {
        String event = "testEvent";

        when(jedisProxy.smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)))).thenReturn(new String[0]);

        List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        verify(jedisProxy).smembers(eq(nsKey(EVENT_HANDLERS_BY_EVENT, event)));
        assertEquals(0, handlers.size());
    }

    private byte[] toJson(EventHandler eventHandler) {
        return RedisTestUtils.toJson(eventHandler).getBytes();
    }
}