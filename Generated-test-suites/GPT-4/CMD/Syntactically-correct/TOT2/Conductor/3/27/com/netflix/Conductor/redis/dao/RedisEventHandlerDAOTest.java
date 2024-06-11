package com.netflix.conductor.redis.dao;
import static org.junit.Assert.*; 
import org.junit.Before; 
import org.junit.After; 
import org.junit.Test; 

public class RedisEventHandlerDAOTest { 
  
    private RedisEventHandlerDAO dao; 
  
    @Before
    public void setUp() throws Exception { 
        //... create instance of RedisEventHandlerDAO, set up mock objects 
    } 

    @Test
    public void testAddEventHandler() { 
        EventHandler eh = new EventHandler();
        // ... set details for eh instance 
        dao.addEventHandler(eh); 
        // ... verify that eh was correctly added, possibly by calling get method 
    }
    //
    // Similar tests for other public methods like updateEventHandler, removeEventHandler, getAllEventHandlers, getEventHandlersForEvent
    //
  
    @After
    public void tearDown() throws Exception { 
        //...clean-up activities post test execution, like deleting test data from database
    } 

}