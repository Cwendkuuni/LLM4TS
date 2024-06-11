package com.oceanbase.odc.service.flow.task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(() -> "Test Result");
        baseRuntimeFlowableDelegate.run(execution);
        assertTrue(baseRuntimeFlowableDelegate.isDone());
        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRun_Exception() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(baseRuntimeFlowableDelegate.initCallable(execution)).thenReturn(() -> {
            throw new Exception("Test Exception");
        });
        baseRuntimeFlowableDelegate.run(execution);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.complete("Test Result");
        assertEquals("Test Result", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGet_Timeout() throws InterruptedException, ExecutionException, TimeoutException {
        long timeout = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        baseRuntimeFlowableDelegate.complete("Test Result");
        assertEquals("Test Result", baseRuntimeFlowableDelegate.get(timeout, unit));
    }
}