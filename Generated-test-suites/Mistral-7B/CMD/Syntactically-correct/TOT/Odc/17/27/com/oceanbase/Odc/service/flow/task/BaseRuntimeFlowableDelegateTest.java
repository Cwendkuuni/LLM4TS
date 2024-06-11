package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<Integer> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;
    private RetryExecutor retryExecutor;
    private Callable<Integer> callable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);

        delegate = new BaseRuntimeFlowableDelegate<Integer>() {
            @Override
            EventPublisher getEventPublisher() {
                return eventPublisher;
            }

            @Override
            FlowableAdaptor getFlowableAdaptor() {
                return flowableAdaptor;
            }

            @Override
            FlowInstanceRepository getFlowInstanceRepository() {
                return flowInstanceRepository;
            }

            @Override
            RetryExecutor getRetryExecutor() {
                return retryExecutor;
            }

            @Override
            Callable<Integer> initCallable(DelegateExecution execution) {
                this.execution = execution;
                return callable = Mockito.spy(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        Integer result = 1;
                        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", 1, true);
                        return result;
                    }
                });
            }
        };

        lenient().doNothing().when(delegate).initTargetTaskInstanceId(any(DelegateExecution.class));
        lenient().doNothing().when(delegate).preHandle(any(DelegateExecution.class));
        lenient().doNothing().when(delegate).updateFlowInstanceStatus(any(FlowStatus.class));
    }

    @Test
    public void testNormalExecution() throws Exception {
        delegate.run(execution);

        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test(expected = ServiceTaskError.class)
    public void testExceptionDuringInitialization() throws Exception {
        doThrow(new RuntimeException("Initialization error")).when(callable).call();

        delegate.run(execution);

        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));

        PreConditions.expectException(ServiceTaskError.class, "Initialization error");
    }

    @Test
    public void testExceptionDuringExecution() throws Exception {
        doThrow(new RuntimeException("Execution error")).when(callable).call();

        delegate.run(execution);

        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);

        PreConditions.expectException(RuntimeException.class, "Execution error");
    }

    @Test(expected = InterruptedException.class)
    public void testInterruptedExecution() throws Exception {
        Thread.currentThread().interrupt();

        delegate.run(execution);

        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);

        PreConditions.expectException(InterruptedException.class);
    }

    @Test
    public void testTimeoutExecution() throws Exception {
        delegate.get(1, TimeUnit.SECONDS);

        verify(callable).call();
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

}