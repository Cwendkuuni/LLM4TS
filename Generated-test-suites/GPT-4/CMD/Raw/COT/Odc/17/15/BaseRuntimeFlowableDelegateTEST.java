
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate;
    DelegateExecution delegateExecution;
    FlowTaskInstance taskInstance;

    @Before
    public void SetUp() {
        baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class);
        delegateExecution = mock(DelegateExecution.class);
        taskInstance = mock(FlowTaskInstance.class);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).execute(delegateExecution);
    }

    @Test
    public void testIsDone() {
        boolean doneStatus = baseRuntimeFlowableDelegate.isDone();
        assertEquals(false, doneStatus);
    }

    @Test(expected = InterruptedException.class)
    public void testGetWithException() throws Exception {
        when(baseRuntimeFlowableDelegate.get()).thenThrow(new InterruptedException());
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        when(baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());
        baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        verify(baseRuntimeFlowableDelegate, times(1)).initTargetTaskInstanceId(delegateExecution);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        when(taskInstance.getId()).thenReturn(Long.valueOf(1));
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        verify(baseRuntimeFlowableDelegate, times(1)).bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.DONE);
        verify(baseRuntimeFlowableDelegate, times(1)).updateFlowInstanceStatus(FlowStatus.DONE);
    }  
}
