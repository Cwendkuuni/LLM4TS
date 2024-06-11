import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Mock
    private EventHandlerDAO eventHandlerDAO;

    @Test
    public void testAddEventHandler() {
        // Typical use case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setEvent("TestEvent");
        eventHandler.setActive(true);

        when(eventHandlerDAO.getEventHandler("TestEvent")).thenReturn(null);
        eventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        // Edge case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setEvent("TestEvent");
        eventHandler.setActive(true);

        when(eventHandlerDAO.getEventHandler("TestEvent")).thenReturn(eventHandler);
        eventHandler.setActive(false);
        eventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testRemoveEventHandler() {
        // Error scenario
        when(eventHandlerDAO.getEventHandler("NonExistentEvent")).thenReturn(null);
        assertThrows(NotFoundException.class, () -> eventHandlerDAO.removeEventHandler("NonExistentEvent"));
    }

    @Test     
    public void testGetAllEventHandlers() {
        // Typical use case
        List<EventHandler> expectedHandlers = new ArrayList<>();
        EventHandler eventHandler1 = new EventHandler();
        eventHandler1.setName("TestEvent1");
        eventHandler1.setEvent("TestEvent1");
        eventHandler1.setActive(true);

        EventHandler eventHandler2 = new EventHandler();
        eventHandler2.setName("TestEvent2");
        eventHandler2.setEvent("TestEvent2");
        eventHandler2.setActive(true);

        expectedHandlers.add(eventHandler1);
        expectedHandlers.add(eventHandler2);

        when(eventHandlerDAO.getAllEventHandlers()).thenReturn(expectedHandlers);
        List<EventHandler> actualHandlers = eventHandlerDAO.getAllEventHandlers();
        assertEquals(expectedHandlers.size(), actualHandlers.size());
    }

    @Test
    public void testGetEventHandlersForEvent() {
        // Error scenario
        when(jedisProxy.smembers(anyString())).thenThrow(new RuntimeException());
        assertThrows(TransientException.class, () -> eventHandlerDAO.getEventHandlersForEvent("InvalidEvent", false));
    }

    @Test
    public void testGetEventHandler() {
        // Edge case
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEvent");
        eventHandler.setEvent("TestEvent");
        eventHandler.setActive(true);
        
        String eventHandlerJson = "{ \"name\": \"TestEvent\", \"event\": \"TestEvent\", \"active\": true }";
        
        when(jedisProxy.hget(anyString(), anyString())).thenReturn(eventHandlerJson);
        
        EventHandler actualEventHandler = eventHandlerDAO.getEventHandler("TestEvent");
        assertEquals(eventHandler.getName(), actualEventHandler.getName());
        assertEquals(eventHandler.getEvent(), actualEventHandler.getEvent());
        assertEquals(eventHandler.isActive(), actualEventHandler.isActive());
    }
}