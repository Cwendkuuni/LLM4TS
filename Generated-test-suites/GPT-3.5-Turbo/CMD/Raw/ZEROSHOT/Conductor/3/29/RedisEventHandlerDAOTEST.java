import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.core.exception.ConflictException;
import com.netflix.conductor.core.exception.NotFoundException;
import com.netflix.conductor.dao.EventHandlerDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.core.config.ConductorProperties;
import com.netflix.conductor.redis.config.RedisProperties;
import com.netflix.conductor.redis.jedis.JedisProxy;

public class RedisEventHandlerDAOTest {

	private JedisProxy jedisProxyMock;
	private ObjectMapper objectMapperMock;
	private ConductorProperties conductorPropertiesMock;
	private RedisProperties redisPropertiesMock;
	private RedisEventHandlerDAO redisEventHandlerDAO;

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
		eventHandler.setName("test_event");
		when(jedisProxyMock.hset(anyString(), anyString(), anyString())).thenReturn(1L);
		redisEventHandlerDAO.addEventHandler(eventHandler);
		verify(jedisProxyMock, times(1)).hset(anyString(), anyString(), anyString());
	}

	@Test(expected = ConflictException.class)
	public void testAddEventHandlerConflict() {
		EventHandler existing = new EventHandler();
		existing.setName("existing_event");
		when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
		redisEventHandlerDAO.addEventHandler(existing);
	}

	@Test
	public void testUpdateEventHandler() {
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName("test_event");
		when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
		when(jedisProxyMock.hset(anyString(), anyString(), anyString())).thenReturn(1L);
		redisEventHandlerDAO.updateEventHandler(eventHandler);
		verify(jedisProxyMock, times(1)).hget(anyString(), anyString());
		verify(jedisProxyMock, times(1)).hset(anyString(), anyString(), anyString());
	}

	@Test(expected = NotFoundException.class)
	public void testUpdateEventHandlerNotFound() {
		EventHandler missing = new EventHandler();
		missing.setName("missing_event");
		when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
		redisEventHandlerDAO.updateEventHandler(missing);
	}

	@Test
	public void testRemoveEventHandler() {
		String name = "test_event";
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName(name);
		when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
		when(jedisProxyMock.hdel(anyString(), anyString())).thenReturn(1L);
		redisEventHandlerDAO.removeEventHandler(name);
		verify(jedisProxyMock, times(1)).hget(anyString(), anyString());
		verify(jedisProxyMock, times(1)).hdel(anyString(), anyString());
	}

	@Test(expected = NotFoundException.class)
	public void testRemoveEventHandlerNotFound() {
		String name = "missing_event";
		when(jedisProxyMock.hget(anyString(), anyString())).thenReturn(null);
		redisEventHandlerDAO.removeEventHandler(name);
	}

	@Test
	public void testGetAllEventHandlers() {
		when(jedisProxyMock.hgetAll(anyString())).thenReturn(null);
		List<EventHandler> handlers = redisEventHandlerDAO.getAllEventHandlers();
		assertEquals(handlers, new LinkedList<>());
		verify(jedisProxyMock, times(1)).hgetAll(anyString());
	}

	@Test
	public void testGetEventHandlersForEvent() {
		String event = "test_event";
		when(jedisProxyMock.smembers(anyString())).thenReturn(null);
		List<EventHandler> handlers = redisEventHandlerDAO.getEventHandlersForEvent(event, true);
		assertEquals(handlers, new LinkedList<>());
		verify(jedisProxyMock, times(1)).smembers(anyString());
	}

	@Test
	public void testGetEventHandler() {
		String name = "test_event";
		when(jedisProxyMock.hget(anyString(), anyString())).thenReturn("{}");
		assertNotNull(redisEventHandlerDAO.getEventHandler(name));
		verify(jedisProxyMock, times(1)).hget(anyString(), anyString());
	}

}