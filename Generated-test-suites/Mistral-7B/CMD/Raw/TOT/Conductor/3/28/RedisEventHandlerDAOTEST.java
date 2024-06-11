import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.collect.Lists;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.testutils.JedisTestUtils;
import com.netflix.conductor.testutils.MockitoExtension;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@Configuration
@Import(RedisEventHandlerDAO.class)
public class RedisEventHandlerDAOTest {

    private static final String EVENT_HANDLER_NAME = "testEventHandler";
    private static final String EVENT = "testEvent";
    private static final String NON_EXISTING_EVENT_HANDLER_NAME = "nonExistingEventHandlerName";

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties redisProperties;
    private EventHandlerDAO redisEventHandlerDAO;

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

    @Bean
    public EventHandlerDAO redisEventHandlerDAO() {
        return new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Before
    public void setup() {
        Mockito.when(jedisProxy.hgetAll(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS))))
                .thenReturn(JedisTestUtils.map(""));
        Mockito.when(jedisProxy.hget(nsKey(EVENT_HANDLERS), EVENT_HANDLER_NAME))
                .thenReturn(null);
        Mockito.when(jedisProxy.smembers(ArgumentMatchers.eq(nsKey(EVENT_HANDLERS_BY_EVENT, EVENT))))
                .thenReturn(Collections.emptySet());
        Mockito.when(jedisProxy.hget(nsKey(EVENT_HANDLERS), NON_EXISTING_EVENT_HANDLER_NAME))
                .thenReturn(null);
    }

    @Test
    public void testAddEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertEquals(eventHandler.getName(), jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
        assertTrue(jedisProxy.exists(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent())));
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithNameAlreadyExists() throws Exception {
        EventHandler existingEventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(existingEventHandler);

        EventHandler newEventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(newEventHandler);
    }

    @Test
    public void testUpdateEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        String updatedEventHandlerName = "updatedEventHandlerName";
        EventHandler updatedEventHandler = new EventHandler(updatedEventHandlerName, EVENT, null);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
        assertEquals(updatedEventHandlerName, jedisProxy.hget(nsKey(EVENT_HANDLERS), updatedEventHandlerName));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistingEventHandler() throws Exception {
        String nonExistingEventHandlerName = "nonExistingEventHandlerName";
        EventHandler updatedEventHandler = new EventHandler(nonExistingEventHandlerName, EVENT, null);
        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
    }

    @Test
    public void testRemoveEventHandler() throws Exception {
        EventHandler eventHandler = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler(eventHandler.getName());

        assertNull(jedisProxy.hget(nsKey(EVENT_HANDLERS), eventHandler.getName()));
        assertFalse(jedisProxy.exists(nsKey(EVENT_HANDLERS_BY_EVENT, eventHandler.getEvent())));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerWithNonExistingEventHandler() throws Exception {
        redisEventHandlerDAO.removeEventHandler(NON_EXISTING_EVENT_HANDLER_NAME);
    }

    @Test
    public void testGetAllEventHandlers() throws Exception {
        EventHandler eventHandler1 = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEvent() throws Exception {
        EventHandler eventHandler1 = createEventHandler();
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler("eventHandler2", EVENT, null);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, false);

        assertEquals(2, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler1));
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test
    public void testGetEventHandlersForEventWithActiveOnlyTrue() throws Exception {
        EventHandler eventHandler1 = createEventHandler();
        eventHandler1.setActive(false);
        redisEventHandlerDAO.addEventHandler(eventHandler1);

        EventHandler eventHandler2 = new EventHandler("eventHandler2", EVENT, null);
        eventHandler2.setActive(true);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(EVENT, true);

        assertEquals(1, eventHandlers.size());
        assertTrue(eventHandlers.contains(eventHandler2));
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEventWithNonExistingEvent() throws Exception {
        redisEventHandlerDAO.getEventHandlersForEvent("nonExistingEvent", false);
    }

    private EventHandler createEventHandler() {
        return new EventHandler(EVENT_HANDLER_NAME, EVENT, null);
    }

    private String nsKey(String... parts) {
        Map<String, String> namespace = new LinkedHashMap<String, String>() {{
            putAll(getBaseNamespace());
            for (String part : parts) {
                put(part, part);
            }
        }};

        return String.join(":", namespace.values());
    }

    private Map<String, String> getBaseNamespace() {
        return new LinkedHashMap<String, String>() {{
            put("CONDUCTOR", "DAOS");
            put("REDIS", "EVENT_HANDLER_DAO");
        }};
    }
}