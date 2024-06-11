import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, null, null, null);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        assertEquals(eventHandler, redisEventHandlerDAO.getEventHandler("testEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        eventHandler.setEvent("updatedEvent");
        redisEventHandlerDAO.updateEventHandler(eventHandler);

        assertEquals("updatedEvent", redisEventHandlerDAO.getEventHandler("testEventHandler").getEvent());
    }

    @Test(expected = ConflictException.class)
    public void testAddExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }
    
    // Additional test cases for other methods can be added here

}