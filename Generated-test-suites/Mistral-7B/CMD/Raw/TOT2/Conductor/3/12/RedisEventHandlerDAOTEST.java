
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class RedisEventHandlerDAOTest {

    @MockBean
    private JedisProxy jedisProxy;

    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private ConductorProperties conductorProperties;

    @MockBean
    private RedisProperties properties;

    @Autowired
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Test
    public void testAddEventHandlerSuccess() {
        // Test case for addEventHandler method
    }

    @Test
    public void testAddEventHandlerConflict() {
        // Test case for addEventHandler method handling conflict
    }

    @Test
    public void testRemoveEventHandlerSuccess() {
        // Test case for removeEventHandler method
    }

    @Test
    public void testRemoveEventHandlerNotFound() {
        // Test case for removeEventHandler method handling not found
    }

    @Test
    public void testGetAllEventHandlers() {
        // Test case for getAllEventHandlers method
    }

    @Test
    public void testGetEventHandlersForEventSuccess() {
        String event = "testEvent";
        EventHandler eventHandler1 = new EventHandler("testEvent", "testClass1", "testMethod1");
        EventHandler eventHandler2 = new EventHandler("testEvent", "testClass2", "testMethod2");
        EventHandler eventHandler3 = new EventHandler("testEvent2", "testClass3", "testMethod3");

        Mockito.when(jedisProxy.hgetAll(nsKey(EVENT_HANDLERS_BY_EVENT, event)))
                .thenReturn(Map.of("eventHandler1", toJson(eventHandler1), "eventHandler2", toJson(eventHandler2)));

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, false);

        Assertions.assertEquals(2, eventHandlers.size());
        Assertions.assertEquals(eventHandler1, eventHandlers.get(0));
        Assertions.assertEquals(eventHandler2, eventHandlers.get(1));

        List<EventHandler> activeEventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);

        Assertions.assertEquals(1, activeEventHandlers.size());
        Assertions.assertEquals(eventHandler1, activeEventHandlers.get(0));
    }
}

// Other test cases for other methods (addEventHandler, removeEventHandler, etc.)