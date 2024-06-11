import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.netflix.conductor.dao.EventHandlerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.dao.RedisEventHandlerDAO;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {

    @Mock
    private JedisProxy jedisProxy;

    @Mock
    private ConductorProperties properties;

    @Mock
    private RedisProperties redisProperties;

    private EventHandlerDAO eventHandlerDAO;

    @Before
    public void setUp() {
        when(jedisProxy.hset(anyString(), anyString(), anyString())).thenReturn(1L);
        eventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), properties, redisProperties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("TEST_EVENT");
        eventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("TEST_EVENT");
        eventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected=NotFoundException.class)
    public void testRemoveEventHandler_NotFound() {
        eventHandlerDAO.removeEventHandler("testEventHandler");
    }

    @Test
    public void testGetAllEventHandlers() {
        List<EventHandler> handlers = eventHandlerDAO.getAllEventHandlers();
        assertNotNull(handlers);
    }
    
    @Test
    public void testGetEventHandlersForEvent() {
        List<EventHandler> handlers = eventHandlerDAO.getEventHandlersForEvent("TEST_EVENT", true);
        assertNotNull(handlers);
    }

}