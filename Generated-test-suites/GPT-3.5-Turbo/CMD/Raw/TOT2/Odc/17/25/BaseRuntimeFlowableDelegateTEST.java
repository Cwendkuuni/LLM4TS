
package com.oceanbase.odc.service.flow.task;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testInit() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initCallable(execution);
        baseRuntimeFlowableDelegate.execute(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testIsDone() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initCallable(execution);
        baseRuntimeFlowableDelegate.execute(execution);
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initCallable(execution);
        baseRuntimeFlowableDelegate.execute(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initCallable(execution);
        baseRuntimeFlowableDelegate.execute(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutThrowsTimeoutException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initCallable(execution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGetThrowsExecutionException() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        baseRuntimeFlowableDelegate.initCallable(execution);
        baseRuntimeFlowableDelegate.execute(execution);
        baseRuntimeFlowableDelegate.throwException(new Exception());
        baseRuntimeFlowableDelegate.get();
    }

}
