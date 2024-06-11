package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import org.flowable.engine.TaskService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FlowTaskInstanceTest {

    private TaskService taskService;
    private ServiceTaskInstanceRepository serviceTaskRepository;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        taskService = Mockito.mock(TaskService.class);
        serviceTaskRepository = Mockito.mock(ServiceTaskInstanceRepository.class);
        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        eventPublisher = Mockito.mock(EventPublisher.class);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(taskService, serviceTaskRepository, flowableAdaptor, eventPublisher);
    }

    @Test
    public void testCreateFlowTaskInstance() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(
                TaskType.TASK_TYPE_ONE, 1L, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        // When
        flowTaskInstance.create();

        // Then
        Mockito.verify(serviceTaskRepository).save(Mockito.any(ServiceTaskInstanceEntity.class));
        ServiceTaskInstanceEntity entity = Mockito.argThat(e -> e.getId() != null);
        Mockito.verify(flowableAdaptor).publishEvent(Mockito.eq(TaskInstanceCreatedEvent.class), Mockito.argThat(e -> e.getInstance().getId().equals(entity.getId())));
    }

    @Test
    public void testBindServiceTask() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        // When
        flowTaskInstance.bindServiceTask(target);

        // Then
        Mockito.verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);

        // When
        boolean isPresent = flowTaskInstance.isPresentOnThisMachine();

        // Then
        assertThat(isPresent).isTrue();
    }

    @Test
    public void testDealloc() {
        // Given
        BaseRuntimeFlowableDelegate<?> target = Mockito.mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);
        flowTaskInstance.bindServiceTask(target);

        // When
        flowTaskInstance.dealloc();

        // Then
        Mockito.verify(flowTaskInstance.listener).onEvent(Mockito.any(ServiceTaskStartedEvent.class));
        Mockito.verify(eventPublisher).removeEventListener(Mockito.eq(flowTaskInstance.listener));
        Mockito.verifyNoMoreInteractions(flowTaskInstance.listener);
    }

    @Test
    public void testAlloc() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        // When
        flowTaskInstance.alloc();

        // Then
        Mockito.verify(flowTaskInstance.listener).onEvent(Mockito.any(ServiceTaskStartedEvent.class));
        Mockito.verify(eventPublisher).addEventListener(Mockito.eq(flowTaskInstance.listener));
    }

    @Test
    public void testComplete() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        Mockito.when(flowableAdaptor.getFlowableElementByType(1L, FlowNodeType.USER_TASK.getId(), FlowableElementType.USER_TASK)).thenReturn(Collections.singletonList(Mockito.mock(FlowableElement.class)));
        Mockito.when(taskService.createTaskQuery().processInstanceId("pid").list()).thenReturn(Collections.emptyList());
        Mockito.when(taskService.complete(Mockito.anyLong(), Mockito.anyMap())).thenReturn(null);

        // When
        flowTaskInstance.complete(false);

        // Then
        Mockito.verify(flowableAdaptor).getFlowableElementByType(1L, FlowNodeType.USER_TASK.getId(), FlowableElementType.USER_TASK);
        Mockito.verify(taskService).createTaskQuery().processInstanceId("pid").list();
        Mockito.verify(taskService).complete(Mockito.anyLong(), Mockito.anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteWithAutomaticStrategy() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        // When & Then
        flowTaskInstance.complete(false);
    }

    @Test
    public void testResourceType() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        // When
        String resourceType = flowTaskInstance.resourceType();

        // Then
        assertThat(resourceType).isEqualTo(ResourceType.ODC_FLOW_TASK_INSTANCE.name());
    }

    @Test
    public void testGetTargetDelegateClass() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(1L, TaskType.TASK_TYPE_ONE, 1L, new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL), false, false,
                Mockito.mock(RuntimeDelegateMapper.class), Mockito.mock(FlowableAdaptor.class), eventPublisher, taskService,
                Mockito.mock(NodeInstanceEntityRepository.class), Mockito.mock(SequenceInstanceRepository.class),
                serviceTaskRepository);

        // When
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass = flowTaskInstance.getTargetDelegateClass();

        // Then
        Mockito.verify(flowTaskInstance.delegateConvertor).map(Mockito.eq(flowTaskInstance.taskType));
        assertThat(targetDelegateClass).isNotNull();
    }
}