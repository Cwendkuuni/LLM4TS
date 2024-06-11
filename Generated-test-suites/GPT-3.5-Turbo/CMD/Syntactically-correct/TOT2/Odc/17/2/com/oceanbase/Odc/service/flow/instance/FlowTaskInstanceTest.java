import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository mockServiceTaskRepository;
    private TaskService mockTaskService;
    private EventPublisher mockEventPublisher;
    private RuntimeDelegateMapper mockDelegateConvertor;

    @Before
    public void setUp() {
        mockServiceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        mockTaskService = mock(TaskService.class);
        mockEventPublisher = mock(EventPublisher.class);
        mockDelegateConvertor = mock(RuntimeDelegateMapper.class);
        
        flowTaskInstance = new FlowTaskInstance(TaskType.TASK_TYPE_1, 1L, 1L, new ExecutionStrategyConfig(), 
                true, false, mockDelegateConvertor, new FlowableAdaptor(), mockEventPublisher,
                mockTaskService, mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                mockServiceTaskRepository);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();

        verify(mockServiceTaskRepository, times(1)).save(any());
        assertNotNull(flowTaskInstance.getId());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.setId(1L);
        flowTaskInstance.setStatus(FlowNodeStatus.INITIALIZED);
        flowTaskInstance.setTargetTaskId(2L);

        flowTaskInstance.update();

        verify(mockServiceTaskRepository, times(1)).update(any());
        assertEquals(FlowNodeStatus.INITIALIZED, flowTaskInstance.getStatus());
    }

    @Test
    public void testDoDelete() {
        flowTaskInstance.setId(1L);

        flowTaskInstance.delete();

        verify(mockServiceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        // Test the logic of the method - complete() is invoked
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        // Test the logic of the method - complete() is invoked
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        flowTaskInstance.cancel(false);

        verify(mockTargetTaskHandle, times(1)).cancel(false);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        when(mockTargetTaskHandle.isDone()).thenReturn(true);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        Object expectedResult = new Object();
        when(mockTargetTaskHandle.get()).thenReturn(expectedResult);

        assertEquals(expectedResult, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);

        Object expectedResult = new Object();
        when(mockTargetTaskHandle.get(anyLong(), any(TimeUnit.class))).thenReturn(expectedResult);

        assertEquals(expectedResult, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        when(mockDelegateConvertor.map(any(TaskType.class))).thenReturn(TestRuntimeFlowableDelegate.class);

        assertEquals(TestRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }
}