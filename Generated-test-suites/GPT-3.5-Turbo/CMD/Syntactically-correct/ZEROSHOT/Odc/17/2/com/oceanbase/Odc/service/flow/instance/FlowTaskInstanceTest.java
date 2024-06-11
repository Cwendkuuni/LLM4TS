import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository mockServiceTaskInstanceRepository;
    private ServiceTaskInstanceEntity mockServiceTaskInstanceEntity;
    private RuntimeDelegateMapper mockRuntimeDelegateMapper;
    private FlowableAdaptor mockFlowableAdaptor;
    private EventPublisher mockEventPublisher;
    private TaskService mockTaskService;
    private NodeInstanceEntityRepository mockNodeRepository;
    private SequenceInstanceRepository mockSequenceRepository;

    @Before
    public void setUp() {
        mockServiceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        mockServiceTaskInstanceEntity = mock(ServiceTaskInstanceEntity.class);
        mockRuntimeDelegateMapper = mock(RuntimeDelegateMapper.class);
        mockFlowableAdaptor = mock(FlowableAdaptor.class);
        mockEventPublisher = mock(EventPublisher.class);
        mockTaskService = mock(TaskService.class);
        mockNodeRepository = mock(NodeInstanceEntityRepository.class);
        mockSequenceRepository = mock(SequenceInstanceRepository.class);

        flowTaskInstance = new FlowTaskInstance(taskType, organizationId, flowInstanceId, strategyConfig, startEndpoint,
            endEndPoint, mockRuntimeDelegateMapper, mockFlowableAdaptor, mockEventPublisher, mockTaskService,
            mockNodeRepository, mockSequenceRepository, mockServiceTaskInstanceRepository);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        verify(mockServiceTaskInstanceRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.update();
        verify(mockServiceTaskInstanceRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testComplete() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.confirmExecute();
        verify(mockTaskService, times(1)).complete(anyString(), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.abort();
        verify(mockTaskService, times(1)).complete(anyString(), anyMap());
    }

    @Test
    public void testCancel() {
        flowTaskInstance.bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
        flowTaskInstance.cancel(false);
        verify(flowTaskInstance.getTargetTaskHandle(), times(1)).cancel(false);
    }

    @Test
    public void testIsDone() {
        flowTaskInstance.bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskHandle().isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        flowTaskInstance.bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskHandle().get()).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.bindServiceTask(any(BaseRuntimeFlowableDelegate.class));
        when(flowTaskInstance.getTargetTaskHandle().get(anyLong(), any(TimeUnit.class))).thenReturn("Result");
        assertEquals("Result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }
}