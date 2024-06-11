package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowableElement;
import com.oceanbase.odc.core.flow.model.FlowElementStatus;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.core.flow.model.FlowTaskExecutionStrategy;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity entity;
    private RuntimeDelegateMapper convertor;
    private FlowableAdaptor flowableAdaptor;
    private EventPublisher eventPublisher;
    private TaskService taskService;
    private NodeInstanceEntityRepository nodeRepository;
    private SequenceInstanceRepository sequenceRepository;
    private ServiceTaskInstanceRepository serviceTaskRepository;

    @Before
    public void setUp() {
        entity = mock(ServiceTaskInstanceEntity.class);
        convertor = mock(RuntimeDelegateMapper.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        eventPublisher = mock(EventPublisher.class);
        taskService = mock(TaskService.class);
        nodeRepository = mock(NodeInstanceEntityRepository.class);
        sequenceRepository = mock(SequenceInstanceRepository.class);
        serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        flowTaskInstance = new FlowTaskInstance(entity, convertor, flowableAdaptor, eventPublisher,
                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        when(entity.getId()).thenReturn(1L);
        when(entity.getCreateTime()).thenReturn(System.currentTimeMillis());
        when(entity.getUpdateTime()).thenReturn(System.currentTimeMillis());

        flowTaskInstance.create();

        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        when(entity.getId()).thenReturn(1L);
        when(entity.getStatus()).thenReturn(FlowElementStatus.PENDING);
        when(entity.getTargetTaskId()).thenReturn(2L);
        when(entity.getTaskType()).thenReturn(TaskType.TYPE1);

        int affectRows = 1;
        when(serviceTaskRepository.update(any(ServiceTaskInstanceEntity.class))).thenReturn(affectRows);

        flowTaskInstance.update();

        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertEquals(FlowElementStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(2L, flowTaskInstance.getTargetTaskId().longValue());
        assertEquals(TaskType.TYPE1, flowTaskInstance.getTaskType());
    }

    @Test
    public void testDoDelete() {
        flowTaskInstance.doDelete(1L);

        verify(serviceTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = Collections.singletonList(flowTaskInstance);
        ServiceTaskInstanceEntity entity = mock(ServiceTaskInstanceEntity.class);
        when(serviceTaskRepository.batchCreate(anyList())).thenReturn(Collections.singletonList(entity));

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        
        assertEquals(1, createdInstances.size());
        assertEquals(entity.getId(), createdInstances.get(0).getId());
        assertNotNull(createdInstances.get(0).getCreateTime());
        assertNotNull(createdInstances.get(0).getUpdateTime());
    }

    @Test
    public void testGetCoreFlowableElementType() {
        assertEquals(FlowableElementType.SERVICE_TASK, flowTaskInstance.getCoreFlowableElementType());
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());

        flowTaskInstance.bindServiceTask(target);

        assertEquals(target, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());

        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(target);

        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCompleteAutoStrategy() {
        flowTaskInstance.setStrategyConfig(new ExecutionStrategyConfig(FlowTaskExecutionStrategy.AUTO));
        flowTaskInstance.complete(false);
    }

    @Test
    public void testComplete() {
        TaskService mockTaskService = mock(TaskService.class);
        when(flowTaskInstance.getTaskService()).thenReturn(mockTaskService);

        Task task1 = mock(Task.class);
        when(task1.getId()).thenReturn("1");
        when(task1.getName()).thenReturn("Task1");

        Task task2 = mock(Task.class);
        when(task2.getId()).thenReturn("2");
        when(task2.getName()).thenReturn("Task2");

        List<Task> tasks = Arrays.asList(task1, task2);
        when(mockTaskService.createTaskQuery().processInstanceId(anyString()).list()).thenReturn(tasks);

        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.complete(false);

        verify(mockTaskService, times(1)).complete("1", Collections.singletonMap("aborted", false));
        verify(mockTaskService, times(1)).complete("2", Collections.singletonMap("aborted", false));
    }

    @Test
    public void testCancel() throws InterruptedException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.isDone()).thenReturn(false);
        flowTaskInstance.bindServiceTask(target);

        flowTaskInstance.cancel(true);

        verify(target, times(1)).cancel(true);
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
        Object result = new Object();
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get()).thenReturn(result);
        flowTaskInstance.bindServiceTask(target);

        assertEquals(result, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object result = new Object();
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.get(1L, TimeUnit.SECONDS)).thenReturn(result);
        flowTaskInstance.bindServiceTask(target);

        assertEquals(result, flowTaskInstance.getResult(1L, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        TaskType taskType = TaskType.TYPE1;
        when(convertor.map(taskType)).thenReturn(BaseRuntimeFlowableDelegate.class);

        assertEquals(BaseRuntimeFlowableDelegate.class, flowTaskInstance.getTargetDelegateClass());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.complete(true);
    }
}