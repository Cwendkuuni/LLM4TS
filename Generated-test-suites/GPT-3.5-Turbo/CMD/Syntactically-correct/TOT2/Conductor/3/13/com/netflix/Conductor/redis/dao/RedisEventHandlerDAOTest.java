import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        when(jedisProxy.hset("EVENT_HANDLERS", eventHandler.getName(), objectMapper.toJson(eventHandler)))
                .thenReturn(1L);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        when(jedisProxy.hset("EVENT_HANDLERS", eventHandler.getName(), objectMapper.toJson(eventHandler)))
                .thenReturn(1L);

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertNotNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        redisEventHandlerDAO.removeEventHandler("TestEventHandler");

        assertNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        assertNotNull(redisEventHandlerDAO.getAllEventHandlers());
    }

    @Test
    public void testIndex() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true));
    }

    @Test
    public void testRemoveIndex() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("TestEventHandler");
        assertTrue(redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true).isEmpty());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", true));
    }

    @Test
    public void testGetEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

}