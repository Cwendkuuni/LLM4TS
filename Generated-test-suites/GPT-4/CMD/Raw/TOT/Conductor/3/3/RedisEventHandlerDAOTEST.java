import org.junit.Before;
import org.junit.Test;

import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.netflix.conductor.redis.config.RedisProperties;
import org.mockito.Mockito;
import redis.clients.jedis.JedisPool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class RedisEventHandlerDAOTest {

    private JedisPool pool;
    private RedisProperties properties;
    private EventHandlerDAO dao;

    @Before
    public void setUp() {
        pool = Mockito.mock(JedisPool.class);
        properties = new RedisProperties();
        dao = new RedisEventHandlerDAO(pool, properties);
    }

    @Test(expected = ConflictException.class)
    public void addEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        when(pool.getResource()).thenReturn(jedis);
        when(jedis.hget(any(), any())).thenReturn(null);

        dao.addEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testHandler");

        when(pool.getResource()).thenReturn(jedis);
        when(jedis.hget(any(), any())).thenReturn(null);

        dao.updateEventHandler(eventHandler);
        verify(jedis).hset(any(), any(), any());
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandler() {
        String name = "testHandler";

        when(pool.getResource()).thenReturn(jedis);
        when(jedis.hget(any(), any())).thenReturn(null);

        dao.removeEventHandler(name);
        verify(jedis).hdel(any(), any());
    }

    @Test
    public void getEventHandlers() {
        when(pool.getResource()).thenReturn(jedis);
        when(jedis.hgetAll(any())).thenReturn(new HashMap<String, String>());

        List<EventHandler> result = dao.getAllEventHandlers();
        assertEquals(result.size(), 0);
    }

    @Test
    public void getEventHandlersForEvent() {
        String event = "testEvent";
        boolean activeOnly = false;

        when(pool.getResource()).thenReturn(jedis);
        when(jedis.smembers(any())).thenReturn(new HashSet<String>());

        List<EventHandler> result = dao.getEventHandlersForEvent(event, activeOnly);
        assertEquals(result.size(), 0);
    }

}