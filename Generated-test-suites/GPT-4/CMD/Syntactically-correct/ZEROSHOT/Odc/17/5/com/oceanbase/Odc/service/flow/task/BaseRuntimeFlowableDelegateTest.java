package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.invocation.DelegateInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest<T> {

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    @Mock
    Callable<T> task;

    @InjectMocks
    BaseRuntimeFlowableDelegate<T> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        doNothing().when(execution).setVariable("myVar", "myValue");
        when(execution.getCurrentActivityId()).thenReturn("123");
        when(flowTaskInstance.getId()).thenReturn(1L);
        when(flowTaskInstance.getTaskType()).thenReturn(TaskType.SOME_TASK);
        when(flowTaskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(flowTaskInstance.dealloc()).thenReturn(Void);
    }

    @Test
    public void testRun() {
        //Initialization
        baseRuntimeFlowableDelegate.init(eventPublisher);
        
        //Execution
        try {
            baseRuntimeFlowableDelegate.run(execution);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Assertions
        assertEquals("123", baseRuntimeFlowableDelegate.getActivityId());
        assertNotNull(baseRuntimeFlowableDelegate.getStrategyConfig());
    }
    
    @Test(expected = ServiceTaskError.class)
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
    }

    @Test
    public void testIsDone() {
        //Initialization
        baseRuntimeFlowableDelegate.init(eventPublisher);

        //Execution
        try {
            baseRuntimeFlowableDelegate.run(execution);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Assert
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        assertEquals("Expected value", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException {
        assertEquals("Expected value", baseRuntimeFlowableDelegate.get(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);
        assertEquals(1L, baseRuntimeFlowableDelegate.getTargetTaskInstanceId().longValue());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(FlowStatus.CANCELLED);
        assertEquals(FlowStatus.CANCELLED, baseRuntimeFlowableDelegate.getFlowStatus());
    }
}
