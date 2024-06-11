package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.FlowNodeStatus;
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

    private TaskService taskService = mock(TaskService.class);
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private RuntimeDelegateMapper delegateConvertor = mock(RuntimeDelegateMapper.class);
    private ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

    @Before
    public void setup() {
        ServiceTaskInstanceEntity entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(1L);
        entity.setStartEndpoint(true);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.MANUAL);
        entity.setTaskType(TaskType.SOME_TASK);
        entity.setTargetTaskId(2L);
        entity.setWaitExecExpireIntervalSeconds(30);
        entity.setExecutionTime(60);

        flowTaskInstance = new FlowTaskInstance(entity, delegateConvertor, mock(FlowableAdaptor.class),
                eventPublisher, taskService, mock(NodeInstanceEntityRepository.class),
                mock(SequenceInstanceRepository.class), serviceTaskRepository);
    }

    @Test
    public void testBatchCreate() {
        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(flowTaskInstance);

        when(serviceTaskRepository.batchCreate(any())).thenReturn(new ArrayList<>());

        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskRepository);
        
        assertNotNull(createdInstances);
        assertEquals(1, createdInstances.size());
        assertEquals(1L, createdInstances.get(0).getId().longValue());
        assertNotNull(createdInstances.get(0).getCreateTime());
        assertNotNull(createdInstances.get(0).getUpdateTime());

        verify(serviceTaskRepository).batchCreate(any());
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();

        assertEquals(1L, flowTaskInstance.getId().longValue());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        
        verify(serviceTaskRepository).update(any());
    }

    @Test
    public void testDoDelete() {
        flowTaskInstance.doDelete(1L);
        
        verify(serviceTaskRepository).deleteById(1L);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTask.getTargetTaskInstanceId()).thenReturn(1L);

        flowTaskInstance.bindServiceTask(targetTask);

        assertEquals(flowTaskInstance, targetTask.getTargetTaskInstance());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();

        verify(taskService).complete(any(), any());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();

        verify(taskService).complete(any(), any());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTask.isDone()).thenReturn(false);
        flowTaskInstance.bindServiceTask(targetTask);

        flowTaskInstance.cancel(false);

        verify(targetTask).cancel(false);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        when(targetTask.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(targetTask);

        assertTrue(flowTaskInstance.isDone());
    }

    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        Object result = new Object();
        when(targetTask.get()).thenReturn(result);
        flowTaskInstance.bindServiceTask(targetTask);

        assertEquals(result, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> targetTask = mock(BaseRuntimeFlowableDelegate.class);
        Object result = new Object();
        when(targetTask.get(anyLong(), any())).thenReturn(result);
        flowTaskInstance.bindServiceTask(targetTask);

        assertEquals(result, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }
}