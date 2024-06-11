
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;

import org.flowable.task.api.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private BaseRuntimeFlowableDelegate<?> mockTaskHandle;

    @Mock
    private Task mockTask;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @InjectMocks
    private ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL,60,1);

    @Test
    public void testBindServiceTask() {
        Mockito.when(mockTaskHandle.getTargetTaskInstanceId()).thenReturn(1L);
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        try {
            flowTaskInstance.confirmExecute();
        } catch (UnsupportedOperationException e) {
            assertEquals("Automatic tasks do not support this execution", e.getMessage());
        }
    }

    @Test
    public void testAbort() {
        try {
            flowTaskInstance.abort();
        } catch (UnsupportedOperationException e) {
            assertEquals("Automatic tasks do not support this execution", e.getMessage());
        }
    }

    @Test
    public void testCancel() {
        Mockito.when(mockTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        try {
            flowTaskInstance.cancel(true);
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
            assertEquals("Task isn't on this machine", e.getMessage());
        }
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        assertSame(mockTaskHandle, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        assertSame(mockTaskHandle, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> expectedClass = mockTaskHandle.getClass();
        flowTaskInstance.bindServiceTask(mockTaskHandle);
        assertEquals(expectedClass, flowTaskInstance.getTargetDelegateClass());
    }
}
