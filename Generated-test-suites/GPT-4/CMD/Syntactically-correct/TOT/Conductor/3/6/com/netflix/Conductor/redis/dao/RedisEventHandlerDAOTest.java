package com.netflix.conductor.redis.dao;
import com.netflix.conductor.common.metadata.events.EventHandler;
import com.netflix.conductor.redis.jedis.JedisProxy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RedisEventHandlerDAOTest {

    private JedisProxy jedisProxy;
    private ObjectMapper objectMapper;
    private ConductorProperties conductorProperties;
    private RedisProperties properties;

    private EventHandlerDAO underTest;

    @Before
    public void setUp() {
        jedisProxy = Mockito.mock(JedisProxy.class);
        objectMapper = new ObjectMapper();
        conductorProperties = new ConductorProperties();
        properties = new RedisProperties();
        underTest = new RedisEventHandlerDAO(jedisProxy, objectMapper, conductorProperties, properties);
    }

    @Test(expected = NullPointerException.class)
    public void addEventHandlerShouldThrowNullPointerException() {
        underTest.addEventHandler(null);
    }

    @Test
    public void addEventHandlerShouldSuccessfullyAddEventHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        underTest.addEventHandler(eventHandler);

        Mockito.verify(jedisProxy).hset(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }

    @Test(expected = NotFoundException.class)
    public void updateEventHandlerShouldThrowNotFoundExceptionForNonExistingHandler() {
        EventHandler eventHandler = new EventHandler();
        eventHandler.setName("testName");
        underTest.updateEventHandler(eventHandler);
    }

    @Test(expected = NotFoundException.class)
    public void removeEventHandlerShouldThrowNotFoundExceptionForNonExistingHandler() {
        underTest.removeEventHandler("testName");
    }

    @Test
    public void getAllEventHandlersShouldSuccessfullyRetrieveAllHandlers() {
        underTest.getAllEventHandlers();
        Mockito.verify(jedisProxy).hgetAll(Mockito.anyString());
    }

    @Test
    public void getEventHandlersForEventShouldSuccessfullyReturnRelevantHandlers() {
        underTest.getEventHandlersForEvent("testEvent", true);
        Mockito.verify(jedisProxy).smembers(Mockito.anyString());
    }
}