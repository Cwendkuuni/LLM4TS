package com.oceanbase.odc.service.flow.task;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.task.BaseRuntimeFlowableDelegate;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate delegate;
    private DelegateExecution execution;
    private Callable callable;

    @Before
    public void setup() throws Exception {
        callable = mock(Callable.class);
        execution = mock(DelegateExecution.class);
        delegate = new BaseRuntimeFlowableDelegate() {

            @Override
            protected Callable initCallable(DelegateExecution execution) {
                return callable;
            }
        };
    }

    @Test(expected = ServiceTaskError.class)
    public void run_WithInitializationError_ShouldThrowServiceTaskError() throws Exception {
        doThrow(Exception.class).when(callable).call();
        delegate.run(execution);
    }

    @Test
    public void run_WithExecutionError_ShouldBeDone() throws Exception {
        doThrow(Exception.class).when(callable).call();
        try {
            delegate.run(execution);
        } catch (Exception ignored) {}
        assertTrue(delegate.isDone());
    }

    @Test
    public void run_WithSuccessfulExecution_ShouldReturnObject() throws Exception {
        String expectedReturn = "Expected Return";
        when(callable.call()).thenReturn(expectedReturn);

        delegate.run(execution);
        Object actualReturn = delegate.get();
        assertEquals(expectedReturn, actualReturn);
    }

    @Test(expected = ExecutionException.class)
    public void getWhenError_ShouldThrowExecutionException() throws Exception {
        doThrow(Exception.class).when(callable).call();
        try {
            delegate.run(execution);
        } catch (Exception ignored) {}

        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void getWithTimeout_WhenNotDone_ShouldThrowTimeoutException() throws Exception {
        delegate.get(1, TimeUnit.MILLISECONDS);
    }

}