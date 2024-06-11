import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        JedisProxy jedisProxy = mock(JedisProxy.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        ConductorProperties conductorProperties = mock(ConductorProperties.class);
        RedisProperties properties = mock(RedisProperties.class);
        EventHandlerDAO redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");
        eventHandler.setEvent("testEvent");

        when(jedisProxy.hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        redisEventHandlerDAO.addEventHandler(eventHandler);
        assertNotNull(redisEventHandlerDAO.getEventHandler("testHandler"));
    }
}