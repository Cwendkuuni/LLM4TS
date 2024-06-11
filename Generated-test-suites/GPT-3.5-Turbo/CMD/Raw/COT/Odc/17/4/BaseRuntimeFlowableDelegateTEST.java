package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        delegate.init(mock(EventPublisher.class));
        assertNotNull(delegate.getEventPublisher());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        assertEquals(false, delegate.isDone());
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        delegate.get();
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGetWithTimeout() throws ExecutionException, TimeoutException, InterruptedException {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        delegate.get(1, TimeUnit.SECONDS);
        assertEquals(true, delegate.isDone());
    }

    // Additional Edge Cases and Exception Handling Scenarios to be Tested

}

