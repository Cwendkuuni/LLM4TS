
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.config.AnyRedisCondition;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Assert;
import org.mockito.Mockito;

@RunWith(MockitoJUnitRunner.class)
public class RedisEventHandlerDAOTest {
    
    @Mock
    private JedisProxy jedisProxy;
    
    @Mock
    private ConductorProperties conductorProperties;

    @Mock
    private RedisProperties properties;

    @Mock
    private AnyRedisCondition condition;

    private RedisEventHandlerDAO subject;

    @Before
    public void setUp() {
        subject = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), conductorProperties, properties);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler_ThrowConflictException() {
        EventHandler handler = new EventHandler();
        handler.setName("Test_Event");

        Mockito.when(jedisProxy.hget(subject.nsKey("EVENT_HANDLERS"), handler.getName())).thenReturn("Test_Event");
        subject.addEventHandler(handler);
    }
    
    @Test
    public void addEventHandler_ExecuteWithoutException() {
        EventHandler handler = new EventHandler();
        handler.setName("Test_Event2");

        Mockito.when(jedisProxy.hget(subject.nsKey("EVENT_HANDLERS"), handler.getName())).thenReturn(null);
        subject.addEventHandler(handler);
        Mockito.verify(jedisProxy).hset(subject.nsKey("EVENT_HANDLERS"), handler.getName(), subject.toJson(handler));
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandler_ThrowNotFoundException() {
        EventHandler handler = new EventHandler();
        handler.setName("Test_Event2");
        
        Mockito.when(jedisProxy.hget(subject.nsKey("EVENT_HANDLERS"), handler.getName())).thenReturn(null);
        subject.updateEventHandler(handler);
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandler_ThrowNotFoundException() {
        Mockito.when(jedisProxy.hget(subject.nsKey("EVENT_HANDLERS"), "Test_Event2")).thenReturn(null);
        subject.removeEventHandler("Test_Event2");
    }

    @Test
    public void getAllEventHandlers_ReturnEmptyList() {
        Mockito.when(jedisProxy.hgetAll(subject.nsKey("EVENT_HANDLERS"))).thenReturn(new HashMap<>());
        Assert.assertEquals(subject.getAllEventHandlers().size(), 0);
    }

    //Continue writing such tests for all other methods of RedisEventHandlerDAO
}
