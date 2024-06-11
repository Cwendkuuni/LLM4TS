package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class BaseRuntimeFlowableDelegateTest {

    @Spy
    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<String>() {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test";
        }
    };

    @Mock
    private DelegateExecution execution;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
        assertNotNull(baseRuntimeFlowableDelegate.getFlowInstanceId());
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
        assertNull(baseRuntimeFlowableDelegate.getEventPublisher());
    }

    @Test
    public void testIsDone() {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals(true, baseRuntimeFlowableDelegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get());
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.run(execution);
        assertEquals("Test", baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);
        baseRuntimeFlowableDelegate.setTargetTaskInstanceId(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(baseRuntimeFlowableDelegate);
    }

    // Add more test cases as needed

}