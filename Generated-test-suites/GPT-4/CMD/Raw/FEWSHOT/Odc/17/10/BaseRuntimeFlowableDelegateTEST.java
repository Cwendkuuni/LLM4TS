
package com.oceanbase.odc.service.flow.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    private BaseRuntimeFlowableDelegateTestable delegateTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegateTest = new BaseRuntimeFlowableDelegateTestable();
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws ExecutionException, InterruptedException {
        Mockito.when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException());
        delegateTest.execute(delegateExecution);
        delegateTest.get();
    }

    @Test
    public void testGet() throws ExecutionException, InterruptedException {
        String activityId = "activityId";
        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn(activityId);
        delegateTest.execute(delegateExecution);
        assertNull(delegateTest.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutException() throws ExecutionException, InterruptedException, TimeoutException {
        Mockito.when(delegateExecution.getCurrentActivityId()).thenThrow(new RuntimeException());
        delegateTest.execute(delegateExecution);
        delegateTest.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetWithTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        String activityId = "activityId";
        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn(activityId);
        delegateTest.execute(delegateExecution);
        assertNull(delegateTest.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testIsDone() {
        assertFalse(delegateTest.isDone());
  
        String activityId = "activityId";
        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn(activityId);
        delegateTest.execute(delegateExecution);
        assertTrue(delegateTest.isDone());
    }

    @Test
    public void testRun() {
        String activityId = "activityId";
        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn(activityId);
        delegateTest.execute(delegateExecution);
        assertNotNull(delegateTest.getActivityId());
    }
}
