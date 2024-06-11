package com.oceanbase.odc.service.onlineschemachange;
import org.flowable.engine.delegate.DelegateTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    // "Available in your class to be tested"
    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;
    @Mock
    private ScheduleService scheduleService;
    //... Mock all other dependencies

    // "Class to be tested"
    @InjectMocks
    private OnlineSchemaChangeFlowableTask service;
    
    @Before
    public void setup() {
        //setup your before configuration here
    }


    @Test
    public void testStart() throws Exception {
    // Test the start() method
    // Prepare mock objects, for instance DelegateExecution
    // Invoke the method, collect the result 
    // Assert the result, assert invoked  methods on mock objects
        
    }

    @Test
    public void testIsSuccessful() {
        // Test the isSuccessful() method
    }

    @Test
    public void testIsFailure() {
        // Test the isFailure() method
    }

    @Test 
    public void testOnFailure() {
        // Test the onFailure() method
    }    
    
    // And so on for each method in your class
    
}
// 