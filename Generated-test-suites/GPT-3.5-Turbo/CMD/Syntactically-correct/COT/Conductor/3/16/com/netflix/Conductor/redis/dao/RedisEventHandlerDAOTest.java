
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        JedisProxy jedisProxy = new JedisProxy();
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = new ConductorProperties();
        RedisProperties redisProperties = new RedisProperties();
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("TestEventHandler");
        assertEquals(eventHandler, retrievedEventHandler);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerWithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandlerExisting");
        redisEventHandlerDAO.addEventHandler(eventHandler);
        redisEventHandlerDAO.addEventHandler(eventHandler); // Should throw ConflictException
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandlerToUpdate");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        EventHandler updatedEventHandler = new EventHandler();
        updatedEventHandler.setName("TestEventHandlerToUpdate");
        updatedEventHandler.setEvent("UpdatedEvent");

        redisEventHandlerDAO.updateEventHandler(updatedEventHandler);
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("TestEventHandlerToUpdate");
        assertEquals(updatedEventHandler, retrievedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNonExistingEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("NonExistingEventHandler");
        redisEventHandlerDAO.updateEventHandler(eventHandler); // Should throw NotFoundException
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandlerToRemove");
        redisEventHandlerDAO.addEventHandler(eventHandler);

        redisEventHandlerDAO.removeEventHandler("TestEventHandlerToRemove");
        EventHandler retrievedEventHandler = redisEventHandlerDAO.getEventHandler("TestEventHandlerToRemove");
        assertEquals(null, retrievedEventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveNonExistingEventHandler() {
        redisEventHandlerDAO.removeEventHandler("NonExistingEventHandler"); // Should throw NotFoundException
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEventHandler1");
        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEventHandler2");

        redisEventHandlerDAO.addEventHandler(eventHandler1);
        redisEventHandlerDAO.addEventHandler(eventHandler2);

        assertEquals(2, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    // Add more test cases to cover edge cases and exceptions

}
