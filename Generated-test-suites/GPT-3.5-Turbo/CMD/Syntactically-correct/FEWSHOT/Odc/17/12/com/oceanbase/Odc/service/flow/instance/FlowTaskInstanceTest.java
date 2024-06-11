import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;
    private RuntimeDelegateMapper delegateConvertor;
    
    @Before
    public void setUp() {
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance = new FlowTaskInstance(TaskType.TASK, 1L, 1L, new ExecutionStrategyConfig(), true, false, delegateConvertor, null, null, null, null, null, null);
    }

    @Test
    public void testBindServiceTask() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals(targetTaskHandle, flowTaskInstance.getTargetTaskHandle());
        verify(targetTaskHandle).setTargetTaskInstanceId(flowTaskInstance.getId());
        verify(targetTaskHandle).setFlowNodeInstance(flowTaskInstance);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.confirmExecute();
        verify(targetTaskHandle, times(1)).execute();
    }

    @Test
    public void testAbort() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.abort();
        verify(targetTaskHandle, times(1)).abort();
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.cancel(false);
        verify(targetTaskHandle, times(1)).cancel(false);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        try {
            flowTaskInstance.isDone();
        } catch (Exception e) {
            // do nothing
        }
        verify(targetTaskHandle, times(1)).isDone();
    }

    @Test
    public void testGetResult() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        try {
            flowTaskInstance.getResult();
        } catch (Exception e) {
            // do nothing
        }
        verify(targetTaskHandle, times(1)).get();
    }

    @Test
    public void testGetResultWithTimeout() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        try {
            flowTaskInstance.getResult(1L, TimeUnit.SECONDS);
        } catch (Exception e) {
            // do nothing
        }
        verify(targetTaskHandle, times(1)).get(1L, TimeUnit.SECONDS);
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }
}