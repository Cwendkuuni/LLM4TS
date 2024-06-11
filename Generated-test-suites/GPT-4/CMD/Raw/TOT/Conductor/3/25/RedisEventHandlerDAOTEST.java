import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.redis.jedis.JedisProxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
public class RedisEventHandlerDAOTest {

    private RedisEventHandlerDAO redisEventHandlerDAO;

    @Before
    public void setUp() {
        JedisProxy jedisProxy = mock(JedisProxy.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        RedisProperties properties = mock(RedisProperties.class);
        redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler_NameExists_ThrowsException() {
        EventHandler eventHandler = createEventHandler("test");
        redisEventHandlerDAO.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandler_NameNotFound_ThrowsException() {
        EventHandler eventHandler = createEventHandler("test");
        redisEventHandlerDAO.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandler_NameNotFound_ThrowsException() {
        redisEventHandlerDAO.removeEventHandler("test");
    }

    @Test
    public void getAllEventHandlers_NoEventHandlers_ReturnsEmptyList() {
        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();
        assertThat(eventHandlers, is(new LinkedList<>()));
    }

    @Test(expected = NotFoundException.class)
    public void getEventHandlersForEvent_NameNotFound_ThrowsException() {
        redisEventHandlerDAO.getEventHandlersForEvent("test", true);
    }

    private EventHandler createEventHandler(String name) {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName(name);
        return eventHandler;
    }
}