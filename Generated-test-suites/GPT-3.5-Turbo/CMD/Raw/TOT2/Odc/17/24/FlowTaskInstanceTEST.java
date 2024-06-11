import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_A, 1L, 1L, new ExecutionStrategyConfig(), true, false,
                delegateConvertor, mock(FlowableAdaptor.class), eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testBatchCreate() {
        FlowTaskInstance instance = new FlowTaskInstance(TaskType.TYPE_B, 2L, 2L, new ExecutionStrategyConfig(), false, true,
                delegateConvertor, mock(FlowableAdaptor.class), eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);

        instance.create();

        List<FlowTaskInstance> instances = Arrays.asList(flowTaskInstance, instance);
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);

        assertNotNull(createdInstances);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
        assertEquals(delegate, flowTaskInstance.getTargetTaskHandle());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWhenNotPresentOnMachine() {
        flowTaskInstance.cancel(true);
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        // Verify the task has been aborted successfully
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(delegate);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        when(delegate.get()).thenReturn("Result");
        flowTaskInstance.bindServiceTask(delegate);

        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> delegate = mock(BaseRuntimeFlowableDelegate.class);
        when(delegate.get(anyLong(), any(TimeUnit.class))).thenReturn("Result");
        flowTaskInstance.bindServiceTask(delegate);

        assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }
}