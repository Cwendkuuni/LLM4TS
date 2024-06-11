
package com.oceanbase.odc.service.flow.instance;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.task.mapper.RuntimeDelegateMapper;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.SequenceInstanceRepository;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.ServiceTaskInstanceRepository;

public class FlowTaskInstanceTest {
    
    private FlowTaskInstance flowTaskInstance;
    private ServiceTaskInstanceRepository serviceTaskInstanceRepositoryMock;
    private TaskService taskServiceMock;
    private EventPublisher eventPublisherMock;
    private RuntimeDelegateMapper delegateMapperMock;
    private Task taskMock;
    
    @Before
    public void setUp() {
        serviceTaskInstanceRepositoryMock = mock(ServiceTaskInstanceRepository.class);
        taskServiceMock = mock(TaskService.class);
        eventPublisherMock = mock(EventPublisher.class);
        delegateMapperMock = mock(RuntimeDelegateMapper.class);
        taskMock = mock(Task.class);
        
        flowTaskInstance = new FlowTaskInstance(TaskType.TYPE_A, 123L, 456L, 
            new ExecutionStrategyConfig(), true, false, delegateMapperMock, 
            mock(FlowableAdaptor.class), eventPublisherMock, taskServiceMock, 
            mock(NodeInstanceEntityRepository.class), mock(SequenceInstanceRepository.class), 
            serviceTaskInstanceRepositoryMock);
    }

    @Test
    public void testBindServiceTask() {
        BaseRuntimeFlowableDelegate<?> targetDelegateMock = mock(BaseRuntimeFlowableDelegate.class);
        flowTaskInstance.bindServiceTask(targetDelegateMock);
        
        assertEquals(targetDelegateMock, flowTaskInstance.getTargetTaskHandle());
        assertEquals(FlowNodeStatus.EXECUTING, flowTaskInstance.getStatus());
    }
    
    @Test
    public void testCancel() throws Exception {
        BaseRuntimeFlowableDelegate<?> targetDelegateMock = mock(BaseRuntimeFlowableDelegate.class);
        when(taskMock.getId()).thenReturn("testId");
        when(targetDelegateMock.getTargetTaskInstanceId()).thenReturn(flowTaskInstance.getId());
        flowTaskInstance.bindServiceTask(targetDelegateMock);
        
        flowTaskInstance.cancel(true);
        
        verify(targetDelegateMock).cancel(true);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCancelWhenTaskNotOnMachine() throws Exception {
        flowTaskInstance.cancel(true);
    }
    
    @Test
    public void testIsDone() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<?> targetDelegateMock = mock(BaseRuntimeFlowableDelegate.class);
        when(targetDelegateMock.isDone()).thenReturn(true);
        flowTaskInstance.bindServiceTask(targetDelegateMock);
        
        assertTrue(flowTaskInstance.isDone());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testIsDoneWhenTaskNotOnMachine() throws InterruptedException, ExecutionException {
        flowTaskInstance.isDone();
    }
    
    @Test
    public void testGetResult() throws InterruptedException, ExecutionException {
        Object expectedResult = "Test Result";
        BaseRuntimeFlowableDelegate<?> targetDelegateMock = mock(BaseRuntimeFlowableDelegate.class);
        when(targetDelegateMock.get()).thenReturn(expectedResult);
        flowTaskInstance.bindServiceTask(targetDelegateMock);
        
        assertEquals(expectedResult, flowTaskInstance.getResult());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWhenTaskNotOnMachine() throws InterruptedException, ExecutionException {
        flowTaskInstance.getResult();
    }
    
    @Test
    public void testGetResultWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Object expectedResult = "Test Result";
        BaseRuntimeFlowableDelegate<?> targetDelegateMock = mock(BaseRuntimeFlowableDelegate.class);
        when(targetDelegateMock.get(1, TimeUnit.SECONDS)).thenReturn(expectedResult);
        flowTaskInstance.bindServiceTask(targetDelegateMock);
        
        assertEquals(expectedResult, flowTaskInstance.getResult(1, TimeUnit.SECONDS));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetResultWithTimeoutWhenTaskNotOnMachine() throws InterruptedException, ExecutionException, TimeoutException {
        flowTaskInstance.getResult(1, TimeUnit.SECONDS);
    }
    
    @Test
    public void testResourceType() {
        assertEquals("ODC_FLOW_TASK_INSTANCE", flowTaskInstance.resourceType());
    }

}
