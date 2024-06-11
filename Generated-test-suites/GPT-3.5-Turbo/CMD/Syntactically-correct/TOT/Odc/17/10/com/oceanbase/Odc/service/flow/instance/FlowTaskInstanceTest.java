import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private TaskService taskService;
    private Task task;

    @Before
    public void setUp() {
        taskService = mock(TaskService.class);
        task = mock(Task.class);
        
        flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setTaskService(taskService);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        
        // Verify task completion
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        
        // Verify task abortion
        assertEquals(FlowNodeStatus.ABORTED, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        when(task.getId()).thenReturn("testTaskId");
        when(flowTaskInstance.getTargetTaskHandle()).thenReturn(task);
        
        flowTaskInstance.cancel(true);
        
        // Verify task cancellation
        verify(task, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        when(flowTaskInstance.getTargetTaskHandle()).thenReturn(task);
        
        // Task is done by default when running this test
        boolean result = flowTaskInstance.isDone();
        
        // Verify if task is done
        assertEquals(true, result);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        Object expectedResult = "Test Result";
        
        when(flowTaskInstance.getTargetTaskHandle()).thenReturn(task);
        when(task.get()).thenReturn(expectedResult);
        
        // Get the result from the task and check if it matches the expected result
        Object result = flowTaskInstance.getResult();
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object expectedResult = "Test Result";
        
        when(flowTaskInstance.getTargetTaskHandle()).thenReturn(task);
        when(task.get(anyLong(), any(TimeUnit.class))).thenReturn(expectedResult);
        
        // Get the result from the task with timeout and check if it matches the expected result
        Object result = flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testResourceType() {
        String expectedResourceType = "ODC_FLOW_TASK_INSTANCE";
        
        // Check if the resource type matches the expected value
        assertEquals(expectedResourceType, flowTaskInstance.resourceType());
    }
}