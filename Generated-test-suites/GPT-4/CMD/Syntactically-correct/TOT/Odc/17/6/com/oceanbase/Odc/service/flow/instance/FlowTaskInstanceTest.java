package com.oceanbase.odc.service.flow.instance;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {
    private FlowTaskInstance instance;
    private ServiceTaskInstanceRepository mockServiceTaskInstanceRepository;
    private TaskService mockTaskService;
    // assuming those fields exist based on the constructors
    // add more mock variables as needed

    @Before
    public void setUp() {
        // Initialize the mock objects
        mockServiceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        mockTaskService = mock(TaskService.class);
        // ...
        // Initialize the object to be tested with the mock objects
        instance = new FlowTaskInstance(/*fill in parameters*/);
        // assuming such constructor exists 
    }

    @Test
    public void testCreate() {
        // Preparations, if any

        // Call method to be tested
        instance.create();
        
        // Test assertions and/or verify how mocks are called        
    }

    @Test
    public void testUpdate() {
        // Preparations, if any

        // Call method to be tested
        instance.update();
        
        // Test assertions and/or verify how mocks are called
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneForNotPresentTask() {
        // Assuming the method throws UnsupportedOperationException when the task is not on the machine

        // Call method to be tested
        instance.isDone();
    }

    // More tests for other methods, edge cases and error scenarios...

}