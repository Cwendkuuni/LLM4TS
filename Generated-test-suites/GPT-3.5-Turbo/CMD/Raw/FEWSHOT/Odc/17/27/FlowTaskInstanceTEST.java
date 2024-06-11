package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;

public class FlowTaskInstanceTest {

    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceEntity entity;
    private RuntimeDelegateMapper runtimeDelegateMapper = mock(RuntimeDelegateMapper.class);
    private FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    private TaskService taskService = mock(TaskService.class);
    private NodeInstanceEntityRepository nodeRepository = mock(NodeInstanceEntityRepository.class);
    private SequenceInstanceRepository sequenceRepository = mock(SequenceInstanceRepository.class);
    private ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

    @Before
    public void setUp() {
        entity = new ServiceTaskInstanceEntity();
        entity.setId(1L);
        entity.setOrganizationId(1L);
        entity.setStatus(FlowNodeStatus.PENDING);
        entity.setFlowInstanceId(1L);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        entity.setStartEndpoint(false);
        entity.setEndEndpoint(false);
        entity.setStrategy(FlowTaskExecutionStrategy.AUTO);
        entity.setTaskType(TaskType.SOME_TASK);
        entity.setTargetTaskId(2L);
        entity.setWaitExecExpireIntervalSeconds(30);
        entity.setExecutionTime(60);
        
        when(serviceTaskRepository.save(any())).thenReturn(entity);
        
        flowTaskInstance = new FlowTaskInstance(entity, runtimeDelegateMapper, flowableAdaptor, eventPublisher,
                                                taskService, nodeRepository, sequenceRepository, serviceTaskRepository);
    }

    @Test
    public void testCreate() {
        flowTaskInstance.create();
        
        assertEquals(1L, flowTaskInstance.getId());
        assertNotNull(flowTaskInstance.getCreateTime());
        assertNotNull(flowTaskInstance.getUpdateTime());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        flowTaskInstance.update();
        
        assertEquals(1L, flowTaskInstance.getId());
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
    }

    @Test
    public void testIsPresentOnThisMachine() {
        assertFalse(flowTaskInstance.isPresentOnThisMachine());
        
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        target.setTargetTaskInstanceId(1L);
        flowTaskInstance.bindServiceTask(target);
        
        assertTrue(flowTaskInstance.isPresentOnThisMachine());
    }

    @Test
    public void testDealloc() {
        flowTaskInstance.dealloc();
        
        verify(eventPublisher, times(1)).removeEventListener(any());
    }

    @Test
    public void testAlloc() {
        flowTaskInstance.alloc();
        
        verify(eventPublisher, times(1)).addEventListener(any());
    }

    @Test
    public void testConfirmExecute() {
        flowTaskInstance.confirmExecute();
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testAbort() {
        flowTaskInstance.abort();
        
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }

    @Test
    public void testCancel() {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        when(target.getTargetTaskInstanceId()).thenReturn(1L);
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
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        Object result = new Object();
        when(target.get()).thenReturn(result);
        flowTaskInstance.bindServiceTask(target);
        
        assertEquals(result, flowTaskInstance.getResult());
    }

    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<?> target = mock(BaseRuntimeFlowableDelegate.class);
        Object result = new Object();
        when(target.get(1, TimeUnit.SECONDS)).thenReturn(result);
        flowTaskInstance.bindServiceTask(target);
        
        assertEquals(result, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }

    @Test
    public void testResourceType() {
        assertEquals(ResourceType.ODC_FLOW_TASK_INSTANCE.name(), flowTaskInstance.resourceType());
    }

    @Test
    public void testGetTargetDelegateClass() {
        Class<? extends BaseRuntimeFlowableDelegate<?>> delegateClass = SomeRuntimeDelegate.class;
        
        when(runtimeDelegateMapper.map(TaskType.SOME_TASK)).thenReturn(delegateClass);
        
        assertEquals(delegateClass, flowTaskInstance.getTargetDelegateClass());
    }

}

class SomeRuntimeDelegate extends BaseRuntimeFlowableDelegate<Object> {
    // Implement methods
}