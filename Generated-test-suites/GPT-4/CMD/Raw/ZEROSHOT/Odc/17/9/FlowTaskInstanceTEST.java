
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import java.util.concurrent.ExecutionException;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import java.util.Collections;
import java.util.Optional;

public class FlowTaskInstanceTest {

    @InjectMocks
    FlowTaskInstance flowTaskInstance;

    @Mock
    TaskService taskService;

    @Mock
    Task task;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetTargetTaskId() {
        flowTaskInstance.setTargetTaskId(1L);
        assertEquals(1L, (long) flowTaskInstance.getTargetTaskId());
    }

    @Test
    public void testAllocDealloc() {
        flowTaskInstance.alloc();
        assertNotNull(flowTaskInstance);
        flowTaskInstance.dealloc();
        assertNull(flowTaskInstance);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.confirmExecute();
        assertNotEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.abort();
        assertNotEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.cancel(true);
        verify(targetTaskHandle, atLeastOnce()).cancel(true);
    }

    @Test(expected= UnsupportedOperationException.class)
    public void testCancelUnsupportedOperation() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testIsDone() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.get()).thenReturn("expectedResult");
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals("expectedResult", flowTaskInstance.getResult());
    }
    
    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.get(5000, TimeUnit.MILLISECONDS)).thenReturn("expectedResult");
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals("expectedResult", flowTaskInstance.getResult(5000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertNotNull(flowTaskInstance.getTargetDelegateClass());
    }
}
