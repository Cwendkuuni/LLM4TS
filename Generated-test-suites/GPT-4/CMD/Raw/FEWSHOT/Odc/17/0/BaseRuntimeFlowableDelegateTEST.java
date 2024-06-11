
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.DelegateExecutionImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class BaseRuntimeFlowableDelegateTest {

    private static class TestingFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Success";
        }
    }

    @Test
    public void testDefaultMethods() throws ExecutionException, InterruptedException {

        ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);

        // Create an instance
        TestingFlowableDelegate delegate = new TestingFlowableDelegate();
        delegate.init(publisher);
 
        assertFalse(delegate.isDone());

        DelegateExecution execution = new DelegateExecutionImpl();
        delegate.run(execution); // Suppose we are running this method in the same thread

        assertTrue(delegate.isDone());

        assertEquals("Success", delegate.get());
        assertEquals("Success", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testExceptionInInitCallable() throws InterruptedException, ExecutionException {
        ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);

        TestingFlowableDelegate delegate = new TestingFlowableDelegate() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Error in initCallable");
            }
        };

        delegate.init(publisher);

        DelegateExecution execution = new DelegateExecutionImpl();
        delegate.run(execution);

        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testExceptionInCallableCall() throws InterruptedException, ExecutionException {
        ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);

        TestingFlowableDelegate delegate = new TestingFlowableDelegate() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {throw new Exception("Error in Callable.call");};
            }
        };

        delegate.init(publisher);

        DelegateExecution execution = new DelegateExecutionImpl();
        delegate.run(execution);

        delegate.get();
    }
}
