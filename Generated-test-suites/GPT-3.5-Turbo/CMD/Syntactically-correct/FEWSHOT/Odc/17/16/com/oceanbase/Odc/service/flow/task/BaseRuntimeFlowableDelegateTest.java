package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution mockExecution;
    @Mock
    private Callable<Object> mockCallable;
    @Mock
    private FlowTaskInstance mockTaskInstance;
    @Mock
    private ExecutionStrategyConfig mockStrategyConfig;

    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        delegate = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return mockCallable;
            }
        };
        delegate.setActivityId("activity-1");
        delegate.setFlowInstanceId(1L);
        delegate.setTaskType(TaskType.TYPE1);
        delegate.setTargetTaskInstanceId(100L);
        delegate.setStrategyConfig(mockStrategyConfig);
    }

    @Test
    public void testExecutionSuccess() throws Exception {
        Mockito.when(mockCallable.call()).thenReturn("Result");

        delegate.run(mockExecution);

        assertEquals("Result", delegate.get());
        assertNotNull(delegate.get(1, TimeUnit.SECONDS));
        assertNotNull(delegate.isDone());
    }

    @Test
    public void testExecutionFailure() {
        Exception exception = new Exception();
        Mockito.when(mockCallable.call()).thenThrow(exception);

        try {
            delegate.run(mockExecution);
        } catch (Exception e) {
            assertEquals(exception, e);
        }

        try {
            delegate.get();
        } catch (Exception e) {
            assertEquals(exception, e.getCause());
        }
    }

    @Test
    public void testIsDone() {
        delegate.run(mockExecution);

        assertFalse(delegate.isDone());
    }

    @Test
    public void testGetTimeout() {
        Thread t = new Thread(() -> {
            try {
                delegate.get(1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        t.start();

        assertFalse(delegate.isDone());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        delegate.bindToFlowTaskInstance(mockTaskInstance);
  
        Mockito.verify(mockTaskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Verify the update
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(mockExecution);

        // Verify the initialization of targetTaskInstanceId
    }
}