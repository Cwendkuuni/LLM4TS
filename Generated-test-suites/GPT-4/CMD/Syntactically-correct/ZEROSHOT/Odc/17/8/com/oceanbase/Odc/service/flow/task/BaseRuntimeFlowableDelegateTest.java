
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.DelegateExecutionImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private Callable<String> callable;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = new MockRuntimeFlowableDelegate();
        when(delegateExecution.getCurrentActivityId()).thenReturn("activityId");
    }

    @Test
    public void testPreHandle() {
        delegate.preHandle(delegateExecution);
        verify(delegateExecution, times(1)).getCurrentActivityId();
    }

    @Test
    public void testIsDone() {
        assert (!delegate.isDone());
    }

    @Test(expected = InterruptedException.class)
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.get(1000, TimeUnit.MILLISECONDS);
    }

    private class MockRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return callable;
        }
    }
}
