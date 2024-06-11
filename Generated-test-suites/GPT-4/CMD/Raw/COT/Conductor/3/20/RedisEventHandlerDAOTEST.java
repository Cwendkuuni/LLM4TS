import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class RedisEventHandlerDAOTest {

    private final RedisEventHandlerDAO redisEventHandlerDAO =
        Mockito.mock(RedisEventHandlerDAO.class);

    @Test(expected = ConflictException.class)
    public void addExistingEventHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("existingHandler");

        when(redisEventHandlerDAO.getEventHandler("existingHandler"))
            .thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistentHandlerTest() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("nonExistentHandler");

        when(redisEventHandlerDAO.getEventHandler("nonExistentHandler"))
            .thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void removeNonExistentHandlerTest() {
        String handlerName = "nonExistentHandler";
        
        when(redisEventHandlerDAO.getEventHandler(handlerName))
            .thenReturn(null);
        
        redisEventHandlerDAO.removeEventHandler(handlerName);
    }

    @Test
    public void getAllEventHandlersTest() {
        List<EventHandler> expectedHandlers = new ArrayList<>();
        // Populate the list here

        when(redisEventHandlerDAO.getAllEventHandlers())
            .thenReturn(expectedHandlers);
        
        List<EventHandler> actualHandlers = redisEventHandlerDAO.getAllEventHandlers();
        
        assertEquals(expectedHandlers, actualHandlers);
    }

    @Test
    public void getEventHandlersForEventTest() {
        String event = "testEvent";
        boolean activeOnly = true;

        List<EventHandler> expectedHandlers = new ArrayList<>();
        // Populate the list here

        when(redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly))
            .thenReturn(expectedHandlers);
        
        List<EventHandler> actualHandlers = redisEventHandlerDAO.getEventHandlersForEvent(event, activeOnly);
        
        assertEquals(expectedHandlers, actualHandlers);
    }
}