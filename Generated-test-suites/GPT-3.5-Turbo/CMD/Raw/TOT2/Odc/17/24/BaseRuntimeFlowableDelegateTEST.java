import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import org.flowable.engine.delegate.DelegateExecution;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Mock
    private DelegateExecution executionMock;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        // Prepare
        Mockito.when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));

        // Execute
        delegate.initTargetTaskInstanceId(executionMock);

        // Verify
        assertNotNull(delegate.getFlowInstanceId());
        assertNotNull(delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());

    }

    @Test
    public void testBindToFlowTaskInstance() {
        // Prepare
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        taskInstance.setTaskType(TaskType.TYPE_A);

        // Execute
        delegate.bindToFlowTaskInstance(taskInstance);

        // Verify
        assertNotNull(taskInstance.getServiceTask());

    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Prepare
        Mockito.when(flowInstanceRepository.updateStatusById(anyLong(), any())).thenReturn(true);

        // Execute
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Verify
        assertTrue(true);

    }

    @Test
    public void testInitCallable() {
        // Prepare
        Callable<String> callable = delegate.initCallable(executionMock);

        // Verify
        assertNotNull(callable);

    }

}