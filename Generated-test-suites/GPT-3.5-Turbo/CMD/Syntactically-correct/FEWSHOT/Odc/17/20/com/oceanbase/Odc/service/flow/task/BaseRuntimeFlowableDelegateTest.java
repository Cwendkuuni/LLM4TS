package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testInitCallable() {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<String> callable = delegate.initCallable(execution);

        assertNotNull(callable);
    }

    @Test
    public void testRun() throws Exception {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.run(execution);

        assertEquals("task123", delegate.getActivityId());
        assertEquals(Long.valueOf(1), delegate.getTargetTaskInstanceId());
        assertEquals(TaskType.USER_TASK, delegate.getTaskType());
        assertNotNull(delegate.getFlowInstanceId());
    }

    @Test
    public void testIsDone() throws Exception {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();

        assertFalse(delegate.isDone());

        delegate.run(mock(DelegateExecution.class));

        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.run(execution);

        assertEquals("Test", delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetException() throws Exception {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.run(execution);
        delegate.setThrown(new Exception("Test Exception"));

        delegate.get();
    }

    @Test
    public void testGetTimeout() throws Exception {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.run(execution);

        assertEquals("Test", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws Exception {
        TestRuntimeFlowableDelegate delegate = new TestRuntimeFlowableDelegate();
        DelegateExecution execution = mock(DelegateExecution.class);

        delegate.get(1, TimeUnit.SECONDS);
    }

    private static class TestRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test";
        }
    }
}