package com.oceanbase.odc.service.flow.task;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BaseRuntimeFlowableDelegateTest {
    @Test
    public void testBaseRuntimeFlowableDelegateConstructor() {
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        assertNotNull(delegate);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        assertFalse(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithParameters() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test(expected=InvocationTargetException.class)
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        delegate.initTargetTaskInstanceId(execution);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        delegate.bindToFlowTaskInstance(taskInstance);
    }
 
    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus status = mock(FlowStatus.class);
        BaseRuntimeFlowableDelegate delegate = new BaseRuntimeFlowableDelegate();
        delegate.updateFlowInstanceStatus(status);
    }
}