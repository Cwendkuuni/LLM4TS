import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        TaskService taskServiceMock = mock(TaskService.class);
        flowTaskInstance = new FlowTaskInstance(taskServiceMock);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        
        // verify the task is completed successfully
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        
        // verify the task is aborted successfully
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        flowTaskInstance.cancel(false);
        
        // verify the task is cancelled successfully
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        boolean isDone = flowTaskInstance.isDone();
        
        assertEquals(true, isDone);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        Object result = flowTaskInstance.getResult();
        
        // verify the result
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTaskHandle.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        Object result = flowTaskInstance.getResult(5, TimeUnit.SECONDS);
        
        // verify the result
    }
}