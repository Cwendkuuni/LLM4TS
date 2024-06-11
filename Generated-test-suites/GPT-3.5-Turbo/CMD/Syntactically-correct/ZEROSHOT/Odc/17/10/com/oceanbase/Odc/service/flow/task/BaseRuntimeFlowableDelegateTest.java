package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;

    @Mock
    private DelegateExecution execution;

    @Mock
    private Callable<String> callable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRun_Success() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        
        baseRuntimeFlowableDelegate.run(execution);
        
        verify(execution, times(1)).getCurrentActivityId();
        verify(callable, times(1)).call();
    }

    @Test
    public void testIsDone() {
        assertEquals(false, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(execution);
        
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test(expected = TimeoutException.class)
    public void testGet_TimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(execution);
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testInitCallable() {
        Callable<String> result = baseRuntimeFlowableDelegate.initCallable(execution);
        
        assertEquals(callable, result);
    }

}