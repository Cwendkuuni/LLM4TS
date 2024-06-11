package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.lift.serv.data.FlowTaskExecutionStrategy;
import org.junit.Test;

public class FlowTaskInstanceTest {

    @Test
    public void testCreate() {
        TaskService taskService = mock(TaskService.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_A, 1L, 1L, new ExecutionStrategyConfig(), true, false, 
            new RuntimeDelegateMapper(), new FlowableAdaptor(), mock(EventPublisher.class), taskService,
            mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskRepository);
        
        flowTaskInstance.create();
        
        verify(serviceTaskRepository, times(1)).save(any(ServiceTaskInstanceEntity.class));
        assertEquals(FlowNodeStatus.CREATED, flowTaskInstance.getStatus());
    }

    @Test
    public void testUpdate() {
        TaskService taskService = mock(TaskService.class);
        ServiceTaskInstanceRepository serviceTaskRepository = mock(ServiceTaskInstanceRepository.class);

        FlowTaskInstance flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_B, 2L, 2L, new ExecutionStrategyConfig(), false, true, 
            new RuntimeDelegateMapper(), new FlowableAdaptor(), mock(EventPublisher.class), taskService, 
            mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskRepository);
        
        flowTaskInstance.setId(1L);
        flowTaskInstance.setStatus(FlowNodeStatus.PENDING);
        flowTaskInstance.setTargetTaskId(100L);
        
        flowTaskInstance.update();
        
        verify(serviceTaskRepository, times(1)).update(any(ServiceTaskInstanceEntity.class));
        assertEquals(FlowNodeStatus.PENDING, flowTaskInstance.getStatus());
        assertEquals(100L, flowTaskInstance.getTargetTaskId().longValue());
    }

    @Test
    public void testBatchCreate() {
        ServiceTaskInstanceRepository serviceTaskInstanceRepository = mock(ServiceTaskInstanceRepository.class);

        List<FlowTaskInstance> instances = new ArrayList<>();
        instances.add(new FlowTaskInstance(TaskType.TYPE_A, 1L, 1L, new ExecutionStrategyConfig(), true, false, 
            new RuntimeDelegateMapper(), new FlowableAdaptor(), mock(EventPublisher.class), mock(TaskService.class), 
            mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskInstanceRepository));
        instances.add(new FlowTaskInstance(TaskType.TYPE_B, 2L, 2L, new ExecutionStrategyConfig(), false, true, 
            new RuntimeDelegateMapper(), new FlowableAdaptor(), mock(EventPublisher.class), mock(TaskService.class), 
            mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), serviceTaskInstanceRepository));
        
        List<FlowTaskInstance> createdInstances = FlowTaskInstance.batchCreate(instances, serviceTaskInstanceRepository);
        
        verify(serviceTaskInstanceRepository, times(1)).batchCreate(anyList());
        assertEquals(2, createdInstances.size());
        assertEquals(FlowNodeStatus.CREATED, createdInstances.get(0).getStatus());
        assertEquals(FlowNodeStatus.CREATED, createdInstances.get(1).getStatus());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        doCallRealMethod().when(flowTaskInstance).confirmExecute();
        doNothing().when(flowTaskInstance).complete(anyBoolean());
        
        flowTaskInstance.confirmExecute();
        
        verify(flowTaskInstance, times(1)).confirmExecute();
        verify(flowTaskInstance, times(1)).complete(false);
    }

    @Test
    public void testAbort() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class);
        doCallRealMethod().when(flowTaskInstance).abort();
        doNothing().when(flowTaskInstance).complete(anyBoolean());
        
        flowTaskInstance.abort();
        
        verify(flowTaskInstance, times(1)).abort();
        verify(flowTaskInstance, times(1)).complete(true);
    }

    @Test
    public void testCancel() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class, CALLS_REAL_METHODS);
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        
        doNothing().when(mockTargetTaskHandle).cancel(anyBoolean());
        flowTaskInstance.cancel(true);
        
        verify(mockTargetTaskHandle, times(1)).cancel(true);
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class, CALLS_REAL_METHODS);
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        
        when(mockTargetTaskHandle.isDone()).thenReturn(true);
        boolean result = flowTaskInstance.isDone();
        
        assertTrue(result);
    }

    @Test
    public void testGetResult() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class, CALLS_REAL_METHODS);
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        
        doReturn("Result").when(mockTargetTaskHandle).get();
        Object result = flowTaskInstance.getResult();
        
        assertEquals("Result", result);
    }

    @Test
    public void testGetResultWithTimeout() throws Exception {
        FlowTaskInstance flowTaskInstance = mock(FlowTaskInstance.class, CALLS_REAL_METHODS);
        BaseRuntimeFlowableDelegate<?> mockTargetTaskHandle = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(mockTargetTaskHandle);
        
        doReturn("Result").when(mockTargetTaskHandle).get(1000L, TimeUnit.MILLISECONDS);
        Object result = flowTaskInstance.getResult(1000L, TimeUnit.MILLISECONDS);
        
        assertEquals("Result", result);
    }
}