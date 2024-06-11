
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.exception.ServiceTaskError;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };
    }

    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate.init(null);
        assertNotNull(baseRuntimeFlowableDelegate.getEventPublisher());
    }

    @Test
    public void testRun() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunExceptionHandling() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new Exception("Test Exception");
                };
            }
        };
        baseRuntimeFlowableDelegate.run(execution);
    }

    @Test
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetExceptionHandling() throws Exception {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    throw new Exception("Test Exception");
                };
            }
        };
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutExceptionHandling() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testIsDone() throws Exception {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testConstructor() {
        assertNotNull(baseRuntimeFlowableDelegate.getRetryExecutor());
        assertNotNull(baseRuntimeFlowableDelegate.getLatch());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        // Test Binding Logic Here
    }

    // Additional Test Methods for Other Public Methods in BaseRuntimeFlowableDelegate

}

