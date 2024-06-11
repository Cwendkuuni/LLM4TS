package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentMatchers;
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

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 */
public class BaseRuntimeFlowableDelegateTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>();
        assertNull(delegate.getActivityId());
        assertNull(delegate.getTargetTaskInstanceId());
        assertNull(delegate.getTaskType());
        assertNull(delegate.getFlowInstanceId());
        assertNull(delegate.getStrategyConfig());
        assertNull(delegate.getFlowableAdaptor());
        assertNull(delegate.getEventPublisher());
        assertNull(delegate.getFlowInstanceRepository());
        assertNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertFalse(delegate.isDone());
        assertNull(delegate.getLatch());
        assertNull(delegate.getRetryExecutor());
        assertNull(delegate.getTaskInstanceCreatedlistener());
        assertNull(delegate.getActiveTaskStatisticsListener());
    }

    /**
     * Test initialization with EventPublisher
     */
    @Test
    public void testInitWithEventPublisher() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        BaseRuntimeFlowableDelegate<Object> delegate = new BaseRuntimeFlowableDelegate<>();
        delegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(ArgumentMatchers.instanceOf(ActiveTaskStatisticsListener.class));
        verify(eventPublisher).addEventListener(ArgumentMatchers.instanceOf(TaskInstanceCreatedListener.class));
    }

    /**
     * Test {@link BaseRuntimeFlowableDelegate#run(DelegateExecution)} with normal execution
     */
    @Test
    public void testRunNormalExecution() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
        BaseRuntimeFlowableDelegate<Object> delegate = spy(new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> {
                    return "result";
                };
            }
        });

        when(eventPublisher.publishEvent(ArgumentMatchers.instanceOf(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.eq(1L))).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        delegate.init(eventPublisher);
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.run(execution);

        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(ServiceTaskStartedEvent.class));
        verify(delegate).preHandle(execution);
        verify(delegate).initTargetTaskInstanceId(execution);
        verify(delegate).bindToFlowTaskInstance(ArgumentMatchers.any(FlowTaskInstance.class));
        verify(delegate).updateFlowInstanceStatus(ArgumentMatchers.eq(FlowStatus.EXECUTING));
        assertEquals("result", delegate.get());
    }

    /**
     * Test {@link BaseRuntimeFlowableDelegate#run(DelegateExecution)} with initialization exception
     */
    @Test
    public void testRunInitializationException() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        BaseRuntimeFlowableDelegate<Object> delegate = spy(new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                throw new RuntimeException("Initialization Exception");
            }
        });

        when(eventPublisher.publishEvent(ArgumentMatchers.instanceOf(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.eq(1L))).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        delegate.init(eventPublisher);
        DelegateExecution execution = mock(DelegateExecution.class);
        try {
            delegate.run(execution);
            fail("Expected ServiceTaskError to be thrown");
        } catch (ServiceTaskError e) {
            assertEquals("Initialization Exception", e.getCause().getMessage());
        }

        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(ServiceTaskStartedEvent.class));
        verify(delegate, Mockito.never()).preHandle(execution);
        verify(delegate, Mockito.never()).initTargetTaskInstanceId(execution);
        verify(delegate, Mockito.never()).bindToFlowTaskInstance(ArgumentMatchers.any(FlowTaskInstance.class));
        verify(delegate, Mockito.never()).updateFlowInstanceStatus(ArgumentMatchers.any(FlowStatus.class));
    }

    /**
     * Test {@link BaseRuntimeFlowableDelegate#run(DelegateExecution)} with service task execution exception
     */
    @Test
    public void testRunServiceTaskExecutionException() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);
        BaseRuntimeFlowableDelegate<Object> delegate = spy(new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) throws Exception {
                throw new RuntimeException("Service Task Execution Exception");
            }
        });

        when(eventPublisher.publishEvent(ArgumentMatchers.instanceOf(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(ArgumentMatchers.anyString())).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId(ArgumentMatchers.anyString(), ArgumentMatchers.eq(1L))).thenReturn(Optional.of(mock(FlowTaskInstance.class)));

        delegate.init(eventPublisher);
        DelegateExecution execution = mock(DelegateExecution.class);
        try {
            delegate.run(execution);
            fail("Expected ServiceTaskError to be thrown");
        } catch (ServiceTaskError e) {
            assertEquals("Service Task Execution Exception", e.getCause().getMessage());
        }

        verify(eventPublisher).publishEvent(ArgumentMatchers.instanceOf(ServiceTaskStartedEvent.class));
        verify(delegate, Mockito.never()).preHandle(execution);
        verify(delegate, Mockito.never()).initTargetTaskInstanceId(execution);
        verify(delegate, Mockito.never()).bindToFlowTaskInstance(ArgumentMatchers.any(FlowTaskInstance.class));
        verify(delegate, Mockito.never()).updateFlowInstanceStatus(ArgumentMatchers.any(FlowStatus.class));
    }

}