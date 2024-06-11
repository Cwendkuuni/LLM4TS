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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowTaskInstanceTest {

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
    private ServiceTaskStartedListener listener;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Getter @Setter private static Long targetTaskId;

    @Before
    public void setup() {
        when(eventPublisher.publishEvent(any(TaskInstanceCreatedEvent.class))).thenReturn(null);
        when(taskService.createTaskQuery()).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceRepository mockServiceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);
        List<ServiceTaskInstanceEntity> entities = Collections.singletonList(new ServiceTaskInstanceEntity());
        when(serviceTaskRepository.batchCreate(entities)).thenReturn(entities);
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, mockServiceTaskInstanceRepository);
        assertEquals(instances, result);
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.save(entity)).thenReturn(entity);

        flowTaskInstance.create();

        verify(serviceTaskRepository, times(1)).save(entity);
        verify(eventPublisher, times(1)).publishEvent(any(TaskInstanceCreatedEvent.class));
    }

    @Test
    public void testUpdate() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.EXECUTING;
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);

        when(entity.getId()).thenReturn(id);
        when(entity.getStatus()).thenReturn(status);
        int affectRows = 1;

        flowTaskInstance.setId(id);
        flowTaskInstance.setStatus(status);
        assertEquals(affectRows, flowTaskInstance.update());

        verify(serviceTaskRepository, times(1)).update(entity);
    }

    @Test
    public void testDoDelete() {
        Long instanceId = 1L;

        flowTaskInstance.doDelete(instanceId);

        verify(serviceTaskRepository, times(1)).deleteById(instanceId);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);

        flowTaskInstance.bindServiceTask(target);

        assertEquals(target, flowTaskInstance.targetTaskHandle);
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener mockListener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();

        verify(eventPublisher, times(1)).removeEventListener(mockListener);
        assertNull(flowTaskInstance.listener);
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener mockListener = mock(ServiceTaskStartedListener.class);

        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();

        verify(eventPublisher, times(1)).addEventListener(mockListener);
        assertEquals(mockListener, flowTaskInstance.listener);
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        // No assertions as this method completes the execution of user tasks related to the task instance.
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAbort() {
        flowTaskInstance.abort();

        // No assertions as this method completes the execution of user tasks related to the task instance and sets the
        // aborted flag.
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel() {
        flowTaskInstance.cancel(true);

        // No assertions as this method cancels the task handle and throws an exception if the task is not present.
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn("Result");
        flowTaskInstance.bindServiceTask(target);

        assertEquals("Result", flowTaskInstance.getResult());
    }

    @Test(expected = InterruptedException.class)
    public void testGetResultInterrupted() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new InterruptedException());
        flowTaskInstance.bindServiceTask(target);

        fail("InterruptedException should be thrown.");
    }

    @Test(expected = ExecutionException.class)
    public void testGetResultExecutionException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenThrow(new ExecutionException("ExecutionException"));
        flowTaskInstance.bindServiceTask(target);

        fail("ExecutionException should be thrown.");
    }

    @Test(expected = TimeoutException.class)
    public void testGetResultTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException("TimeoutException"));
        flowTaskInstance.bindServiceTask(target);

        fail("TimeoutException should be thrown.");
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> result = flowTaskInstance.getTargetDelegateClass();

        assertTrue(result.isAssignableFrom(BaseRuntimeFlowableDelegate.class));
    }

    @Test
    public void testComplete() {
        verifyNoMoreInteractions(flowableAdaptor);

        flowTaskInstance.complete(false);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK
                .name(), FlowableElementType.SERVICE_TASK.name());
        verify(taskService, times(1)).createTaskQuery();
        verify(taskService, times(1)).complete(anyLong(), anyMap());
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(anyLong());
    }

    @Test
    public void testCompleteWithAbortedFlag() {
        verifyNoMoreInteractions(flowableAdaptor);

        flowTaskInstance.complete(true);

        verify(flowableAdaptor, times(1)).getFlowableElementByType(anyLong(), FlowableElementType.USER_TASK
                .name(), FlowableElementType.SERVICE_TASK.name());
        verify(taskService, times(1)).createTaskQuery();
        verify(taskService, times(1)).complete(anyLong(), anyMap());
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(anyLong());

        Map<String, Object> variables = flowTaskInstance.getVariables();
        assertTrue(variables.containsKey(FlowTaskInstance.ABORT_VARIABLE_NAME));
        assertTrue((Boolean) variables.get(FlowTaskInstance.ABORT_VARIABLE_NAME));
    }
}