import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flowTaskInstance = new FlowTaskInstance(new TaskType("taskType"), 1L, 1L, null, false, false,
                mock(RuntimeDelegateMapper.class), mock(FlowableAdaptor.class), mock(EventPublisher.class),
                mock(TaskService.class), mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class),
                mock(ServiceTaskInstanceRepository.class));
        OngoingStubbing ongoingStubbing = mock(TaskService.class).createTaskQuery();
        when(ongoingStubbing.list()).thenReturn(Collections.emptyList());
    }

    @After
    public void tearDown() {
        flowTaskInstance = null;
    }

    @Test
    public void testCreateFlowTaskInstanceSuccess() {
        // Given
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);

        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.singletonList(new FlowableElement()));

        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(mock(ServiceTaskInstanceEntity.class));

        // When
        flowTaskInstance.create();

        // Then
        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
        verify(serviceTaskInstanceRepository).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFlowTaskInstanceWithEmptyName() {
        // Given
        TaskService taskService = mock(TaskService.class);
        NodeInstanceEntityRepository nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        SequenceInstanceRepository sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);

        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class), any(FlowableElementType.class)))
                .thenReturn(Collections.emptyList());

        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(serviceTaskInstanceRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(mock(ServiceTaskInstanceEntity.class));

        // When & Then
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(null, null, null, null, false, false, null, null, null, null, null, null, null);
        flowTaskInstance.create();
    }

    @Test
    public void testBindServiceTaskSuccess() {
        // Given
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        // When
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        // Then
        verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test(expected = IllegalStateException.class)
    public void testBindServiceTaskWithDifferentInstanceIds() {
        // Given
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        // When & Then
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        Verify.verify(flowTaskInstance.getId(), "!=", targetTaskHandle.getTargetTaskInstanceId());
    }

    @Test
    public void testCompleteSuccess() {
        // Given
        List<FlowableElement> elements = Collections.singletonList(new FlowableElement());
        String processInstanceId = "processInstanceId";

        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class), any(FlowableElementType.class)))
                .thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(processInstanceId));

        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));

        // When
        flowTaskInstance.complete(false);

        // Then
        verify(taskService).createTaskQuery();
        elements.forEach(e -> verify(taskService).complete(anyLong(), anyMap()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithAutomaticTask() {
        // Given
        List<FlowableElement> elements = Collections.singletonList(new FlowableElement());
        String processInstanceId = "processInstanceId";

        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowableElementType.class), any(FlowableElementType.class)))
                .thenReturn(elements);
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(processInstanceId));

        when(flowTaskInstance.strategyConfig.getStrategy()).thenReturn(FlowTaskExecutionStrategy.AUTO);

        // When & Then
        flowTaskInstance.complete(false);
    }

    @Test
    public void testCancelSuccess() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);

        when(targetTaskHandle.isDone()).thenReturn(false);

        // When
        flowTaskInstance.cancel(true);

        // Then
        verify(targetTaskHandle).cancel(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWithNoTaskHandle() {
        // Given

        // When & Then
        flowTaskInstance.cancel(true);
    }
}