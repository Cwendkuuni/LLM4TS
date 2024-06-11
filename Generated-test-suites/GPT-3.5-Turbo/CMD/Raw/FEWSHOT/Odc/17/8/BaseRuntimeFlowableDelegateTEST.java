package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowTaskInstance flowTaskInstance;

    // Test initCallable method for returning a valid Callable instance
    @Test
    public void testInitCallable() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        Callable<String> callable = delegate.initCallable(execution);
        assertNotNull(callable);

        try {
            String result = callable.call();
            assertEquals("Test Callable", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test run method for successful execution and task handling
    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);

        Mockito.when(execution.getCurrentActivityId()).thenReturn("activity123");
        Mockito.when(flowableAdaptor.getTaskInstanceByActivityId("activity123", 1L)).thenReturn(Optional.of(flowTaskInstance));

        delegate.execute(execution);

        assertTrue(delegate.isDone());
        assertEquals("Test Callable", delegate.get());
    }

    // Test get method for successful retrieval of result
    @Test
    public void testGet() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.execute(execution);

        assertEquals("Test Callable", delegate.get());
    }

    // Test get method for timeout exception
    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(2000);
                    return "Test Callable";
                };
            }
        };

        delegate.execute(execution);

        delegate.get(1, TimeUnit.SECONDS);
    }

    // Test updateFlowInstanceStatus method for successful update
    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.setFlowInstanceRepository(flowInstanceRepository);

        Mockito.when(flowInstanceRepository.updateStatusById(1L, FlowStatus.RUNNING)).thenReturn(true);
        delegate.updateFlowInstanceStatus(FlowStatus.RUNNING);

        Mockito.verify(flowInstanceRepository, Mockito.times(1)).updateStatusById(1L, FlowStatus.RUNNING);
    }

    // Test bindToFlowTaskInstance method for successful binding
    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.setTargetTaskInstanceId(1L);
        delegate.setActivityId("activity123");
        delegate.bindToFlowTaskInstance(flowTaskInstance);

        Mockito.verify(flowTaskInstance, Mockito.times(1)).bindServiceTask(delegate);
    }

    // Test preHandle method for successful execution
    @Test
    public void testPreHandle() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "Test Callable";
            }
        };

        delegate.preHandle(execution);
        // Add your assertions here if preHandle has specific behavior
    }
}