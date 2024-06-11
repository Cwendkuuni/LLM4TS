package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.event.TaskEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeTypeEnum;
import com.oceanbase.odc.service.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit tests for {@link FlowTaskInstance}.
 *
 * @author yh263208
 */
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
    private RuntimeDelegateMapper delegateConvertor;

    @InjectMocks
    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceEntity serviceTaskInstanceEntity;

    @Mock
    private Task task;

    @Mock
    private TaskInfo taskInfo;

    @Mock
    private FlowableElement flowableElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowableElementByType(anyLong(), eq(FlowNodeTypeEnum.USER_TASK),
                eq(FlowableElementType.USER_TASK))).thenReturn(Collections.singletonList(flowableElement));
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(serviceTaskInstanceEntity));
        List<FlowTaskInstance> result = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, result.size());
        assertEquals(flowTaskInstance.getId(), result.get(0).getId());
    }

    @Test
    public void testCreate() {
        when(serviceTaskRepository.save(any())).thenReturn(serviceTaskInstanceEntity);
        flowTaskInstance.create();
        verify(serviceTaskRepository, times(1)).save(any());
    }

    @Test
    public void testUpdate() {
        when(serviceTaskRepository.update(any())).thenReturn(1);
        flowTaskInstance.update();
        verify(serviceTaskRepository, times(1)).update(any());
    }

    @Test
    public void testDelete() {
        flowTaskInstance.dealloc();
        verify(serviceTaskRepository, times(1)).deleteById(eq(flowTaskInstance.getId()));
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(target);
        verify(flowTaskInstance, times(1)).setStatus(eq(FlowNodeStatus.EXECUTING));
    }

    @Test
    public void testIsPresentOnThisMachine() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.alloc();
        flowTaskInstance.dealloc();
        verify(flowTaskInstance, times(1)).listener = null;
        verify(eventPublisher, times(1)).removeEventListener(eq(listener));
    }

    @Test
    public void testAlloc() {
        ServiceTaskStartedListener listener = mock(ServiceTaskStartedListener.class);
        flowTaskInstance.dealloc();
        flowTaskInstance.alloc();
        verify(flowTaskInstance, times(1)).listener = listener;
        verify(eventPublisher, times(1)).addEventListener(eq(listener));
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowNodeTypeEnum.USER_TASK), eq(FlowableElementType.USER_TASK));
        verify(taskService, times(1)).complete(anyLong(), anyMap());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowNodeTypeEnum.USER_TASK), eq(FlowableElementType.USER_TASK));
        verify(taskService, times(1)).complete(anyLong(), anyMap());
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowTaskInstance.getFlowInstanceId()));
        verify(taskService, times(flowableInstanceTasks.size())).complete(anyLong(), anyMap());
    }

    @Test
    public void testCancel() {
        when(taskService.cancel(anyLong(), anyBoolean())).thenReturn(true);
        flowTaskInstance.cancel(true);
        verify(taskService, times(1)).cancel(eq(task.getId()), eq(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel_notOnMachine() {
        flowTaskInstance.cancel(true);
        throw new UnsupportedOperationException("Task isn't on this machine");
    }

    @Test
    public void testIsDone() {
        when(task.isDone()).thenReturn(true);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws Exception {
        when(task.getResult()).thenReturn("result");
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResult_timeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(task.getResult(anyLong(), any())).thenReturn("timeout_result");
        assertEquals("timeout_result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetResult_timeout_exception() throws InterruptedException, ExecutionException, TimeoutException {
        when(task.getResult(anyLong(), any())).thenThrow(new TimeoutException("timeout"));
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testComplete() {
        verify(flowableAdaptor, times(1)).getFlowableElementByType(eq(flowTaskInstance.getId()),
                eq(FlowNodeTypeEnum.USER_TASK), eq(FlowableElementType.USER_TASK));
        verify(taskService, times(flowableInstanceTasks.size())).complete(anyLong(), anyMap());
        verify(flowableAdaptor, times(1)).getProcessInstanceIdByFlowInstanceId(eq(flowTaskInstance.getFlowInstanceId()));
    }

    @Test
    public void testMapToTaskEntity() {
        FlowNodeStatus status = FlowNodeStatus.PENDING;
        FlowNodeType nodeType = FlowNodeType.SERVICE_TASK;
        FlowNodeStatus expectedStatus = FlowNodeStatus.PENDING;
        FlowableElementType expectedElementType = FlowableElementType.SERVICE_TASK;
        Long expectedOrganizationId = 1L;
        Long expectedFlowInstanceId = 1L;
        Long expectedId = 1L;
        Long expectedTargetTaskId = null;
        Integer expectedExecutionTime = null;
        Integer expectedPendingExpireIntervalSeconds = null;
        FlowTaskExecutionStrategy expectedStrategy = null;
        TaskType expectedTaskType = TaskType.UNKNOWN;
        FlowableElementTypeEnum expectedNodeTypeEnum = FlowNodeTypeEnum.SERVICE_TASK;

        ServiceTaskInstanceEntity expectedServiceTaskInstanceEntity = new ServiceTaskInstanceEntity();
        expectedServiceTaskInstanceEntity.setOrganizationId(expectedOrganizationId);
        expectedServiceTaskInstanceEntity.setStatus(expectedStatus);
        expectedServiceTaskInstanceEntity.setFlowInstanceId(expectedFlowInstanceId);
        expectedServiceTaskInstanceEntity.setStartEndpoint(false);
        expectedServiceTaskInstanceEntity.setEndEndpoint(false);
        expectedServiceTaskInstanceEntity.setStrategy(expectedStrategy);
        expectedServiceTaskInstanceEntity.setTaskType(expectedTaskType);
        expectedServiceTaskInstanceEntity.setTargetTaskId(expectedTargetTaskId);
        expectedServiceTaskInstanceEntity.setExecutionTime(expectedExecutionTime);
        expectedServiceTaskInstanceEntity.setWaitExecExpireIntervalSeconds(expectedPendingExpireIntervalSeconds);
        expectedServiceTaskInstanceEntity.setNodeType(expectedNodeTypeEnum);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(new ExecutionStrategyConfig(expectedStrategy,
                expectedPendingExpireIntervalSeconds, expectedExecutionTime), nodeType, expectedOrganizationId,
                expectedFlowInstanceId, null, false, false, flowableAdaptor, eventPublisher, taskService,
                nodeRepository, sequenceRepository, serviceTaskRepository);
        ServiceTaskInstanceEntity actualServiceTaskInstanceEntity = FlowTaskInstance.mapToTaskEntity(flowTaskInstance);

        assertEquals(expectedOrganizationId, actualServiceTaskInstanceEntity.getOrganizationId());
        assertEquals(expectedStatus, actualServiceTaskInstanceEntity.getStatus());
        assertEquals(expectedFlowInstanceId, actualServiceTaskInstanceEntity.getFlowInstanceId());
        assertEquals(false, actualServiceTaskInstanceEntity.isStartEndpoint());
        assertEquals(false, actualServiceTaskInstanceEntity.isEndEndpoint());
        assertEquals(expectedStrategy, actualServiceTaskInstanceEntity.getStrategy());
        assertEquals(expectedTaskType, actualServiceTaskInstanceEntity.getTaskType());
        assertEquals(expectedTargetTaskId, actualServiceTaskInstanceEntity.getTargetTaskId());
        assertEquals(expectedExecutionTime, actualServiceTaskInstanceEntity.getExecutionTime());
        assertEquals(expectedPendingExpireIntervalSeconds, actualServiceTaskInstanceEntity.getWaitExecExpireIntervalSeconds());
        assertEquals(expectedNodeTypeEnum, actualServiceTaskInstanceEntity.getNodeType());
    }
}