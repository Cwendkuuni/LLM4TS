package com.oceanbase.odc.service.flow.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.flowable.engine.delegate.DelegateExecution;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private Callable<String> callableMock;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };
    }

    @Test
    public void testIsDone() {
        delegate.execute(execution);
        boolean done = delegate.isDone();
        assertEquals(true, done);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.execute(execution);
        String result = "TestResult";
        when(callableMock.call()).thenReturn(result);
        String actualResult = delegate.get();
        assertNotNull(actualResult);
        assertEquals(result, actualResult);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.execute(execution);
        String result = "TestResult";
        when(callableMock.call()).thenReturn(result);
        String actualResult = delegate.get(1, TimeUnit.SECONDS);
        assertNotNull(actualResult);
        assertEquals(result, actualResult);
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws InterruptedException, ExecutionException {
        delegate.execute(execution);
        when(callableMock.call()).thenThrow(new RuntimeException("Test Exception"));
        delegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.execute(execution);
        when(callableMock.call()).then(invocation -> {
            Thread.sleep(2000);
            return "TestResult";
        });
        delegate.get(1, TimeUnit.SECONDS);
    }
}