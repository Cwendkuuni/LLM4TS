package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

/**
 * Tests for {@link FlowTaskInstance}
 *
 * @author yh263208
 */
public class FlowTaskInstanceTest {

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private NodeInstanceEntityRepository nodeRepository;

    @Mock
    private SequenceInstanceRepository sequenceRepository;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private ServiceTaskStartedListener listener;

    @InjectMocks
    private FlowTaskInstance testInstance;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(nodeRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(sequenceRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(serviceTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(serviceTaskRepository.save(any(ServiceTaskInstanceEntity.class))).thenReturn(new ServiceTaskInstanceEntity());
        when(runtimeService.getRuntimeService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(mock(TaskInfoQuery.class));
        when(taskService.createTaskQuery().processInstanceId(anyString())).thenReturn(mock(TaskQuery.class));
        when(taskService.complete(anyLong(), anyMap()))
                .thenReturn(mock(Task.class));
        when(flowableAdaptor.getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK))
                .thenReturn(Collections.singletonList(mock(FlowableElement.class)));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(anyLong()))
                .thenReturn(Optional.of("processInstanceId"));
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = new FlowTaskInstance(
                new ServiceTaskInstanceEntity(), delegateConvertor, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.create();
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = List.of(testInstance);
        List<ServiceTaskInstanceEntity> entities = serviceTaskRepository.batchCreate(List.of(new ServiceTaskInstanceEntity()));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, result.size());
        assertEquals(instances.get(0).getId(), result.get(0).getId());
    }

    @Test
    public void testUpdate() {
        testInstance.update();
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
    }

    @Test
    public void testDelete() {
        testInstance.doDelete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        assertEquals(FlowNodeStatus.EXECUTING, testInstance.getStatus());
        verify(testInstance, times(1)).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertTrue(testInstance.isPresentOnThisMachine());
        testInstance.dealloc();
        assertFalse(testInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        testInstance.alloc();
        testInstance.dealloc();
        verify(eventPublisher, times(1)).removeEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testAlloc() {
        testInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any(ServiceTaskStartedListener.class));
    }

    @Test
    public void testConfirmExecute() {
        testInstance.confirmExecute();
        verify(flowableAdaptor, times(1))
                .getFlowableElementByType(testInstance.getId(), FlowableElementType.USER_TASK);
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testAbort() {
        testInstance.abort();
        verify(flowableAdaptor, times(1))
                .getFlowableElementByType(testInstance.getId(), FlowableElementType.USER_TASK);
        verify(taskService, times(1)).complete(anyLong(), mapOf(ABORT_VARIABLE_NAME, Boolean.TRUE));
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        testInstance.cancel(true);
        verify(target, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        when(target.isDone()).thenReturn(true);
        assertTrue(testInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        when(target.get()).thenReturn("result");
        assertEquals("result", testInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout()
            throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        testInstance.bindServiceTask(target);
        when(target.get(1, TimeUnit.MINUTES)).thenThrow(new TimeoutException());
        assertNull(testInstance.getResult(1, TimeUnit.MINUTES));
    }

    @Test
    public void testComplete() {
        testInstance.complete(false);
        verify(flowableAdaptor, times(1))
                .getFlowableElementByType(testInstance.getId(), FlowableElementType.USER_TASK);
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAutomaticTasksDoNotSupportExecution() {
        testInstance.complete(false);
        verify(flowableAdaptor, times(1))
                .getFlowableElementByType(testInstance.getId(), FlowableElementType.USER_TASK);
        verify(taskService, never()).complete(anyLong(), anyMap());
        testInstance.strategyConfig = new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO);
        testInstance.complete(false);
    }

    @Test
    public void testMapToTaskEntity() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        ServiceTaskInstanceEntity entity = FlowTaskInstance.mapToTaskEntity(instance);
        assertEquals(instance.getOrganizationId(), entity.getOrganizationId());
        assertEquals(instance.getStatus(), entity.getStatus());
        assertEquals(instance.getFlowInstanceId(), entity.getFlowInstanceId());
        assertEquals(instance.isStartEndpoint(), entity.isStartEndpoint());
        assertEquals(instance.isEndEndpoint(), entity.isEndEndpoint());
        assertEquals(instance.strategyConfig.getStrategy(), entity.getStrategy());
        assertEquals(instance.getTaskType(), entity.getTaskType());
        assertEquals(instance.getTargetTaskId(), entity.getTargetTaskId());
        assertEquals(instance.strategyConfig.getPendingExpireIntervalSeconds(),
                entity.getWaitExecExpireIntervalSeconds());
        assertEquals(instance.strategyConfig.getExecutionTime(), entity.getExecutionTime());
    }
}