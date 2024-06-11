import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.testutils.TestUtils;

import static org.mockito.Mockito.*;

@Configuration
@Import(RedisEventHandlerDAO.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;

    @Bean
    public JedisProxy jedisProxy() {
        return Mockito.mock(JedisProxy.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Mockito.mock(ObjectMapper.class);
    }

    @Bean
    public ConductorProperties conductorProperties() {
        return Mockito.mock(ConductorProperties.class);
    }

    @Bean
    public RedisProperties redisProperties() {
        return Mockito.mock(RedisProperties.class);
    }

    @Before
    public void setup() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = TestUtils.getSampleEventHandler("testAddEventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        verify(jedisProxy).hset(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq(eventHandler.getName()), ArgumentMatchers.anyString());
        verify(objectMapper).writeValueAsString(eventHandler);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent()), eventHandler.getName());
        verify(redisEventHandlerDAO).index(eventHandler);
        verify(redisEventHandlerDAO).recordRedisDaoRequests("addEventHandler");
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithConflict() throws Exception {
        String eventName = "testEvent";
        EventHandler existingEventHandler = TestUtils.getSampleEventHandler(eventName);
        EventHandler newEventHandler = TestUtils.getSampleEventHandler("testAddEventHandlerWithConflict");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), ArgumentMatchers.eq(existingEventHandler.getName()))).thenReturn(TestUtils.getJsonString(existingEventHandler));

        redisEventHandlerDAO.addEventHandler(newEventHandler);

        verify(jedisProxy, never()).hset(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq(newEventHandler.getName()), ArgumentMatchers.anyString());
        verify(objectMapper, never()).writeValueAsString(newEventHandler);
        verify(jedisProxy, never()).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventName), newEventHandler.getName());
        verify(redisEventHandlerDAO, never()).index(newEventHandler);
        verify(redisEventHandlerDAO, never()).recordRedisDaoRequests("addEventHandler");
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler existingEventHandler = TestUtils.getSampleEventHandler("testUpdateEventHandler");
        EventHandler newEventHandler = TestUtils.getSampleEventHandler("testUpdateEventHandler");

        redisEventHandlerDAO.updateEventHandler(newEventHandler);

        verify(jedisProxy).hset(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq(newEventHandler.getName()), ArgumentMatchers.anyString());
        verify(objectMapper).writeValueAsString(newEventHandler);
        verify(jedisProxy).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, newEventHandler.getEvent()), newEventHandler.getName());
        verify(redisEventHandlerDAO).removeIndex(existingEventHandler);
        verify(redisEventHandlerDAO).index(newEventHandler);
        verify(redisEventHandlerDAO).recordRedisDaoRequests("updateEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNotFound() throws Exception {
        String eventName = "testEvent";
        EventHandler eventHandler = TestUtils.getSampleEventHandler("testUpdateEventHandlerWithNotFound");

        when(jedisProxy.hget(nsKey(EVENT_HANDLERS), ArgumentMatchers.eq(eventHandler.getName()))).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        verify(jedisProxy, never()).hset(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS)), ArgumentMatchers.eq(eventHandler.getName()), ArgumentMatchers.anyString());
        verify(objectMapper, never()).writeValueAsString(eventHandler);
        verify(jedisProxy, never()).sadd(nsKey(EVENT_HANDLERS_BY_EVENT, eventName), eventHandler.getName());
        verify(redisEventHandlerDAO, never()).removeIndex(any());
        verify(redisEventHandlerDAO, never()).index(eventHandler);
        verify(redisEventHandlerDAO, never()).recordRedisDaoRequests("updateEventHandler");
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        String eventName = "testRemoveEventHandler";
        EventHandler eventHandler = TestUtils.getSampleEventHandler(eventName);

        redisEventHandlerDAO.removeEventHandler(eventName);

        verify(jedisProxy).hdel(nsKey(EVENT_HANDLERS), eventName);
        verify(redisEventHandlerDAO).removeIndex(eventHandler);
        verify(redisEventHandlerDAO).recordRedisDaoRequests("removeEventHandler");
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNotFound() throws Exception {
        String eventName = "testRemoveEventHandlerWithNotFound";

        redisEventHandlerDAO.removeEventHandler(eventName);

        verify(jedisProxy, never()).hdel(nsKey(EVENT_HANDLERS), eventName);
        verify(redisEventHandlerDAO, never()).removeIndex(any());
        verify(redisEventHandlerDAO, never()).recordRedisDaoRequests("removeEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        Map<String, String> all = Maps.newHashMap();
        all.put("testEventHandler1", TestUtils.getJsonString(TestUtils.getSampleEventHandler("testEventHandler1")));
        all.put("testEventHandler2", TestUtils.getJsonString(TestUtils.getSampleEventHandler("testEventHandler2")));

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(all);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        verify(jedisProxy).hgetAll(nsKey(EVENT_HANDLERS));
        verify(objectMapper).readValue(ArgumentMatchers.anyString(), EventHandler.class);
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        String eventName = "testEventHandlersForEvent";

        Map<String, Set<String>> handlersByEvent = Maps.newHashMap();
        handlersByEvent.put(eventName, Lists.newLinkedHashSet(Arrays.asList("testEventHandler1", "testEventHandler2")));

        when(jedisProxy.smembers(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS_BY_EVENT, eventName)))).thenReturn(handlersByEvent.get(eventName).toArray(new String[0]));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);

        verify(jedisProxy).smembers(nsKey(EVENT_HANDLERS_BY_EVENT, eventName));
        assertEquals(2, eventHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnly() throws Exception {
        String eventName = "testEventHandlersForEventWithActiveOnly";

        Map<String, Map<String, String>> allEventHandlers = Maps.newHashMap();
        allEventHandlers.put("testEventHandler1", TestUtils.getJsonString(TestUtils.getSampleEventHandler("testEventHandler1", true)));
        allEventHandlers.put("testEventHandler2", TestUtils.getJsonString(TestUtils.getSampleEventHandler("testEventHandler2", false)));

        when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS))).thenReturn(allEventHandlers);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, true);

        verify(jedisProxy).hgetAll(nsKey(EVENT_HANDLERS));
        assertEquals(1, eventHandlers.size());
        assertEquals("testEventHandler1", eventHandlers.get(0).getName());
    }
}