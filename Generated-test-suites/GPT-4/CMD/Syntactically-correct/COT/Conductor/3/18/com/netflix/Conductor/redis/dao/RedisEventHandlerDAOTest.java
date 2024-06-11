package com.netflix.conductor.redis.dao;
import static org.junit.jupiter.api.Assertions.assertThrows;
 
public class RedisEventHandlerDAOTest {
 
    RedisEventHandlerDAO redisDAO;

    @Mock
    JedisProxy jedisProxy;

    @Before
    public void init() {
        redisDAO = new RedisEventHandlerDAO(jedisProxy, new ObjectMapper(), new ConductorProperties(), new RedisProperties());
    }
   
    @Test
    public void testAddEventHandler_nullName() {
        EventHandler e = new EventHandler();
        assertTrue(assertThrows(IllegalArgumentException.class, () -> redisDAO.addEventHandler(e)) instanceof IllegalArgumentException);
    }

    @Test
    public void testUpdateEventHandler_nullName() {
        EventHandler e = new EventHandler();
        assertTrue(assertThrows(IllegalArgumentException.class, () -> redisDAO.updateEventHandler(e)) instanceof IllegalArgumentException);
    }

    @Test
    public void testRemoveEventHandler_nonExistentHandler() {
        String name = "non_existent_handler";
        assertTrue(assertThrows(NotFoundException.class, () -> redisDAO.removeEventHandler(name)) instanceof NotFoundException);
    }

    @Test
    public void testGetEventHandlersForEvent_nonExistentEvent() {
        String event = "non_existent_event";
        boolean active = true;
        assertTrue(assertThrows(NotFoundException.class, () -> redisDAO.getEventHandlersForEvent(event, active)) instanceof NotFoundException);
    }
  
}