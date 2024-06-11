package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeImpl;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Unit test for {@link FlowTaskInstance} class
 */
public class FlowTaskInstanceTest {

    private static final String FLOWABLE_ENGINE_NAME = "flowableEngine";
    private static final String FLOWABLE_ADAPTOR_NAME = "flowableAdaptor";
    private static final String TASK_SERVICE_NAME = "taskService";
    private static final String EVENT_PUBLISHER_NAME = "eventPublisher";
    private static final String NODE_INSTANCE_REPOSITORY_NAME = "nodeInstanceRepository";
    private static final String SEQUENCE_INSTANCE_REPOSITORY_NAME = "sequenceInstanceRepository";
    private static final String SERVICE_TASK_INSTANCE_REPOSITORY_NAME = "serviceTaskInstanceRepository";

    private ServiceTaskInstanceRepository serviceTaskInstanceRepository;
    private TaskService taskService;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;
    private SequenceInstanceRepository sequenceInstanceRepository;

    @Before
    public void setup() {
        serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        taskService = mock(TaskService.class);
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        nodeInstanceEntityRepository = mock(NodeInstanceEntityRepository.class);
        sequenceInstanceRepository = mock(SequenceInstanceRepository.class);
    }

    @Test
    public void testFlowTaskInstanceCreation() {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

        // When
        flowTaskInstance.create();

        // Then
        verify(serviceTaskInstanceRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testFlowTaskInstanceBatchCreation() {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1);
        List<FlowTaskInstance> flowTaskInstances = Collections.singletonList(
                new FlowTaskInstance(strategyConfig, false, false, flowableAdaptor, eventPublisher, taskService,
                        nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository));

        // When
        List<FlowTaskInstance> createdFlowTaskInstances = FlowTaskInstance.batchCreate(flowTaskInstances,
                serviceTaskInstanceRepository);

        // Then
        verify(serviceTaskInstanceRepository, times(1)).batchCreate(anyList());
        assertEquals(1, createdFlowTaskInstances.size());
    }

    @Test
    public void testFlowTaskInstanceBinding() {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1);
        BaseRuntimeFlowableDelegate<?> targetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(strategyConfig, false, false, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

        // When
        flowTaskInstance.bindServiceTask(targetTaskHandle);

        // Then
        assertEquals(targetTaskHandle, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testFlowTaskInstanceDealloc() {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1);
        FlowTaskStartedListener listener = mock(FlowTaskStartedListener.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(strategyConfig, false, false, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        flowTaskInstance.alloc();

        // When
        flowTaskInstance.dealloc();

        // Then
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testFlowTaskInstanceAlloc() {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1);
        FlowTaskStartedListener listener = mock(FlowTaskStartedListener.class);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(strategyConfig, false, false, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);

        // When
        flowTaskInstance.alloc();

        // Then
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testFlowTaskInstanceCompletion() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 1);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(strategyConfig, false, false, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        Task task = Mockito.mock(Task.class);

        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("testUserTask");
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK), any())).thenReturn(
                Collections.singletonList(new FlowableElement("testUserTask", "testUserTask")));

        // When
        flowTaskInstance.complete(false);

        // Then
        verify(taskService, times(1)).complete(task.getId(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFlowTaskInstanceCompletionWithAutomaticStrategy() throws InterruptedException, ExecutionException,
            TimeoutException {
        // Given
        FlowNodeType flowNodeType = new FlowNodeTypeImpl("testFlowNodeType");
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO, 1);
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(strategyConfig, false, false, flowableAdaptor, eventPublisher,
                taskService, nodeInstanceEntityRepository, sequenceInstanceRepository, serviceTaskInstanceRepository);
        Task task = Mockito.mock(Task.class);

        when(taskService.createTaskQuery().list()).thenReturn(Collections.singletonList(task));
        when(task.getName()).thenReturn("testUserTask");
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(FlowableElementType.USER_TASK), any())).thenReturn(
                Collections.singletonList(new FlowableElement("testUserTask", "testUserTask")));

        // When & Then
        flowTaskInstance.complete(false);
    }
}