
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    
    @Mock
    private TaskService taskService = mock(TaskService.class);

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance();
    }
    
    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testIsDone() {
        // Test when task is not present on this machine
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.isDone());
        
        // Mocking the target task handle
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        assertTrue(flowTaskInstance.isDone());
    }
    
    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Test when task is not present on this machine
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult());
        
        // Mocking the target task handle
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        assertNotNull(flowTaskInstance.getResult());
    }
    
    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Test when task is not present on this machine
        assertThrows(UnsupportedOperationException.class, () -> flowTaskInstance.getResult(1, TimeUnit.SECONDS));
        
        // Mocking the target task handle
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        
        assertNotNull(flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }
}
