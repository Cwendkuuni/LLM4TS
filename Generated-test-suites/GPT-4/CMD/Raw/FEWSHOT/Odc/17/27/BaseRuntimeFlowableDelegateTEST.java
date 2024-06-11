package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseFlowDelegate;
    private final DelegateExecution execution = mock(DelegateExecution.class);

    @Before
    public void setUp() throws Exception {
        baseFlowDelegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Call";
            }
        };

        when(execution.getCurrentActivityId()).thenReturn("ActivityID");
    }

    @After
    public void tearDown() throws Exception {
        baseFlowDelegate = null;
    }
    
    @Test
    public void get() throws InterruptedException, ExecutionException {
        assertNull(baseFlowDelegate.get());
    }

    @Test
    public void getWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        assertNull(baseFlowDelegate.get(2, TimeUnit.SECONDS));
    }    

    @Test
    public void isDone() {
        assertFalse(baseFlowDelegate.isDone());
    }

    @Test
    public void execute() {
        baseFlowDelegate.execute(execution);
        assertEquals("ActivityID", baseFlowDelegate.getActivityId());
    }

    @Test (expected = ExecutionException.class)
    public void run() throws Exception {
        baseFlowDelegate.run(execution);
    }
}