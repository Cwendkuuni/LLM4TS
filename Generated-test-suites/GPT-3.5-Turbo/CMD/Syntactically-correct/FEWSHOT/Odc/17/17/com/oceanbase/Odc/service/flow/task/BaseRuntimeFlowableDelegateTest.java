package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test";
            }
        };

        Executor execution = Mockito.mock(DelegateExecution.class);
        assertEquals("Test", delegate.call());

        try {
            delegate.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        preHandle(execution);
        delegate.execute(execution);
        delegate.execute(execution);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Init Test";
            }
        };

        Executor execution = Mockito.mock(DelegateExecution.class);
        delegate.initTargetTaskInstanceId(execution);
        assertNotNull(delegate.getFlowInstanceId());
        assertNotNull(delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Bind Test";
            }
        };

        Executor execution = Mockito.mock(DelegateExecution.class);
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        Mockito.when(taskInstance.getId()).thenReturn(12345L);
        delegate.bindToFlowTaskInstance(taskInstance);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Update Test";
            }
        };

        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getFlowInstanceRepository());
        delegate.updateFlowInstanceStatus(FlowStatus.FAILED);
    }
}