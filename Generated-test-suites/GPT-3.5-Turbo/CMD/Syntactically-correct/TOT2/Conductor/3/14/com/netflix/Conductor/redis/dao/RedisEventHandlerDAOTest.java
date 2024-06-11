import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.ConflictException;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);
        
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        when(dao.getEventHandler("testEventHandler")).thenReturn(null);
        
        dao.addEventHandler(eventHandler);
        
        verify(dao, times(1)).addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);
        
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        when(dao.getEventHandler("testEventHandler")).thenReturn(eventHandler);
        
        dao.updateEventHandler(eventHandler);

        verify(dao, times(1)).updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);
        
        String name = "nonExistentEventHandler";
        
        when(dao.getEventHandler(name)).thenReturn(null);
        
        dao.removeEventHandler(name);
    }

    @Test
    public void testGetAllEventHandlers() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);

        dao.getAllEventHandlers();

        verify(dao, times(1)).getAllEventHandlers();
    }

    @Test
    public void testGetEventHandlersForEvent() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);

        dao.getEventHandlersForEvent("testEvent", false);

        verify(dao, times(1)).getEventHandlersForEvent("testEvent", false);
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_NotFound() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);

        when(dao.getEventHandler(any(String.class))).thenReturn(null);

        dao.getEventHandlersForEvent("nonExistentEvent", false);
    }

    @Test
    public void testGetEventHandler() {
        RedisEventHandlerDAO dao = mock(RedisEventHandlerDAO.class);

        dao.getEventHandler("testEventHandler");

        verify(dao, times(1)).getEventHandler("testEventHandler");
    }
}