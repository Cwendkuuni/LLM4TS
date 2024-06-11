package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import lombok.SneakyThrows;

/**
 * JUnit test cases for BaseRuntimeFlowableDelegate class
 */
@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @org.junit.Before
    public void setUp() {
        // Initialize mocks
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        RetryExecutor retryExecutor = mock(RetryExecutor.class);
        ActiveTaskStatisticsListener activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        TaskInstanceCreatedListener taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);

        // Initialize test subject
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        // Implementation of the callable goes here
                        return null;
                    }
                };
            }
        };

        // Set up dependencies
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
        delegate.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), eq(1L)))
                .thenReturn(Mockito.mock(FlowTaskInstance.class));
    }

    /**
     * Test initialization of methods
     */
    @org.junit.Test
    public void testInitialization() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>();
        assertNotNull(delegate.getActivityId());
        assertNull(delegate.getTargetTaskInstanceId());
        assertNull(delegate.getTaskType());
        assertNull(delegate.getFlowInstanceId());
        assertNull(delegate.getStrategyConfig());
        assertNull(delegate.getFlowableAdaptor());
        assertNull(delegate.getEventPublisher());
        assertNull(delegate.getFlowInstanceRepository());
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
        assertNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertFalse(delegate.isDone());
        assertNull(delegate.getLatch());
    }

    /**
     * Test initialization of methods with dependencies
     */
    @org.junit.Test
    public void testInitializationWithDependencies() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return null;
            }
        };
        delegate.init(Mockito.mock(EventPublisher.class));
        assertNotNull(delegate.getEventPublisher());
        assertTrue(delegate.getActiveTaskStatisticsListener() instanceof ActiveTaskStatisticsListener);
        assertTrue(delegate.getTaskInstanceCreatedlistener() instanceof TaskInstanceCreatedListener);
    }

    /**
     * Test execution of method run, including initialization and method call
     */
    @SneakyThrows
    @org.junit.Test
    public void testRun() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> 1;
            }

            @Override
            protected void preHandle(DelegateExecution execution) {
                // preHandle implementation goes here
            }
        };

        // Set up execution
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        // Trigger run method
        delegate.run(execution);

        // Verify results
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        assertNotNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertTrue(delegate.isDone());
    }

    /**
     * Test execution of method run with exception
     */
    @org.junit.Test
    public void testRunWithException() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Test exception");
            }
        };

        // Set up execution
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        try {
            // Trigger run method
            delegate.run(execution);
            fail("Expected exception to be thrown");
        } catch (ServiceTaskError e) {
            // Verify exception
            assertEquals("Test exception", e.getCause().getMessage());
            assertTrue(delegate.isDone());
        }
    }

    /**
     * Test execution of method get
     */
    @org.junit.Test
    public void testGet() throws Exception {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> 1;
            }
        };

        // Trigger run method
        delegate.run(Mockito.mock(DelegateExecution.class));

        // Verify results
        assertEquals(1, delegate.get());
    }

    /**
     * Test execution of method get with timeout
     */
    @org.junit.Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> {
                    Thread.sleep(1001);
                    return 1;
                };
            }
        };

        // Trigger run method
        delegate.run(Mockito.mock(DelegateExecution.class));

        // Wait for timeout
        delegate.get(1, TimeUnit.SECONDS);

        // Verify exception
        fail("Unexpected result");
    }

    /**
     * Test initialization of targetTaskInstanceId
     */
    @org.junit.Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        delegate.initTargetTaskInstanceId(execution);

        assertEquals(1, delegate.getFlowInstanceId());
        assertEquals(TaskType.TEST_TASK_TYPE, delegate.getTaskType());
        assertEquals(1L, delegate.getTargetTaskInstanceId());
    }

    /**
     * Test binding to flowTaskInstance
     */
    @org.junit.Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance).bindServiceTask(any());
    }

    /**
     * Test updating flowInstanceStatus
     */
    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<Integer> delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return null;
            }
        };

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(eq(1), eq(FlowStatus.COMPLETED));
    }
}