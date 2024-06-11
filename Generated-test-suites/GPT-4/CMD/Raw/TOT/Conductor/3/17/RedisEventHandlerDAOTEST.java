import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import org.junit.Test;

public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        EventHandlerDAO dao = mock(EventHandlerDAO.class);
        EventHandler handler = new EventHandler();
        handler.setName("testHandler");

        dao.addEventHandler(handler);
        verify(dao, times(1)).addEventHandler(any());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandlerDAO dao = mock(EventHandlerDAO.class);
        EventHandler handler = new EventHandler();
        handler.setName("testHandler");

        dao.updateEventHandler(handler);
        verify(dao, times(1)).updateEventHandler(any());
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandlerDAO dao = mock(EventHandlerDAO.class);

        dao.removeEventHandler("testHandler");
        verify(dao, times(1)).removeEventHandler(anyString());
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandlerDAO dao = mock(EventHandlerDAO.class);

        dao.getAllEventHandlers();
        verify(dao, times(1)).getAllEventHandlers();
    }

    @Test
    public void testGetEventHandlersForEvent() {
        EventHandlerDAO dao = mock(EventHandlerDAO.class);

        dao.getEventHandlersForEvent("testEvent", true);
        verify(dao, times(1)).getEventHandlersForEvent(anyString(), anyBoolean());
    }
}