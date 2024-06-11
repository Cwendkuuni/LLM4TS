import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ConductorProperties conductorProperties = new ConductorProperties();
    private RedisProperties redisProperties = new RedisProperties();

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        eventHandler.setEvent("UpdatedEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        eventHandler.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.removeEventHandler("TestEventHandler");
        assertNull(redisEventHandlerDAO.getEventHandler("TestEventHandler"));
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        eventHandler1.setEvent("TestEvent1");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(2, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        eventHandler1.setEvent("TestEvent");

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");
        eventHandler2.setEvent("TestEvent");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(2, redisEventHandlerDAO.getEventHandlersForEvent("TestEvent", false).size());
    }

}