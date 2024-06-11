package com.netflix.conductor.redis.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RedisEventHandlerDAOTest {

	@Mock
	private JedisProxy jedisProxy;

	private RedisEventHandlerDAO redisEventHandlerDAO;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		redisEventHandlerDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(),
        new ConductorProperties(), new RedisProperties());
	} 

	@Test(expected = ConflictException.class)
	public void addEventHandler_existingHandler_test() {
		String handlerName = "existingHandler";
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName(handlerName);

        when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn("{\"name\":\"existingHandler\"}");

		redisEventHandlerDAO.addEventHandler(eventHandler);
	}

	@Test
	public void addEventHandler_newHandler_test() {
		String handlerName = "newHandler";
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName(handlerName);

        when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn(null);

		redisEventHandlerDAO.addEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
	}

	@Test(expected = NotFoundException.class)
	public void updateEventHandler_notFound_test() {
		String handlerName = "notFoundHandler";
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName(handlerName);

		when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn(null);

		redisEventHandlerDAO.updateEventHandler(eventHandler);
	}

	@Test
	public void updateEventHandler_foundHandler_test() {
		String handlerName = "existingHandler";
		EventHandler eventHandler = new EventHandler();
		eventHandler.setName(handlerName);

		when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn("{\"name\":\"existingHandler\"}");

		redisEventHandlerDAO.updateEventHandler(eventHandler);
        verify(jedisProxy, times(1)).hset(anyString(), eq(eventHandler.getName()), anyString());
	}

	@Test(expected = NotFoundException.class)
	public void removeEventHandler_notFound_test() {
		String handlerName = "notFoundHandler";

		when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn(null);

		redisEventHandlerDAO.removeEventHandler(handlerName);
	}

	@Test
	public void removeEventHandler_foundHandler_test() {
		String handlerName = "existingHandler";

		when(jedisProxy.hget(anyString(), eq(handlerName))).thenReturn("{\"name\":\"existingHandler\"}");

		redisEventHandlerDAO.removeEventHandler(handlerName);
        verify(jedisProxy, times(1)).hdel(anyString(), eq(handlerName));
	}

	@Test
	public void getAllEventHandlers_test() {
        when(jedisProxy.hgetAll(anyString())).thenReturn(new HashMap<String, String>() {
            {
                put("handler1", "{\"name\":\"handler1\"}");
                put("handler2", "{\"name\":\"handler2\"}");
            }
        });

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getAllEventHandlers();

        assertEquals(2, eventHandlers.size());
        assertEquals("handler1", eventHandlers.get(0).getName());
        assertEquals("handler2", eventHandlers.get(1).getName());
	}

	@Test
	public void getEventHandlersForEvent_test() {
        String eventName = "event1";
		
        Set<String> eventNameSet = new HashSet<>();
        eventNameSet.add("handler1");
        eventNameSet.add("handler2");

        when(jedisProxy.smembers(anyString())).thenReturn(eventNameSet);
        when(jedisProxy.hget(anyString(), eq("handler1"))).thenReturn("{\"name\":\"handler1\"}");
        when(jedisProxy.hget(anyString(), eq("handler2"))).thenReturn("{\"name\":\"handler2\"}");

        List<EventHandler> eventHandlers = redisEventHandlerDAO.getEventHandlersForEvent(eventName, false);

        assertEquals(2, eventHandlers.size());
        assertEquals("handler1", eventHandlers.get(0).getName());
        assertEquals("handler2", eventHandlers.get(1).getName());
	}
}
