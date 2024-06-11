package com.oceanbase.odc.service.flow.instance;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;

    @Mock
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RuntimeDelegateMapper delegateConvertor;

    @Mock
    private BaseRuntimeFlowableDelegate<?> targetTaskHandle;

    @Before
    public void setUp() {
        flowTaskInstance = new FlowTaskInstance(TaskType.TASK_TYPE, 1L, 1L, new ExecutionStrategyConfig(), true,
                true, delegateConvertor, new FlowableAdaptor(), eventPublisher, taskService,
                mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskRepository);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Arrays.asList(flowTaskInstance);
        when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Arrays.asList(new ServiceTaskInstanceEntity()));
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        assertEquals(1, createdInstances.size());
        assertNotNull(createdInstances.get(0).getId());
        assertNotNull(createdInstances.get(0).getCreateTime());
        assertNotNull(createdInstances.get(0).getUpdateTime());
    }

    @Test
    public void testCreate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        when(serviceTaskRepository.save(any())).thenReturn(entity);
        flowTaskInstance.create();
        assertNotNull(flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(flowTaskInstance.getId());
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setTargetTaskId(flowTaskInstance.getTargetTaskId());
        entity.setTaskType(flowTaskInstance.getTaskType());
        when(serviceTaskRepository.update(any())).thenReturn(1);
        flowTaskInstance.update();
    }

    @Test
    public void testDoDelete() {
        flowTaskInstance.doDelete(1L);
        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertNotNull(flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        verify(eventPublisher, times(1)).addEventListener(any());
    }

    @Test
    public void testComplete() {
        Task task = mock(Task.class);
        when(task.getName()).thenReturn("Task");
        List<Task> tasks = Arrays.asList(task);
        when(taskService.createTaskQuery().list()).thenReturn(tasks);
        flowTaskInstance.complete(false);
        verify(taskService, times(1)).complete(anyString(), any());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        verify(targetTaskHandle, times(1)).cancel(false);
    }

    @Test
    public void testCancel() {
        when(targetTaskHandle.isDone()).thenReturn(false);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        flowTaskInstance.cancel(false);
        verify(targetTaskHandle, times(1)).cancel(false);
    }

    @Test
    public void testIsDone() {
        when(targetTaskHandle.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        when(targetTaskHandle.get()).thenReturn("result");
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals("result", flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(targetTaskHandle.get(anyLong(), any())).thenReturn("result");
        flowTaskInstance.bindServiceTask(targetTaskHandle);
        assertEquals("result", flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        assertEquals(delegateConvertor.map(flowTaskInstance.getTaskType()), flowTaskInstance.getTargetDelegateClass());
    }

}