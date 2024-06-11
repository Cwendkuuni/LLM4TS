package com.netflix.conductor.redis.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.mockito.Mockito;


public class RedisEventHandlerDAOTest {

    @Test
    public void testAddEventHandler() {
        // Initialize mocks
        JedisProxy jedisProxy = Mockito.mock(JedisProxy.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        RedisProperties properties = Mockito.mock(RedisProperties.class);
        
        // Initialize RedisEventHandlerDAO object
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        // Initialize EventHandler Object
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("TestHandler");

        Mockito.when(jedisProxy.hset(Mockito.any(), Mockito.eq(eventHandler.getName()), Mockito.any()))
                .thenReturn(null);
        dao.addEventHandler(eventHandler);

        // Verify if the test case is working by a stub function
        Mockito.verify(jedisProxy).hset(Mockito.any(), Mockito.eq(eventHandler.getName()), Mockito.any());
    }

    @Test(expected = NotFoundException.class)
    public void testRemoveEventHandler() {
        // Initialize mocks
        JedisProxy jedisProxy = Mockito.mock(JedisProxy.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ConductorProperties conductorProperties = Mockito.mock(ConductorProperties.class);
        RedisProperties properties = Mockito.mock(RedisProperties.class);
        
        // Initialize RedisEventHandlerDAO object
        RedisEventHandlerDAO dao = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);

        String handlerName = "TestHandler";

        // As the event handler is not added yet, removing it should throw an exception
        dao.removeEventHandler(handlerName);
    }
}