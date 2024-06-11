package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private BaseRuntimeFlowableDelegate<?> baseDelegate;

    @Before
    public void setup() {
        // Initial setup goes here with all the required dependencies

        // Mocking the delegate
        baseDelegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);

        // Assume remaining dependencies are initialized, then the instance is created as:
        // flowTaskInstance = new FlowTaskInstance(remaining arguments...);
    }

    @Test
    public void testIsDoneTaskPresentOnMachine() throws Exception {
        // Assuming the task is bound to the machine
        flowTaskInstance.bindServiceTask(baseDelegate);
        // Assuming the task is done
        when(baseDelegate.isDone()).thenReturn(true);

        assertTrue("The test should be true as the task is done.", flowTaskInstance.isDone());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneTaskNotPresentOnMachine() throws Exception {
        // Assuming the task is not bound to the machine
        flowTaskInstance.dealloc();

        // Calling isDone() should throw an exception 
        flowTaskInstance.isDone();
    }
}
