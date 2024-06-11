import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RedisEventHandlerDAOTest {

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @SuppressWarnings("unchecked")
    private JedisProxy jedisProxy = mock(JedisProxy.class);
    @SuppressWarnings("unchecked")
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    @SuppressWarnings("unchecked")
    private ConductorProperties conductorProperties = mock(ConductorProperties.class);
    @SuppressWarnings("unchecked")
    private RedisProperties redisProperties = mock(RedisProperties.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "jedisProxy", jedisProxy);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "objectMapper", objectMapper);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "conductorProperties", conductorProperties);
        ReflectionTestUtils.setFieldValue(redisEventHandlerDAO, "redisProperties", redisProperties);
    }

    // AddEventHandler test case

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");
        when(objectMapper.writeValueAsString(any(EventHandler.class))).thenReturn("testJson");

        redisEventHandlerDAO.addEventHandler(eventHandler);

        // Check if the event handler is added to the Redis
        verifyEventHandlersCount(1);
        verifyEventHandlerExists("testEventHandler");
    }

    // UpdateEventHandler test case

    @Test
    public void testUpdateEventHandler() {
        String existingEventHandlerName = "existingEventHandler";
        EventHandler existingEventHandler = new EventHandler();
        existingEventHandler.setName(existingEventHandlerName);
        existingEventHandler.setEvent("existingEvent");
        when(jedisProxy.hget(anyString(), eq(existingEventHandlerName))).thenReturn("existingJson");
        when(objectMapper.readValue(anyString(), eq(EventHandler.class))).thenReturn(existingEventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName(existingEventHandlerName);
        updatedEventHandler.setEvent("updatedEvent");
        when(objectMapper.writeValueAsString(updatedEventHandler)).thenReturn("updatedJson");

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);

        // Check if the event handler is updated in the Redis
        verifyEventHandlersCount(1);
        verifyEventHandlerExists("updatedEvent", existingEventHandlerName);
    }

    // GetAllEventHandlers test case

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent("event1");
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("eventHandler1", "eventHandler1Json"));

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent("event2");
        when(jedisProxy.hgetAll(anyString())).thenReturn(Map.of("eventHandler2", "eventHandler2Json"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        // Check if the event handlers are retrieved correctly
        assertThat(eventHandlers, is(Arrays.asList(eventHandler1, eventHandler2)));
    }

    // GetEventHandlersForEvent test case

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "testEvent";

        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("eventHandler1");
        eventHandler1.setEvent(event);
        eventHandler1.setActive(true);
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("eventHandler1"));

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("eventHandler2");
        eventHandler2.setEvent(event);
        eventHandler2.setActive(false);
        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("eventHandler1", "eventHandler2"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        // Check if the event handlers are retrieved correctly
        assertThat(eventHandlers, is(Arrays.asList(eventHandler1, eventHandler2)));
    }

    // Edge cases and exception handling test cases

    // Test addEventHandler with existing name
    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("existingEventHandlerName");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(anyString(), eq("existingEventHandlerName"))).thenReturn("existingJson");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    // Test updateEventHandler with non-existent name
    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerWithNonExistentName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonExistentEventHandlerName");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hget(anyString(), eq("nonExistentEventHandlerName"))).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    // Test getEventHandler with non-existent name
    @Test(expected = NotFoundException.class)
    public void testGetEventHandlerWithNonExistentName() {
        String nonExistentEventHandlerName = "nonExistentEventHandlerName";

        redisEventHandlerDAO.getEventHandler(nonExistentEventHandlerName);
    }

    // Test getEventHandlersForEvent with no matching event handlers
    @Test
    public void testGetEventHandlersForEventWithNoMatchingHandlers() {
        String event = "noMatchingEvent";

        redisEventHandlerDAO.getEventHandlersForEvent(event, false);
    }

    // Test getEventHandlersForEvent with activeOnly set to true and no active handlers
    @Test
    public void testGetEventHandlersForEventWithNoActiveHandlers() {
        String event = "noActiveEvent";

        EventHandler inactiveEventHandler = new EventHandler();
        inactiveEventHandler.setName("inactiveEventHandler");
        inactiveEventHandler.setEvent(event);
        inactiveEventHandler.setActive(false);

        when(jedisProxy.smembers(anyString())).thenReturn(Set.of("inactiveEventHandler"));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        // Check if no event handlers are returned
        assertThat(eventHandlers, is(Collections.emptyList()));
    }

    // Helper methods

    private void verifyEventHandlersCount(int expectedCount) {
        when(jedisProxy.hlen(anyString())).thenReturn(expectedCount);
    }

    private void verifyEventHandlerExists(String name) {
        verifyEventHandlerExists(name, null);
    }

    private void verifyEventHandlerExists(String name, String expectedJson) {
        String json = "";
        try {
            json = jedisProxy.hget(anyString(), eq(name)).getContentAsString();
        } catch (Exception e) {
            throw new TransientException("Unable to get event handler named " + name, e);
        }

        assertThat(json, is(expectedJson));
    }
}