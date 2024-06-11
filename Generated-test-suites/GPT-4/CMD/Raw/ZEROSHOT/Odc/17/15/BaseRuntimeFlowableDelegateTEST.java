
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import java.util.concurrent.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution mockExecution;
    @Mock
    private Callable<String> mockCallable;
    @Mock
    private EventPublisher mockEventPublisher;
    @Mock
    private FlowTaskInstance mockTaskInstance;
    @InjectMocks
    private TestableBaseRuntimeFlowableDelegate testableBaseRuntimeFlowableDelegate;
  
    private class TestableBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override protected Callable<String> initCallable(DelegateExecution execution) {
            return mockCallable;
        }
    }
  
    @Before
    public void setup() throws Exception {
        when(mockExecution.getCurrentActivityId()).thenReturn("activityId");
        when(mockCallable.call()).thenReturn("Successful Call");
        testableBaseRuntimeFlowableDelegate.init(mockEventPublisher);
    }

    @Test
    public void testRunSuccessful() throws Exception {
        testableBaseRuntimeFlowableDelegate.run(mockExecution);
        assertTrue(testableBaseRuntimeFlowableDelegate.isDone());
        assertEquals("Successful Call", testableBaseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunInitializingFailed() throws Exception {
        when(mockExecution.getCurrentActivityId()).thenThrow(new RuntimeException());
        testableBaseRuntimeFlowableDelegate.run(mockExecution);
    }

    @Test(expected = RuntimeException.class)
    public void testRunExecutionFailed() throws Exception {
        when(mockCallable.call()).thenThrow(new RuntimeException());
        testableBaseRuntimeFlowableDelegate.run(mockExecution);
    }
  
    @Test
    public void testFlowStatusUpdate() {
        testableBaseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.COMMITTED);
        // Check any verifying method for changes to flowStatus
    }

    @Test
    public void testTaskInstanceBinding() {
        when(mockTaskInstance.getId()).thenReturn(testableBaseRuntimeFlowableDelegate.getTargetTaskInstanceId());
        testableBaseRuntimeFlowableDelegate.bindToFlowTaskInstance(mockTaskInstance);
        // Check any verifying method for the instance binding
    }

    @Test
    public void testExecute() {
        testableBaseRuntimeFlowableDelegate.execute(mockExecution);
        verify(mockEventPublisher, times(1)).removeEventListener(any());
    }

    @Test(expected = ExecutionException.class)
    public void testGetThrowsExecutionException() throws Exception {
        when(mockCallable.call()).thenThrow(new RuntimeException());
        testableBaseRuntimeFlowableDelegate.run(mockExecution);
        testableBaseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimesOut() throws Exception {
        testableBaseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }
}
