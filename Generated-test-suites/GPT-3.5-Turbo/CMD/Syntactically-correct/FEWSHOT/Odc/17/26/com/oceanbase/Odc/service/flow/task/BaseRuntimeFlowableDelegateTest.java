package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Result";
            }
        };
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("testActivityId");
        
        delegate.initTargetTaskInstanceId(execution);
        
        assertEquals("testProcessDefinitionId", delegate.getFlowInstanceId());
        assertEquals("testActivityId", delegate.getActivityId());
        assertEquals("Test", delegate.getTaskType());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);

        delegate.setTargetTaskInstanceId(1L);
        delegate.setActivityId("TestActivityId");

        delegate.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setFlowInstanceId(1L);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testInitCallable() {
        DelegateExecution execution = mock(DelegateExecution.class);
        
        Callable<String> callable = delegate.initCallable(execution);
        
        assertNotNull(callable);
    }

    @Test
    public void testExecute() throws ExecutionException, InterruptedException {
        DelegateExecution execution = mock(DelegateExecution.class);
        
        delegate.initTargetTaskInstanceId(execution);
        
        delegate.execute(execution);
        
        Future<String> future = delegate;
        
        assertEquals("Test Result", future.get());
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        delegate.setReturnObject("Test Result");
        delegate.setDone(true);

        assertEquals("Test Result", delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws ExecutionException, InterruptedException {
        delegate.setThrown(new Exception("Test Exception"));
        delegate.setDone(true);

        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

}