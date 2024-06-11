package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlowTaskInstanceTest {

    @Mock
    private TaskService taskService;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        flowTaskInstance = null;
    }

    @Test
    public void testFlowTaskInstanceConstructionWithEntity() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getOrganizationId()).thenReturn(1L);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.PENDING);
        when(entity.getFlowInstanceId()).thenReturn(1L);
        when(entity.getCreateTime()).thenReturn(System.currentTimeMillis());
        when(entity.getUpdateTime()).thenReturn(System.currentTimeMillis());
        when(entity.isStartEndpoint()).thenReturn(true);
        when(entity.isEndEndpoint()).thenReturn(false);
        when(entity.getTargetTaskId()).thenReturn(2L);
        when(entity.getTaskType()).thenReturn(TaskType.TASK_TYPE_1);

        RuntimeDelegateMapper delegateConvertor = mock(RuntimeDelegateMapper.class);
        when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(mock(BaseRuntimeFlowableDelegate.class));

        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(Arrays.asList(
                mock(FlowableElement.class), mock(FlowableElement.class)));

        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(true);

        TaskService taskService = mock(TaskService.class);

        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);

        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);

        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, flowableAdaptor,
                eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(1L, flowTaskInstance.getId());
        assertEquals(1L, flowTaskInstance.getOrganizationId());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(1L, flowTaskInstance.getFlowInstanceId());
        assertEquals(System.currentTimeMillis(), flowTaskInstance.getCreateTime());
        assertEquals(System.currentTimeMillis(), flowTaskInstance.getUpdateTime());
        assertTrue(flowTaskInstance.isStartEndpoint());
        assertFalse(flowTaskInstance.isEndEndpoint());
        assertEquals(2L, flowTaskInstance.getTargetTaskId());
        assertEquals(TaskType.TASK_TYPE_1, flowTaskInstance.getTaskType());
        assertSame(delegateConvertor, flowTaskInstance.delegateConvertor);
        assertSame(flowableAdaptor, flowTaskInstance.flowableAdaptor);
        assertSame(eventPublisher, flowTaskInstance.eventPublisher);
        assertSame(taskService, flowTaskInstance.taskService);
        assertSame(nodeRepository, flowTaskInstance.nodeRepository);
        assertSame(sequenceRepository, flowTaskInstance.sequenceRepository);
        assertSame(serviceTaskRepository, flowTaskInstance.serviceTaskRepository);
    }

    @Test
    public void testFlowTaskInstanceConstructionWithTaskType() {
        Long organizationId = 1L;
        Long flowInstanceId = 1L;
        ExecutionStrategyConfig strategyConfig = new ExecutionStrategyConfig();
        strategyConfig.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        strategyConfig.setPendingExpireIntervalSeconds(10L);
        strategyConfig.setExecutionTime(100L);

        RuntimeDelegateMapper delegateConvertor = mock(RuntimeDelegateMapper.class);
        when(delegateConvertor.map(TaskType.TASK_TYPE_1)).thenReturn(mock(BaseRuntimeFlowableDelegate.class));

        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK)).thenReturn(Arrays.asList(
                mock(FlowableElement.class), mock(FlowableElement.class)));

        EventPublisher eventPublisher = mock(EventPublisher.class);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(true);

        TaskService taskService = mock(TaskService.class);

        NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);

        SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);

        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TASK_TYPE_1, organizationId, flowInstanceId,
                strategyConfig, true, true, delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);

        assertEquals(FlowNodeType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertEquals(organizationId, flowTaskInstance.getOrganizationId());
        assertEquals(flowInstanceId, flowTaskInstance.getFlowInstanceId());
        assertEquals(strategyConfig, flowTaskInstance.strategyConfig);
        assertEquals(true, flowTaskInstance.isStartEndpoint());
        assertEquals(true, flowTaskInstance.isEndEndpoint());
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getNodeType());
        assertSame(delegateConvertor, flowTaskInstance.delegateConvertor);
        assertSame(flowableAdaptor, flowTaskInstance.flowableAdaptor);
        assertSame(eventPublisher, flowTaskInstance.eventPublisher);
        assertSame(taskService, flowTaskInstance.taskService);
        assertSame(nodeRepository, flowTaskInstance.nodeRepository);
        assertSame(sequenceRepository, flowTaskInstance.sequenceRepository);
        assertSame(serviceTaskRepository, flowTaskInstance.serviceTaskRepository);
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Arrays.asList(mock(FlowTaskInstance.class), mock(FlowTaskInstance.class));

        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        when(serviceTaskInstanceRepository.batchCreate(anyList())).thenReturn(Collections.emptyList());

        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);

        assertSame(Collections.emptyList(), result);
    }

    @Test
    public void testCreate() throws Exception {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(entity.getId()).thenReturn(1L);
        when(entity.getFlowInstanceId()).thenReturn(1L);

        doReturn(entity).when(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));

        CountDownLatch latch = new CountDownLatch(1);
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenAnswer(i -> {
            latch.countDown();
            return true;
        });

        flowTaskInstance.create();

        latch.await(5, TimeUnit.SECONDS);

        verify(taskService).saveTask(any(Task.class), anyMap());
        verify(serviceTaskRepository).save(any(ServiceTaskInstanceEntity.class));
        verify(eventPublisher).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);

        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getStatus()).thenReturn(FlowNodeStatus.PENDING);

        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        entity.setId(1L);
        when(entity.getStatus()).thenReturn(FlowNodeStatus.EXECUTING);

        int affectRows = 1;
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(affectRows);

        flowTaskInstance.update();

        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testDelete() {
        Long instanceId = 1L;
        when(serviceTaskRepository.deleteById(instanceId)).thenReturn(true);

        flowTaskInstance.doDelete(instanceId);

        verify(serviceTaskRepository).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        when(target.getTargetTaskInstanceId()).thenReturn(1L);

        flowTaskInstance.bindServiceTask(target);

        assertEquals(target, flowTaskInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(1L);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        assertNull(flowTaskInstance.listener);

        verify(eventPublisher).removeEventListener(listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();

        assertSame(listener, flowTaskInstance.listener);

        verify(eventPublisher).addEventListener(listener);
    }

    @Test
    public void testConfirmExecute() {
        completeTask(true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        completeTask(true);
        flowTaskInstance.abort();
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        flowTaskInstance.bindServiceTask(target);

        flowTaskInstance.cancel(true);

        verify(target).cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);

        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("result");

        flowTaskInstance.bindServiceTask(target);

        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);

        flowTaskInstance.bindServiceTask(target);

        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE, flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> targetDelegateClass =
                flowTaskInstance.getTargetDelegateClass();

        assertTrue(BaseRuntimeFlowableDelegate.class.isAssignableFrom(targetDelegateClass));
    }

    @Test
    public void testComplete() throws Exception {
        TaskType taskType = TaskType.TASK_TYPE_1;
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(1L));
        when(taskService.createTaskQuery().processInstanceId(1L).list()).thenReturn(Arrays.asList(mock(Task.class),
                mock(Task.class)));

        flowTaskInstance.complete(true);

        verify(taskService).complete(anyLong(), anyMap());
    }

    private void completeTask(boolean aborted) throws Exception {
        TaskType taskType = TaskType.TASK_TYPE_1;
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong())).thenReturn(Optional.of(1L));
        when(taskService.createTaskQuery().processInstanceId(1L).list()).thenReturn(Arrays.asList(mock(Task.class),
                mock(Task.class)));
        when(taskService.complete(anyLong(), anyMap())).thenReturn(true);

        flowTaskInstance.complete(aborted);

        verify(taskService).complete(anyLong(), anyMap());
    }
}