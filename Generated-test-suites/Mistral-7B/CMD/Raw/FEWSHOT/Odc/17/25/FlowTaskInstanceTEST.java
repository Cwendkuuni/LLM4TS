package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.TaskEntity;
import org.flowable.engine.impl.persistence.entity.TaskInstanceEntity;
import org.flowable.engine.impl.persistence.entity.VariableInstanceEntity;
import org.flowable.engine.impl.test.AbstractTestProcessEngineRule;
import org.flowable.engine.impl.test.FlowableTestRule;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.flowable.task.service.TaskQuery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    @Rule
    public FlowableTestRule flowableTestRule = new FlowableTestRule();
    @Rule
    public AbstractTestProcessEngineRule engineRule = new AbstractTestProcessEngineRule(flowableTestRule);

    @Mock
    private RuntimeService runtimeService;
    @Mock
    private TaskService taskService;
    @Mock
    private NodeInstanceEntityRepository nodeRepository;
    @Mock
    private SequenceInstanceRepository sequenceRepository;
    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private RuntimeDelegateMapper delegateConvertor;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private ServiceTaskInstanceEntity entity;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    private long processInstanceId;
    private long flowInstanceId;
    private long flowableNodeId;
    private long taskId;

    @Before
    public void setup() {
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
        when(nodeRepository.findById(anyLong())).thenReturn(Optional.ofNullable(new NodeInstanceEntity()));
        when(sequenceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(new SequenceInstanceEntity()));
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(entity);
        when(serviceTaskRepository.findById(anyLong())).thenReturn(Optional.ofNullable(entity));
        when(serviceTaskRepository.findByFlowInstanceId(anyLong())).thenReturn(Collections.singletonList(entity));
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(1);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowNodeType.USER_TASK.getId(),
                FlowableElementType.USER_TASK.getId())).thenReturn(Collections.singletonList(new FlowableElement()));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.ofNullable(processInstanceId));

        when(taskService.createTaskQuery()).thenReturn(mock(TaskQuery.class));
        when(taskService.createTaskQuery().processInstanceId(anyLong())).thenReturn(mock(TaskQuery.class));
        when(taskService.createTaskQuery().processInstanceId(anyLong()).list()).thenReturn(Collections.emptyList());

        when(runtimeService.startProcessInstanceByKey(anyString(), anyMap())).thenReturn(processInstanceId);
        when(runtimeService.getProcessInstance(processInstanceId)).thenReturn(new ProcessInstance());

        when(entity.getId()).thenReturn(flowableNodeId);
        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getFlowInstanceId()).thenReturn(flowInstanceId);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        when(entity.getTaskType()).thenReturn(TaskType.TEST_TASK);
        when(entity.getTargetTaskId()).thenReturn(1L);
        when(entity.getStrategy()).thenReturn(FlowTaskExecutionStrategy.AUTO);
        when(entity.getWaitExecExpireIntervalSeconds()).thenReturn(10L);
        when(entity.getExecutionTime()).thenReturn(10L);

        when(entity.getCreateTime()).thenReturn(System.currentTimeMillis() - 1000L);
        when(entity.getUpdateTime()).thenReturn(System.currentTimeMillis());

        when(entity.getFlowInstanceId()).thenReturn(flowInstanceId);
        when(entity.getFlowableNodeId()).thenReturn(flowableNodeId);

        when(entity.getTaskType()).thenReturn(TaskType.TEST_TASK);
    }

    @Test
    public void testCreate() {
        FlowTaskExecutionStrategy strategy = FlowTaskExecutionStrategy.MANUAL;
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TEST_TASK, 1L, 1L, strategy, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, runtimeService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(entity);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateWhenInstanceExists() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TEST_TASK, 1L, 1L, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, runtimeService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.create();
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        List<ServiceTaskInstanceEntity> entities = flowTaskInstance.batchCreate(instances, serviceTaskRepository);
        verify(serviceTaskRepository, times(1)).batchCreate(entities);
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TEST_TASK, 1L, 1L, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, runtimeService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(entity);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowableNodeId);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(listener);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(flowableNodeId, FlowNodeType.USER_TASK.getId(),
                FlowableElementType.USER_TASK.getId());
        verify(taskService, times(1)).complete(taskId, Collections.singletonMap(ABORT_VARIABLE_NAME, false));
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(flowableNodeId, FlowNodeType.USER_TASK.getId(),
                FlowableElementType.USER_TASK.getId());
        verify(taskService, times(1)).complete(taskId, Collections.singletonMap(ABORT_VARIABLE_NAME, true));
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        when(taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0).getId())
                .thenReturn(taskId);
        flowTaskInstance.cancel(true);
        verify(flowableAdaptor, times(1)).getFlowableElementByType(flowableNodeId, FlowNodeType.USER_TASK.getId(),
                FlowableElementType.USER_TASK.getId());
        verify(taskService, times(1)).cancel(taskId, true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        when(target.getTargetTaskInstanceId()).thenReturn(flowableNodeId);
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");
        when(target.getTargetTaskInstanceId()).thenReturn(flowableNodeId);
        flowTaskInstance.bindServiceTask(target);
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1, TimeUnit.MINUTES)).thenReturn("result");
        when(target.getTargetTaskInstanceId()).thenReturn(flowableNodeId);
        flowTaskInstance.bindServiceTask(target);
        assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.MINUTES));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWhenNotPresent() {
        assertEquals(null, flowTaskInstance.getResult());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() throws Exception {
        flowTaskInstance.complete(false);
        verify(flowableAdaptor, times(1)).getFlowableElementByType(flowableNodeId, FlowNodeType.USER_TASK.getId(),
                FlowableElementType.USER_TASK.getId());
        verify(taskService, times(1)).completeAll(flowInstanceId);
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TEST_TASK, 1L, 1L, null, false, false,
                delegateConvertor, flowableAdaptor, eventPublisher, runtimeService, nodeRepository, sequenceRepository,
                serviceTaskRepository);
        assertEquals(entity, flowTaskInstance.mapToTaskEntity(flowTaskInstance));
    }
}