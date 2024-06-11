import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;
    private JedisProxy jedisProxyMock;
    private ObjectMapper objectMapperMock;
    private ConductorProperties conductorPropertiesMock;
    private RedisProperties redisPropertiesMock;

    @Before
    public void setup() {
        jedisProxyMock = mock(JedisProxy.class);
        objectMapperMock = mock(ObjectMapper.class);
        conductorPropertiesMock = mock(ConductorProperties.class);
        redisPropertiesMock = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxyMock, objectMapperMock, conductorPropertiesMock, redisPropertiesMock);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxyMock, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerConflict() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
        redisEventHandlerDAO.addEventHandler(eventHandler);  
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxyMock, times(1)).hset(anyString(), anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestEventHandler");
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.updateEventHandler(eventHandler); 
    }

    @Test
    public void testRemoveEventHandler() {
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
        redisEventHandlerDAO.removeEventHandler("TestEventHandler");
        verify(jedisProxyMock, times(1)).hdel(anyString(), anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
        redisEventHandlerDAO.removeEventHandler("TestEventHandler");  
    }

    @Test
    public void testGetAllEventHandlers() {
        when(jedisProxyMock.hgetAll(anyString())).thenReturn(null);
        List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
        assertNull(handlers);
    }

}