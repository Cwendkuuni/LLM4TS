import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @InjectMocks
    private RedisEventHandlerDAO dao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        when(jedisProxy.hget("EVENT_HANDLERS", "testEventHandler")).thenReturn(null);
        
        dao.addEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset("EVENT_HANDLERS", "testEventHandler", dao.toJson(eventHandler));
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        when(jedisProxy.hget("EVENT_HANDLERS", "testEventHandler")).thenReturn(dao.toJson(eventHandler));
        
        dao.updateEventHandler(eventHandler);
        
        verify(jedisProxy, times(1)).hset("EVENT_HANDLERS", "testEventHandler", dao.toJson(eventHandler));
    }

    @Test
    public void testRemoveEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        
        when(jedisProxy.hget("EVENT_HANDLERS", "testEventHandler")).thenReturn(dao.toJson(eventHandler));
        
        dao.removeEventHandler("testEventHandler");
        
        verify(jedisProxy, times(1)).hdel("EVENT_HANDLERS", "testEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> expectedHandlers = new LinkedList<>();
        EventHandler handler1 = new EventHandler();
        handler1.setName("testHandler1");
        EventHandler handler2 = new EventHandler();
        handler2.setName("testHandler2");
        expectedHandlers.add(handler1);
        expectedHandlers.add(handler2);
        
        when(jedisProxy.hgetAll("EVENT_HANDLERS")).thenReturn(expectedHandlers);
        
        List<EventHandler> actualHandlers = dao.getAllEventHandlers();
        
        assertEquals(expectedHandlers.size(), actualHandlers.size());
        assertEquals(expectedHandlers.get(0).getName(), actualHandlers.get(0).getName());
        assertEquals(expectedHandlers.get(1).getName(), actualHandlers.get(1).getName());
    }
}