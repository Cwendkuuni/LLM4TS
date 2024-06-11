
package com.oceanbase.odc.service.flow.instance;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {
    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private TaskService taskService;

    @Before
    public void setUp() {
        // Initialization of FlowTaskInstance or mocks if needed
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        assertNotNull(flowTaskInstance.getId());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        boolean result = flowTaskInstance.isPresentOnThisMachine();
        assertFalse(result);
    }

    @Test
    public void testComplete() {
        flowTaskInstance.create();
        flowTaskInstance.complete(false);
        assertTrue(flowTaskInstance.getStatus() == FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.create();
        flowTaskInstance.abort();
        assertTrue(flowTaskInstance.getStatus() == FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.create();
        flowTaskInstance.confirmExecute();
        assertTrue(flowTaskInstance.getStatus() == FlowNodeStatus.EXECUTING);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTaskInstance.cancel(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone() {
        flowTaskInstance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult(){
        try {
            flowTaskInstance.getResult();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertNotNull(flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void bindServiceTask() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        when(delegate.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(delegate);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }
}
