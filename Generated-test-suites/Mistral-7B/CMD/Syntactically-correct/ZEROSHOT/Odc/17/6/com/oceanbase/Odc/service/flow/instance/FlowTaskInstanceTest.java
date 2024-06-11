package com.oceanbase.odc.service.flow.instance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowableExecutionStrategy;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

class FlowTaskInstanceTest {

    private static final String TASK_TYPE = "TASK_TYPE";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long ORGANIZATION_ID = 1L;
    private static final Long ID = 1L;
    private static final Long TARGET_TASK_ID = 2L;
    private static final FlowNodeStatus INITIAL_STATUS = FlowNodeStatus.PENDING;
    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;
    private static final FlowNodeStatus FINAL_STATUS = FlowNodeStatus.EXECUTED;
    private static final FlowableExecutionStrategy STRATEGY = FlowTaskExecutionStrategy.MANUAL;

    private ServiceTaskInstanceRepository serviceTaskRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private RuntimeDelegateMapper delegateConvertor;
    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;
    private ServiceTaskInstanceEntity taskInstanceEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        delegateConvertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        nodeInstanceRepository = mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
    }

    @Test
    public void testCreateWithoutTargetTaskId() {
        // Arrange
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(true);

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);
         // Assert
         Assertions.assertThrows(IllegalArgumentException.class, () -> instance.create());
    }

    @Test
    public void testCreateWithTargetTaskId() {
        // Arrange
        ServiceTaskInstanceEntity expectedEntity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class)))
                .thenReturn(expectedEntity);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class)))
                .thenReturn(true);

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);
        instance.setTargetTaskId(TARGET_TASK_ID);
        instance.create();

        // Assert
        verify(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));
        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdateWhenNotExist() {
        // Arrange

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);

        // Assert
        Assertions.assertThrows(IllegalStateException.class, () -> instance.update());
    }

    @Test
    public void testUpdateWhenExist() {
        // Arrange
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(ID);
        entity.setStatus(INITIAL_STATUS);
        when(serviceTaskRepository.findById(ID)).thenReturn(Optional.of(entity));

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);
        instance.setId(ID);
        instance.update();

        // Assert
        verify(serviceTaskRepository).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDeleteWhenNotExist() {
        // Arrange

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);

        // Assert
        Assertions.assertThrows(IllegalStateException.class, () -> instance.doDelete(ID));
    }

    @Test
    public void testDeleteWhenExist() {
        // Arrange
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(ID);
        when(serviceTaskRepository.findById(ID)).thenReturn(Optional.of(entity));

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);
        instance.setId(ID);
        instance.doDelete(ID);

        // Assert
        verify(serviceTaskRepository).deleteById(ID);
    }

    @Test
    public void testBindServiceTask() {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        // Act
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);
        instance.bindServiceTask(target);

        // Assert
        verify(instance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testConfirmExecute() {
        // Arrange
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);

        // Act
        instance.confirmExecute();

        // Assert
        Assertions.assertThrows(UnsupportedOperationException.class, () -> instance.complete(false));
    }

    @Test
    public void testAbort() {
        // Arrange
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);

        // Act
        instance.abort();

        // Assert
        Assertions.assertThrows(UnsupportedOperationException.class, () -> instance.complete(false));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(ID);
        instance.bindServiceTask(target);
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(mock(Task.class)));

        // Act
        instance.cancel(true);

        // Assert
        verify(target).cancel(true);
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(ID);
        instance.bindServiceTask(target);
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(mock(Task.class)));
        when(target.get()).thenReturn("result");

        // Act
        String result = instance.getResult();

        // Assert
        assertThat(result, is("result"));
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Arrange
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getId()).thenReturn(ID);
        instance.bindServiceTask(target);
        when(taskService.createTaskQuery().processInstanceId(anyString()).list())
                .thenReturn(Collections.singletonList(mock(Task.class)));
        when(target.get(anyLong(), any(TimeUnit.class))).thenThrow(new TimeoutException());

        // Act & Assert
        Assertions.assertThrows(TimeoutException.class, () -> instance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testComplete() {
        // Arrange
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);
        List<FlowableElement> elements = Collections.singletonList(mock(FlowableElement.class));
        List<Task> tasks = Collections.singletonList(mock(Task.class));
        when(flowableAdaptor.getFlowableElementByType(anyLong(), any(FlowNodeType.class),
                        any(FlowableElementType.class))).thenReturn(elements);
        when(taskService.createTaskQuery().processInstanceId(anyString())).thenReturn(tasks.stream()
                .map(Task::getId).collect(Collectors.toList()));

        // Act
        instance.complete(false);

        // Assert
        verify(elements.get(0)).getName();
        verify(tasks.get(0)).complete(anyMapOf(String.class, Object.class));
    }

    @Test
    public void testMapToTaskEntity() {
        // Arrange
        FlowTaskInstance instance = new FlowTaskInstance(TASK_TYPE, ORGANIZATION_ID, FLOW_INSTANCE_ID,
                new ExecutionStrategyConfig(STRATEGY), true, true, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeInstanceRepository, sequenceInstanceRepository,
                serviceTaskRepository);

        // Act
        ServiceTaskInstanceEntity expectedEntity = mapToTaskEntity(instance);

        // Assert
        assertThat(expectedEntity, is(notNullValue()));
        assertThat(expectedEntity.getOrganizationId(), is(ORGANIZATION_ID));
        assertThat(expectedEntity.getStatus(), is(INITIAL_STATUS));
        assertThat(expectedEntity.getFlowInstanceId(), is(FLOW_INSTANCE_ID));
        assertThat(expectedEntity.getStartEndpoint(), is(true));
        assertThat(expectedEntity.getEndEndpoint(), is(true));
        assertThat(expectedEntity.getStrategy(), is(STRATEGY.name()));
        assertThat(expectedEntity.getTaskType(), is(TASK_TYPE));
        assertThat(expectedEntity.getTargetTaskId(), is(nullValue()));
        assertThat(expectedEntity.getWaitExecExpireIntervalSeconds(), is(0L));
        assertThat(expectedEntity.getExecutionTime(), is(0L));
    }

    private static Map<String, Object> anyMapOf(Class<?>... keyTypes) {
        return new HashMap<>();
    }

    private static ServiceTaskInstanceEntity mapToTaskEntity(FlowTaskInstance instance) {
        return new FlowTaskInstanceTest().new FlowTaskInstanceTest_MapToTaskEntity()
                .mapToTaskEntity(instance);
    }

    public static class FlowTaskInstanceTest_MapToTaskEntity {

        public ServiceTaskInstanceEntity mapToTaskEntity(FlowTaskInstance instance) {
            ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
            entity.setOrganizationId(instance.getOrganizationId());
            entity.setStatus(instance.getStatus());
            entity.setFlowInstanceId(instance.getFlowInstanceId());
            entity.setStartEndpoint(instance.isStartEndpoint());
            entity.setEndEndpoint(instance.isEndEndPoint());
            entity.setStrategy(instance.strategyConfig.getStrategy());
            entity.setTaskType(instance.getTaskType());
            entity.setTargetTaskId(instance.getTargetTaskId());
            entity.setWaitExecExpireIntervalSeconds(instance.strategyConfig.getPendingExpireIntervalSeconds());
            entity.setExecutionTime(instance.strategyConfig.getExecutionTime());
            return entity;
        }

    }

}