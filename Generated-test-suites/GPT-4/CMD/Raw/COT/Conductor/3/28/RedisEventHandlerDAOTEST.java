import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.LinkedList;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO; 

public class RedisEventHandlerDAOTest {     

    @Test (expected = ConflictException.class)  
    public void testAddEventHandler_ConflictException() {
        // Mock the objects
        EventHandler eventHandler = mock(EventHandler.class);
        RedisEventHandlerDAO handlerDAO = mock(RedisEventHandlerDAO.class);

        // Define the method behavior
        when(handlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        // Call the method
        handlerDAO.addEventHandler(eventHandler);
    }
                  
    @Test (expected = NotFoundException.class)
    public void testUpdateEventHandler_NotFoundException() {
        // Same as above mock and define
        EventHandler eventHandler = mock(EventHandler.class);
        RedisEventHandlerDAO handlerDAO = mock(RedisEventHandlerDAO.class);

        when(handlerDAO.getEventHandler(eventHandler.getName())).thenThrow(new NotFoundException());

        handlerDAO.updateEventHandler(eventHandler);
    }
    
    @Test  
    public void testRemoveEventHandler() {
        // Same as above mock and define
        EventHandler eventHandler = mock(EventHandler.class);
        RedisEventHandlerDAO handlerDAO = mock(RedisEventHandlerDAO.class);

        when(handlerDAO.getEventHandler(eventHandler.getName())).thenReturn(eventHandler);

        handlerDAO.removeEventHandler(eventHandler.getName());

        // Verify method was called with the right parameters
        verify(handlerDAO).removeEventHandler(eventHandler.getName());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler = mock(EventHandler.class);
        RedisEventHandlerDAO handlerDAO = mock(RedisEventHandlerDAO.class);

        when(handlerDAO.getAllEventHandlers()).thenReturn(new LinkedList<EventHandler>(Arrays.asList(eventHandler)));

        assertEquals(handlerDAO.getAllEventHandlers().size(), 1);
    }

    @Test
    public void testGetEventHandlersForEvent() {
        String eventName = "TestEvent";
        EventHandler eventHandler = mock(EventHandler.class);
        RedisEventHandlerDAO handlerDAO = mock(RedisEventHandlerDAO.class);

        when(eventHandler.getEvent()).thenReturn(eventName);
        when(handlerDAO.getEventHandlersForEvent(eventName, true))
                .thenReturn(new LinkedList<EventHandler>(Arrays.asList(eventHandler)));
        assertEquals(eventName, handlerDAO.getEventHandlersForEvent(eventName, true).get(0).getEvent());
    }

}