import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.junit.Test;

public class FlowTaskInstanceTest {

    TaskService taskService = mock(TaskService.class);

    @Test
    public void testBindServiceTask() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
        
        flowTaskInstance.bindServiceTask(target);
        
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.confirmExecute();
        
        assertFalse(flowTaskInstance.isDone());
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.abort();
        
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
        flowTaskInstance.cancel(false);
        
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
        Object result = flowTaskInstance.getResult();
        
        assertEquals(1, result);
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        flowTaskInstance.bindServiceTask(new BaseRuntimeFlowableDelegate<>());
        Object result = flowTaskInstance.getResult(1, TimeUnit.SECONDS);
        
        assertEquals(1, result);
    }

    @Test
    public void testResourceType() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }
}