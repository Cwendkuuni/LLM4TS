package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Test
    public void testGetCoreFlowableElementType() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertEquals(FlowableElementType.SERVICE_TASK,  instance.getCoreFlowableElementType());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        instance.setStatus(FlowNodeStatus.PENDING);
        assertThrows(UnsupportedOperationException.class, () -> instance.confirmExecute());
    }

    @Test
    public void testAbort() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        instance.setStatus(FlowNodeStatus.PENDING);
        assertThrows(UnsupportedOperationException.class, () -> instance.abort());
    }

    @Test
    public void testCancelTaskNotOnMachine() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertThrows(UnsupportedOperationException.class, () -> instance.cancel(true));
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertThrows(UnsupportedOperationException.class, () -> instance.isDone());
    }

    @Test
    public void testGetResult() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertThrows(UnsupportedOperationException.class, () -> instance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertThrows(UnsupportedOperationException.class, () -> instance.getResult(1L, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), instance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        FlowTaskInstance instance = buildFlowTaskInstance();
        assertNotEquals(null, instance.getTargetDelegateClass());
    }

    private FlowTaskInstance buildFlowTaskInstance() {
        // Add code to initiate a FlowTaskInstance instance
    }
}