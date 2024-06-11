package com.oceanbase.odc.service.flow.task;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class BaseRuntimeFlowableDelegateTest {

    // Test for init method
    @Test
    public void testInit() {
        //Mock objects
        EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
        
        BaseRuntimeFlowableDelegate delegator = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);

        delegator.init(eventPublisher);
        
        //Verifying event listeners are added
        Mockito.verify(eventPublisher).addEventListener(delegator.getActiveTaskStatisticsListener());
        Mockito.verify(eventPublisher).addEventListener(delegator.getTaskInstanceCreatedlistener());
    }

    // Test for execute method
    @Test
    public void testExecute() {
        //Mock objects
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        BaseRuntimeFlowableDelegate delegator = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);

        delegator.execute(execution);
        
        //Verify method calls
        Mockito.verify(delegator).run(execution);
    }

    // Test for isDone method
    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate delegator = new BaseRuntimeFlowableDelegate<Void>();

        assertFalse(delegator.isDone());
        // After set done = true
        delegator.setDone(true);
        assertTrue(delegator.isDone());
    }

    // Test for get method
    @Test(expected = ExecutionException.class)
    public void testGet() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate delegator = new BaseRuntimeFlowableDelegate<Void>();
        delegator.setThrown(new Exception());
        delegator.get();
    }
    
    // Test for get(long timeout, TimeUnit unit) method
    @Test(expected = TimeoutException.class)
    public void testGet_withTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate delegator = new BaseRuntimeFlowableDelegate<Void>();
        delegator.get(1, TimeUnit.MILLISECONDS);
    }
    
    // Test for bindToFlowTaskInstance method
    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate delegator = new BaseRuntimeFlowableDelegate<Void>();
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(delegator.getTargetTaskInstanceId());
        delegator.bindToFlowTaskInstance(taskInstance);
    }

}