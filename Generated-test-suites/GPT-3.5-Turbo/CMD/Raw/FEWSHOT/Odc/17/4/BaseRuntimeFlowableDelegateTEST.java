package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Success";
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testRun() throws Exception {
        delegate.run(execution);
        assertTrue(delegate.isDone());
        assertEquals("Test Success", delegate.get());
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);
        verify(eventPublisher).removeEventListener(any());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.run(execution);
        assertEquals("Test Success", delegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        assertEquals("Test Success", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(new FlowTaskInstance()));
        
        delegate.initTargetTaskInstanceId(execution);
        
        assertEquals("activityId", delegate.getActivityId());
        assertEquals(Long.valueOf(1), delegate.getFlowInstanceId());
        assertEquals(Long.valueOf(0), delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(0L);
        delegate.setTargetTaskInstanceId(0L);
        
        delegate.bindToFlowTaskInstance(taskInstance);
        
        verify(taskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.setFlowInstanceId(1L);
        
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, FlowStatus.COMPLETED);
    }
}