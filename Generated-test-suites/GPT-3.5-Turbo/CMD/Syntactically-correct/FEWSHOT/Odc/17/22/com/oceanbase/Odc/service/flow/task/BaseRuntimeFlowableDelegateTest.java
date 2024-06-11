
package com.oceanbase.odc.service.flow.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "TestString";
            }
        };
        
        Callable<String> callable = delegate.initCallable(Mockito.mock(DelegateExecution.class));
        assertNotNull(callable);
        try {
            String result = callable.call();
            assertEquals("TestString", result);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testRun() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "TestString";
            }
        };

        try {
            delegate.run(Mockito.mock(DelegateExecution.class));
            assertTrue(delegate.isDone());
            assertEquals("TestString", delegate.get());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test(expected = ExecutionException.class)
    public void testRunWithException() throws InterruptedException, ExecutionException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new RuntimeException("Test Exception");
                };
            }
        };

        delegate.run(Mockito.mock(DelegateExecution.class));
        assertTrue(delegate.isDone());
        delegate.get();
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(1000); // delay for 5 seconds
                    }
                    return "TestString";
                };
            }
        };

        delegate.run(Mockito.mock(DelegateExecution.class));
        assertTrue(delegate.isDone());
        assertEquals("TestString", delegate.get(3, TimeUnit.SECONDS));
    }
}
