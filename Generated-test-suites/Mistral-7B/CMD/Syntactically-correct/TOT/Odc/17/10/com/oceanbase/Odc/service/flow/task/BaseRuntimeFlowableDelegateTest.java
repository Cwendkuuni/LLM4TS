package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> baseRuntimeFlowableDelegate;
    private DelegateExecution delegateExecution;
    private EventPublisher eventPublisher;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setUp() {
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                // This method is mocked in the test
                return null;
            }
        };
        baseRuntimeFlowableDelegate.init(eventPublisher);
        delegateExecution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testRun_shouldInvokeInitCallableAndUpdateFlowInstanceStatus() throws Exception {
        // Given
        Callable<String> callable = Mockito.mock(Callable.class);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(new FlowTaskInstance()));

        baseRuntimeFlowableDelegate.activityId = "activityId";
        baseRuntimeFlowableDelegate.initTargetTaskInstanceId(delegateExecution);
        baseRuntimeFlowableDelegate.bindToFlowInstance("flowInstance");

        // When
        baseRuntimeFlowableDelegate.run(delegateExecution);

        // Then
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testRun_shouldHandleException() throws Exception {
        // Given
        Callable<String> callable = Mockito.mock(Callable.class);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("Test Exception"));

        baseRuntimeFlowableDelegate.activityId = "activityId";

        // When & Then
        Exception exception = assertThrows(ServiceTaskError.class, () -> baseRuntimeFlowableDelegate.run(delegateExecution));
        assertThat(exception.getCause(), instanceOf(RuntimeException.class));
        assertThat(exception.getCause().getMessage(), is("Test Exception"));
    }

    @Test
    public void testGet_shouldReturnResultWhenDone() throws Exception {
        // Given
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenReturn("Test Result");
        baseRuntimeFlowableDelegate.returnObject = "Test Result";
        baseRuntimeFlowableDelegate.done = true;

        // When
        String result = baseRuntimeFlowableDelegate.get();

        // Then
        assertThat(result, is("Test Result"));
    }

    @Test
    public void testGet_shouldThrowTimeoutExceptionWhenNotDone() throws Exception {
        // Given
        Callable<String> callable = Mockito.mock(Callable.class);
        baseRuntimeFlowableDelegate.latch = new CountDownLatch(1);

        // When & Then
        assertThrows(TimeoutException.class, () -> baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testGet_shouldReturnExecutionExceptionWhenThrown() throws Exception {
        // Given
        Callable<String> callable = Mockito.mock(Callable.class);
        when(callable.call()).thenThrow(new RuntimeException("Test Exception"));
        baseRuntimeFlowableDelegate.thrown = new RuntimeException("Test Exception");

        // When
        Exception exception = baseRuntimeFlowableDelegate.get();

        // Then
        assertThat(exception, instanceOf(ExecutionException.class));
        assertThat(exception.getCause(), instanceOf(RuntimeException.class));
        assertThat(exception.getCause().getMessage(), is("Test Exception"));
    }

    @Test
    public void testBindToFlowInstance_shouldBindTaskToInstance() {
        // Given
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();

        // When
        baseRuntimeFlowableDelegate.bindToFlowInstance(flowTaskInstance);

        // Then
        assertThat(flowTaskInstance.getServiceTask(), is(baseRuntimeFlowableDelegate));
    }
}