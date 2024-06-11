import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RedisProperties redisProperties;

    @InjectMocks
    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler testEventHandler = new EventHandler();
        testEventHandler.setName("testEventHandler");

        // Test adding a new event handler
        redisEventHandlerDAO.addEventHandler(testEventHandler);

        // Verify that the event handler was added
        assertEquals(testEventHandler, redisEventHandlerDAO.getEventHandler("testEventHandler"));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler testEventHandler = new EventHandler();
        testEventHandler.setName("testEventHandler");
        
        // Test updating an existing event handler
        redisEventHandlerDAO.updateEventHandler(testEventHandler);
        
        // Verify that the event handler was updated
        assertEquals(testEventHandler, redisEventHandlerDAO.getEventHandler("testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        // Test removing a non-existent event handler
        redisEventHandlerDAO.removeEventHandler("nonExistentEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        // Test getting all event handlers
        assertNotEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Test getting event handlers for a specific event
        assertNotEquals(0, redisEventHandlerDAO.getEventHandlersForEvent("testEvent", false).size());
    }

    @Test(expected = TransientException.class)
    public void testGetEventHandlerException() {
        // Mock an exception from JedisProxy
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new DataAccessException("Test Exception"));

        // Test getting an event handler with exception
        redisEventHandlerDAO.getEventHandler("testEventHandler");
    }

}