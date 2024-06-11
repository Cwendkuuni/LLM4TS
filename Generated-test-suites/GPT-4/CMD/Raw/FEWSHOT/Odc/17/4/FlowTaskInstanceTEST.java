package com.oceanbase.odc.service.flow.instance;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.Optional;

import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {
    
    //Tested class
    private FlowTaskInstance flowTaskInstance;
    
    //Mocked Fields
    private BaseRuntimeFlowableDelegate targetTaskHandle;
    
    @Before
    public void setup() {
        //TODO: Mock required classes and fields, Initialize FlowTaskInstance instance.
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        //Mock the target task handle and it's getTargetTaskInstanceId method 
        BaseRuntimeFlowableDelegate mockFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        when(mockFlowableDelegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(mockFlowableDelegate);
        //Assert that task status has changed to executing
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testIsPresentOnThisMachine() {
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        when(targetTaskHandle.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        
        final String expected = "result";
        
        when(targetTaskHandle.get()).thenReturn(expected);
        assertEquals(expected, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        
        final String expected = "result";

        when(targetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenReturn(expected);
        assertEquals(expected, flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        flowTaskInstance.abort();
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
   }

}