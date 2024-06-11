package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.core.shared.constant.FlowStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Test(expected = NullPointerException.class)
    public void testInit() {
        EventPublisher eventPublisher = null;
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.init(eventPublisher);
    }    

    @Test(expected = NullPointerException.class)
    public void testRunWithNullExecution() throws Exception {
        DelegateExecution execution = null;        
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.run(execution);
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullExecution() {
        DelegateExecution execution = null;
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.execute(execution);
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        boolean result = baseRuntimeFlowableDelegate.isDone();
        assertFalse(result);
    }

    @Test(expected = ExecutionException.class)
    public void testGet() {
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() {
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testBindToFlowTaskInstanceWithNullInstance() {
        FlowTaskInstance taskInstance = null;
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
    }
    
    @Test(expected = NullPointerException.class)
    public void testUpdateFlowInstanceStatusWithNullStatus() {
        FlowStatus flowStatus = null;
	    BaseRuntimeFlowableDelegate baseRuntimeFlowableDelegate = mock(BaseRuntimeFlowableDelegate.class, CALLS_REAL_METHODS);
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(flowStatus);
    }
}