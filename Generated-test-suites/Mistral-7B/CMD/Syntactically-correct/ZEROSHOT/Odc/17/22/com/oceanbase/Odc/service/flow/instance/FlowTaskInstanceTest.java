package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedEvent;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Unit tests for {@link FlowTaskInstance}
 *
 * @author yh263208
 * @since ODC_release_3.3.0
 */
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
    private RuntimeDelegateMapper delegateConvertor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Mock
    private ServiceTaskStartedListener listener;

    @Mock
    private TaskInfo taskInfo;

    @Mock
    private Task task;

    private static final String PROCESS_INSTANCE_ID = "processInstanceId";

    private static final String TASK_NAME = "taskName";

    private static final Long TASK_ID = 1L;

    private static final Long SERVICE_TASK_ID = 2L;

    private static final Long FLOW_INSTANCE_ID = 1L;

    private static final Long ORGANIZATION_ID = 1L;

    private static final Long NODE_ID = 1L;

    private static final FlowNodeStatus STATUS = FlowNodeStatus.PENDING;

    private static final FlowNodeType NODE_TYPE = FlowNodeType.SERVICE_TASK;

    private static final TaskType TASK_TYPE = TaskType.PROCESS;

    private static final ExecutionStrategyConfig STRATEGY_CONFIG =
            new ExecutionStrategyConfig(FlowTaskExecutionStrategy.MANUAL, 5L);

    @Before
    public void setup() {
        when(eventPublisher.removeEventListener(any()))
                .then(invocation -> invocation.getArgument(0)).thenReturn(listener);
        when(eventPublisher.publishEvent(any()))
                .thenAnswer(invocation -> {});
        when(taskService.createTaskQuery().processInstanceId(eq(PROCESS_INSTANCE_ID)))
                .thenReturn(mock(TaskQuery.class));
        when(taskService.createTaskQuery().processInstanceId(eq(PROCESS_INSTANCE_ID)).list())
                .thenReturn(Collections.emptyList());
        when(taskService.createTaskQuery().list())
                .thenReturn(Collections.singletonList(taskInfo));
        when(taskService.createTaskQuery().list().get(0))
                .thenReturn(task);
        when(taskService.complete(eq(task.getId())))
                .thenReturn(null);
        when(taskService.getTask(eq(task.getId())))
                .thenReturn(task);
        when(delegateConvertor.map(eq(taskType)))
                .thenReturn(targetTaskHandle);
        when(serviceTaskRepository.save(any()))
                .thenAnswer(invocation -> ((ServiceTaskInstanceEntity) invocation.getArgument(0)));
        when(serviceTaskRepository.update(any()))
                .thenReturn(1);
        when(nodeRepository.findById(eq(NODE_ID)))
                .thenReturn(Optional.of(new FlowNodeInstanceEntity()));
        when(sequenceRepository.findById(eq(1L)))
                .thenReturn(Optional.of(new SequenceInstanceEntity()));
        when(serviceTaskRepository.deleteById(anyLong()))
                .thenReturn(1);
        when(flowableAdaptor.getFlowableElementByType(eq(SERVICE_TASK_ID), eq(NODE_TYPE), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        when(flowableAdaptor.getProcessInstanceIdByFlowInstanceId(eq(FLOW_INSTANCE_ID)))
                .thenReturn(Optional.of(PROCESS_INSTANCE_ID));
    }

    @Test
    public void testCreate() {
        FlowTaskInstance instance = new FlowTaskInstance(taskType, ORGANIZATION_ID, FLOW_INSTANCE_ID, STRATEGY_CONFIG,
                true, false, delegateConvertor, flowableAdaptor, eventPublisher, taskService, nodeRepository,
                sequenceRepository, serviceTaskRepository);
        instance.create();
        verify(serviceTaskRepository).save(any());
    }

    @Test
    public void testUpdate() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.update();
        verify(serviceTaskRepository).update(any());
    }

    @Test
    public void testDelete() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.doDelete(SERVICE_TASK_ID);
        verify(serviceTaskRepository).deleteById(SERVICE_TASK_ID);
    }

    @Test
    public void testBindServiceTask() {
        when(task.getId()).thenReturn(TASK_ID);
        when(task.getName()).thenReturn(TASK_NAME);
        when(task.isAssigned()).thenReturn(false);
        when(task.isSuspended()).thenReturn(false);

        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.bindServiceTask(targetTaskHandle);
        verify(instance).setStatus(FlowNodeStatus.EXECUTING);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeallocateWhenNotPresent() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.dealloc();
    }

    @Test
    public void testAllocate() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.alloc();
        verify(eventPublisher).addEventListener(eq(listener));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeallocateWhenPresent() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.alloc();
        instance.dealloc();
        verify(eventPublisher).removeEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        when(flowableAdaptor.getFlowableElementByType(eq(SERVICE_TASK_ID), eq(NODE_TYPE), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.confirmExecute();
        verify(taskService).createTaskQuery().processInstanceId(eq(PROCESS_INSTANCE_ID)).list()
                .forEach(task -> verify(taskService).complete(task.getId()));
    }

    @Test
    public void testAbort() {
        when(flowableAdaptor.getFlowableElementByType(eq(SERVICE_TASK_ID), eq(NODE_TYPE), eq(FlowableElementType.USER_TASK)))
                .thenReturn(Collections.singletonList(new FlowableElement()));
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.abort();
        verify(taskService).createTaskQuery().processInstanceId(eq(PROCESS_INSTANCE_ID)).list()
                .forEach(task -> verify(taskService).complete(task.getId()));
        verify(taskInfo.getVariables()).putIfAbsent(ABORT_VARIABLE_NAME, true);
    }

    @Test
    public void testCancel() throws InterruptedException, ExecutionException, TimeoutException {
        when(task.getId()).thenReturn(TASK_ID);
        when(task.getName()).thenReturn(TASK_NAME);
        when(task.isAssigned()).thenReturn(true);
        when(task.isSuspended()).thenReturn(false);

        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.bindServiceTask(targetTaskHandle);
        instance.cancel(true);
        verify(targetTaskHandle).cancel(true);
    }

    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        when(task.getId()).thenReturn(TASK_ID);
        when(task.getName()).thenReturn(TASK_NAME);
        when(task.isAssigned()).thenReturn(true);
        when(task.isSuspended()).thenReturn(false);

        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.bindServiceTask(targetTaskHandle);
        instance.complete(false);
        verify(targetTaskHandle).isDone();
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(task.getId()).thenReturn(TASK_ID);
        when(task.getName()).thenReturn(TASK_NAME);
        when(task.isAssigned()).thenReturn(true);
        when(task.isSuspended()).thenReturn(false);

        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.bindServiceTask(targetTaskHandle);
        when(targetTaskHandle.get()).thenReturn("result");
        assertEquals("result", instance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(task.getId()).thenReturn(TASK_ID);
        when(task.getName()).thenReturn(TASK_NAME);
        when(task.isAssigned()).thenReturn(true);
        when(task.isSuspended()).thenReturn(false);

        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.bindServiceTask(targetTaskHandle);
        when(targetTaskHandle.get(anyLong(), any())).thenAnswer(invocation -> {
            long timeout = invocation.getArgumentAt(0, Long.class);
            TimeUnit unit = invocation.getArgumentAt(1, TimeUnit.class);
            Thread.sleep(timeout * 1000);
            return "result";
        });
        assertEquals("result", instance.getResult(5000, TimeUnit.MILLISECONDS));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWhenNotPresent() {
        FlowTaskInstance instance = new FlowTaskInstance(new ServiceTaskInstanceEntity(), delegateConvertor,
                flowableAdaptor, eventPublisher, taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
        instance.getResult();
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testMapToTaskEntity() {
        ServiceTaskInstanceEntity expected = new ServiceTaskInstanceEntity();
        expected.setOrganizationId(ORGANIZATION_ID);
        expected.setStatus(STATUS);
        expected.setFlowInstanceId(FLOW_INSTANCE_ID);
        expected.setStartEndpoint(true);
        expected.setEndEndpoint(false);
        expected.setStrategy(STRATEGY_CONFIG.getStrategy());
        expected.setTaskType(TASK_TYPE);
        expected.setTargetTaskId(NODE_ID);
        expected.setWaitExecExpireIntervalSeconds(STRATEGY_CONFIG.getPendingExpireIntervalSeconds());
        expected.setExecutionTime(STRATEGY_CONFIG.getExecutionTime());

        ServiceTaskInstanceEntity actual = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);
        assertEquals(expected, actual);
    }
}