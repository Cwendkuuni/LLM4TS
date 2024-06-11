package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@ExtendWith(MockitoExtension.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @NonNull
    private BaseRuntimeFlowableDelegate<String> delegate;

    @NonNull
    private Execution execution;

    @NonNull
    private Callable<String> callable;

    @BeforeEach
    public void setup() {
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                this.eventPublisher = eventPublisher;
                this.flowInstanceRepository = flowInstanceRepository;
                this.flowableAdaptor = flowableAdaptor;
                return callable = () -> "Result";
            }
        };
        execution = Executions.newDelegateExecution();
    }

    @Test
    public void testInitCallable() {
        // Given
        callable = delegate.initCallable(execution);

        // When
        String result = null;
        try {
            result = callable.call();
        } catch (Exception e) {
            Assertions.fail("Should not throw exception");
        }

        // Then
        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertEquals("Result", result, "Result should be equal to 'Result'");
    }

    @Test
    public void testRunSuccess() throws Exception {
        // Given
        delegate.activityId = "ActivityId";
        delegate.initTargetTaskInstanceId(execution);
        delegate.preHandle(execution);

        // When
        String result = callable.call();
        delegate.postHandle(execution, result);

        // Then
        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertEquals("Result", result, "Result should be equal to 'Result'");
        Assertions.assertFalse(execution.isTerminated(), "Execution should not be terminated");
        Assertions.assertFalse(delegate.isDone(), "Delegate should not be done");

        // Verify event publishing
        ServiceTaskStartedEvent event = (ServiceTaskStartedEvent) Assertions.assertNotNull(
                eventPublisher.getPublishedEvents().stream().filter(e -> e instanceof ServiceTaskStartedEvent).findFirst().get());
        Assertions.assertEquals(delegate, event.getTask(), "Event task should be equal to the delegate");
        Assertions.assertEquals(Thread.currentThread(), event.getThread(), "Event thread should be equal to the current thread");
    }

    @Test
    public void testRunException() throws Exception {
        // Given
        Callable<String> exceptionCallable = () -> {
            throw new RuntimeException("Exception");
        };
        delegate.callable = exceptionCallable;

        // When
        try {
            callable.call();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            Assertions.assertEquals("Exception", cause.getMessage(), "Exception message should be 'Exception'");
        }

        // Then
        Assertions.assertFalse(execution.isTerminated(), "Execution should not be terminated");
        Assertions.assertTrue(delegate.isDone(), "Delegate should be done");
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        // Given
        FlowStatus newStatus = FlowStatus.COMPLETED;

        // When
        delegate.updateFlowInstanceStatus(newStatus);

        // Then
        Assertions.assertTrue(flowInstanceRepository.updateStatusById(flowInstanceId, newStatus).isPresent(),
                "Flow instance status should be updated");
    }
}