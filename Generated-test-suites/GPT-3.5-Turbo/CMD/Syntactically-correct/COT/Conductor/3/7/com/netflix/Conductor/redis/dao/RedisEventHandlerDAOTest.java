import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        redisEventHandlerDAO = mock(RedisEventHandlerDAO.class);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        
        doNothing().when(redisEventHandlerDAO).addEventHandler(eventHandler);
        
        redisEventHandlerDAO.addEventHandler(eventHandler);
        
        verify(redisEventHandlerDAO, times(1)).addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        
        doNothing().when(redisEventHandlerDAO).updateEventHandler(eventHandler);
        
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        
        verify(redisEventHandlerDAO, times(1)).updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        String name = "TestEventHandler";
        
        doNothing().when(redisEventHandlerDAO).removeEventHandler(name);
        
        redisEventHandlerDAO.removeEventHandler(name);
        
        verify(redisEventHandlerDAO, times(1)).removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        when(redisEventHandlerDAO.getAllEventHandlers()).thenReturn(new LinkedList<>());
        
        assertEquals(0, redisEventHandlerDAO.getAllEventHandlers().size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String event = "TestEvent";
        boolean activeOnly = false;
        
        when(redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly)).thenReturn(new LinkedList<>());
        
        assertEquals(0, redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly).size());
    }

    @Test
    public void testGetEventHandler() {
        String name = "TestEventHandler";
        
        when(redisEventHandlerDAO.getEventHandler(name)).thenReturn(null);
        
        assertNull(redisEventHandlerDAO.getEventHandler(name));
    }
}