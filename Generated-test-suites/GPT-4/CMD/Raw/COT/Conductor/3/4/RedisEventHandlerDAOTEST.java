
import static org.mockito.Mockito.*;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.core.exception.TransientException;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

public class RedisEventHandlerDAOTest {
    
    @Mock
    JedisProxy jedisProxy;

    @Mock
    EventHandler eventHandler, eventHandlerExisting;

    @Test(expected = NullPointerException.class)
    public void testAddEventHandler_Null() {
        new RedisEventHandlerDAO().addEventHandler(null);
    }

    @Test(expected=ConflictException.class)
    public void testAddEventHandler_Existing() {
        when(eventHandler.getName()).thenReturn("name");
        when(eventHandlerExisting.getName()).thenReturn("name");
        new RedisEventHandlerDAO().addEventHandler(eventHandler);
        new RedisEventHandlerDAO().addEventHandler(eventHandlerExisting);
    }

    @Test(expected=NotFoundException.class)
    public void testUpdateEventHandler_NotFound() {
        when(eventHandler.getName()).thenReturn("name");

        new RedisEventHandlerDAO().updateEventHandler(eventHandler);
    }

    @Test(expected=NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        new RedisEventHandlerDAO().removeEventHandler("name");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = new RedisEventHandlerDAO().getAllEventHandlers();

        Assert.assertNotNull(handlers);
    }

    @Test(expected=TransientException.class)
    public void testGetEventHandlersForEvent_TransientException() {
        when(jedisProxy.hget(anyString(), anyString())).thenThrow(new RuntimeException());
        new RedisEventHandlerDAO().getEventHandlersForEvent("event-name", true);
    }
}
