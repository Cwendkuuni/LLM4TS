package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.exception.ServiceTaskError;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MockitoJUnitRunner
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> delegate;

    @Autowired
    private BaseRuntimeFlowableDelegate<String> testInstance;

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String FLOW_INSTANCE_ID = "flowInstanceId";
    private static final String ACTIVITY_ID = "activityId";

    @BeforeAll
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testInstance.setEventPublisher(eventPublisher);
        testInstance.setFlowableAdaptor(flowableAdaptor);
        testInstance.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testInitializeTask() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(FLOW_INSTANCE_ID);
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(createFlowTaskInstance());

        delegate.init(eventPublisher);

        verify(eventPublisher, times(1)).addEventListener(any(ActiveTaskStatisticsListener.class));
        verify(eventPublisher, times(1)).addEventListener(any(TaskInstanceCreatedListener.class));
    }

    @Test
    public void testRunMethodWithSuccess() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(FLOW_INSTANCE_ID);
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(createFlowTaskInstance());
        Callable<String> callable = delegate.initCallable(null);

        delegate.preHandle(mock(DelegateExecution.class));
        String result = callable.call();

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        assertThat(result).isNotNull();
    }

    @Test
    public void testRunMethodWithException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenThrow(new RuntimeException("Exception"));

        delegate.preHandle(mock(DelegateExecution.class));

        try {
            delegate.run(mock(DelegateExecution.class));
        } catch (Exception e) {
            Exception thrownException = e.getCause();
            verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
            verify(flowableAdaptor, never()).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
            assertThat(thrownException).isInstanceOf(ServiceTaskError.class);
        }
    }

    @Test
    public void testPreHandleMethod() {
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(createFlowTaskInstance());

        delegate.preHandle(mock(DelegateExecution.class));

        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(FLOW_INSTANCE_ID);
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(createFlowTaskInstance());
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));

        delegate.preHandle(mock(DelegateExecution.class));
        delegate.run(mock(DelegateExecution.class));

        String result = testInstance.get();

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        assertThat(result).isNotNull();
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch latch = new CountDownLatch(1);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(FLOW_INSTANCE_ID);
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID)).thenReturn(createFlowTaskInstance());
        Callable<String> callable = delegate.initCallable(mock(DelegateExecution.class));

        delegate.preHandle(mock(DelegateExecution.class));
        delegate.run(mock(DelegateExecution.class));

        String result = testInstance.get(10, TimeUnit.SECONDS);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        assertThat(result).isNotNull();
    }

    @Test
    public void testIsDone() {
        delegate.run(mock(DelegateExecution.class));

        assertTrue(testInstance.isDone());
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        FlowStatus flowStatus = FlowStatus.COMPLETED;

        delegate.updateFlowInstanceStatus(flowStatus);

        verify(retryExecutor, times(1)).run(() -> flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, flowStatus), isA(Consumer.class));
    }

    private FlowTaskInstance createFlowTaskInstance() {
        return new FlowTaskInstance();
    }

}