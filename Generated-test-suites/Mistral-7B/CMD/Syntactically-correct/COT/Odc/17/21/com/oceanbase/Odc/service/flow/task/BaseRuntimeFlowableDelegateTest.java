package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private FlowableAdaptor flowableAdaptor;
    private RetryExecutor retryExecutor;

    @Before
    public void setup() {
        eventPublisher = Mockito.mock(EventPublisher.class);
        flowInstanceRepository = Mockito.mock(FlowInstanceRepository.class);
        flowableAdaptor = Mockito.mock(FlowableAdaptor.class);
        retryExecutor = Mockito.mock(RetryExecutor.class);
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void init(EventPublisher eventPublisher) {
                super.init(eventPublisher);
            }

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "TestResult";
            }
        };
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setRetryExecutor(retryExecutor);
        delegate.setEventPublisher(eventPublisher);
        execution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivityId", 1L)).thenReturn(Optional.of(new FlowTaskInstance()));

        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(Mockito.any(ServiceTaskStartedEvent.class));
        ArgumentCaptor<FlowStatus> flowStatusCaptor = ArgumentCaptor.forClass(FlowStatus.class);
        verify(flowInstanceRepository, times(1)).updateStatusById(1L, flowStatusCaptor.capture());
        assertEquals(FlowStatus.RUNNING, flowStatusCaptor.getValue());

        assertEquals("TestResult", delegate.get());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunException() throws Exception {
        doThrow(new RuntimeException("TestException")).when(delegate).preHandle(execution);

        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(Mockito.any(ServiceTaskStartedEvent.class));
        ArgumentCaptor<String> errorMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(log, times(1)).warn("Service task execution failed", errorMessageCaptor.capture());
        assertEquals("TestException", errorMessageCaptor.getValue());

        throw new ServiceTaskError("TestException");
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        delegate.initTargetTaskInstanceId(execution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId");
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId("testActivityId", 1L);
        verify(flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

        delegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        delegate.updateFlowInstanceStatus(FlowStatus.SUCCEEDED);

        verify(retryExecutor, times(1)).run(() -> {
            try {
                flowInstanceRepository.updateStatusById(1L, FlowStatus.SUCCEEDED);
                return null;
            } catch (Exception e) {
                return null;
            }
        }, any());
    }
}