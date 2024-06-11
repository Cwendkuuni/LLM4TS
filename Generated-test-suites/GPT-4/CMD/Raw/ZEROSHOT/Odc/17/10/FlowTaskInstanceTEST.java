
package com.oceanbase.odc.service.flow.instance;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetTargetTaskId() {
        Long taskId = 9999L;
        flowTaskInstance.setTargetTaskId(taskId);
        assertEquals(taskId, flowTaskInstance.getTargetTaskId());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowTaskInstance, times(1)).confirmExecute();
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowTaskInstance, times(1)).abort();
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.cancel(true);
        verify(flowTaskInstance, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.isDone();
        verify(flowTaskInstance, times(1)).isDone();
    }

    @Test(expected=InterruptedException.class)
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.getResult();
    }

    @Test(expected=InterruptedException.class)
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(baseRuntimeFlowableDelegate);
        flowTaskInstance.getResult(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        flowTaskInstance.getTargetDelegateClass();
        verify(flowTaskInstance, times(1)).getTargetDelegateClass();
    }
}
