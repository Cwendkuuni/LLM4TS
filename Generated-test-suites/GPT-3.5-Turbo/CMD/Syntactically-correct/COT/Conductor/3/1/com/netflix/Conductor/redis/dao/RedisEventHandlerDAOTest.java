
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;

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

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties,
                properties);
    }

    @Test
    public void testAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler"))).thenReturn(null);

        redisEventHandlerDAO.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq("EVENT_HANDLERS"), Mockito.anyString());
    }

    @Test(expected = ConflictException.class)
    public void testAddEventHandlerAlreadyExists() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler")))
                .thenReturn("{ \"name\": \"testEventHandler\", \"event\": \"testEvent\" }");

        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test
    public void testUpdateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler")))
                .thenReturn("{ \"name\": \"testEventHandler\", \"event\": \"testEvent\" }");

        redisEventHandlerDAO.updateEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.eq("EVENT_HANDLERS"), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateEventHandlerNotFound() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testEventHandler");
        eventHandler.setEvent("testEvent");

        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler"))).thenReturn(null);

        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test
    public void testRemoveEventHandler() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler")))
                .thenReturn("{ \"name\": \"testEventHandler\", \"event\": \"testEvent\" }");

        redisEventHandlerDAO.removeEventHandler("testEventHandler");

        Mockito.verify(jedisProxy).hdel(Mockito.anyString(), Mockito.eq("testEventHandler"));
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandlerNotFound() {
        Mockito.when(jedisProxy.hget(Mockito.anyString(), Mockito.eq("testEventHandler"))).thenReturn(null);

        redisEventHandlerDAO.removeEventHandler("testEventHandler");
    }
}

