
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Mock
    private JedisProxy jedisProxy;
    @Mock
    private ConductorProperties conductorProperties;
    @Mock
    private RedisProperties redisProperties;

    @Before
    public void setUp() {
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(),
                conductorProperties, redisProperties);
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandler_WithExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_name");

        when(jedisProxy.hget("EVENT_HANDLERS", eventHandler.getName())).thenReturn(eventHandler);

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandler_WithOutExistingName() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_name");

        when(jedisProxy.hget("EVENT_HANDLERS", eventHandler.getName())).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler_WithOutExistingName() {
        String handlerName = "test_name";

        when(jedisProxy.hget("EVENT_HANDLERS", handlerName)).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler(handlerName);
    }

    @Test
    public void testGetAllEventHandlers() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("test_name");

        when(jedisProxy.hgetAll("EVENT_HANDLERS"))
                .thenReturn(Collections.singletonMap(eventHandler.getName(), "test_json"));

        assertEquals(1, redisEventHandlerDAO.getAllEventHandlers().size());
        assertEquals(eventHandler.getName(), redisEventHandlerDAO.getAllEventHandlers().get(0).getName());
    }

    @Test(expected = NotFoundException.class)
    public void testGetEventHandlersForEvent_WithoutExistingEvent() {
        String eventName = "test_event";

        when(jedisProxy.smembers("EVENT_HANDLERS_BY_EVENT", eventName)).thenReturn(Collections.emptySet());

        assertEquals(0, redisEventHandlerDAO.getEventHandlersForEvent(eventName, true).size());
    }
    
    @Test
    public void testGetEventHandler_Success() {
        String handlerName = "test_name";
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(handlerName);
    
        when(jedisProxy.hget("EVENT_HANDLERS", handlerName)).thenReturn("{'name':'test_name'}");
    
        EventHandler getResult = redisEventHandlerDAO.getEventHandler(handlerName);
        assertNotNull(getResult);
        assertEquals(eventHandler.getName(), getResult.getName());
    }
    
    @Test(expected = TransientException.class)
    public void testGetEventHandler_Failure() {
        String handlerName = "test_name";
    
        when(jedisProxy.hget("EVENT_HANDLERS", handlerName)).thenThrow(new RuntimeException());
        
        redisEventHandlerDAO.getEventHandler(handlerName);
    }
    
    @Test
    public void testGetEventHandler_NullResult() {
        String handlerName = "test_name";
    
        when(jedisProxy.hget("EVENT_HANDLERS", handlerName)).thenReturn(null);
    
        EventHandler getResult = redisEventHandlerDAO.getEventHandler(handlerName);
        assertNull(getResult);
    }
}
