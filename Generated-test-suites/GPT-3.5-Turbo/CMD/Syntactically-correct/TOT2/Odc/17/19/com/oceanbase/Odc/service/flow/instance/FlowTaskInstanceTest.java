import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FlowTaskInstanceTest {

    @Mock
    private ServiceTaskInstanceRepository mockServiceTaskInstanceRepository;

    @Mock
    private TaskService mockTaskService;

    @Mock
    private EventPublisher mockEventPublisher;

    @Mock
    private RuntimeDelegateMapper mockRuntimeDelegateMapper;

    @Mock
    private FlowableAdaptor mockFlowableAdaptor;

    @Mock
    private NodeInstanceEntityRepository mockNodeRepository;

    @Mock
    private SequenceInstanceRepository mockSequenceRepository;

    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        flowTaskInstance = new FlowTaskInstance(TaskType.SOME_TYPE, 123L, 456L, new ExecutionStrategyConfig(), true, false,
                mockRuntimeDelegateMapper, mockFlowableAdaptor, mockEventPublisher, mockTaskService, mockNodeRepository,
                mockSequenceRepository, mockServiceTaskInstanceRepository);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();

        // Verify that the service task instance is created successfully
        verify(mockServiceTaskInstanceRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();

        // Verify that the service task instance is updated successfully
        verify(mockServiceTaskInstanceRepository, times(1)).update(any());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        flowTaskInstance.abort();
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.cancel(true);

        // Verify that the task cancellation is performed successfully
        verify(flowTaskInstance.getTargetTaskDelegate(), times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskDelegate().isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        Object expectedResult = new Object();
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskDelegate().get()).thenReturn(expectedResult);

        assertEquals(expectedResult, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object expectedResult = new Object();
        long timeout = 1L;
        TimeUnit unit = TimeUnit.SECONDS;
        flowTaskInstance.bindServiceTask(mock(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskDelegate().get(timeout, unit)).thenReturn(expectedResult);

        assertEquals(expectedResult, flowTaskInstance.getResult(timeout, unit));
    }

}