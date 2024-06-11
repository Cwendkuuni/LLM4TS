package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Unit test for {@link BaseRuntimeFlowableDelegate}.
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private DelegateExecution execution;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    private BaseRuntimeFlowableDelegate<String> delegate;

    /**
     * Initialize mocks and test objects.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("someProcessDefinitionId"))
                .thenReturn(Optional.of(1L));
        when(flowableAdaptor.getFlowInstanceById(1L)).thenReturn(execution);
        when(execution.getProcessDefinitionId()).thenReturn("someProcessDefinitionId");
        when(execution.getCurrentActivityId()).thenReturn("someActivityId");
        when(eventPublisher.publishEvent(serviceTaskStartedEventCaptor.capture())).thenReturn(null);

        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected void init(EventPublisher eventPublisher) {
                super.init(eventPublisher);
                this.flowableAdaptor = flowableAdaptor;
                this.eventPublisher = eventPublisher;
                this.flowInstanceRepository = flowInstanceRepository;
                this.retryExecutor = retryExecutor;
            }

            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "Test Result";
                    }
                };
            }

            @Override
            protected void preHandle(DelegateExecution execution) throws Exception {
                // No-op
            }

            @Override
            public void updateFlowInstanceStatus(FlowStatus flowStatus) {
                // No-op
            }
        };

        delegate.init(eventPublisher);
    }

    /**
     * Test initialization of the delegate.
     */
    @Test
    public void testInitialization() {
        assertNotNull(delegate.getActivityId());
        assertNotNull(delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getFlowInstanceId());
        assertNotNull(delegate.getStrategyConfig());
        assertNull(delegate.getReturnObject());
        assertFalse(delegate.isDone());
    }

    /**
     * Test running the delegate.
     */
    @Test
    public void testRun() throws Exception {
        delegate.run(execution);

        assertTrue(delegate.isDone());
        assertEquals("Test Result", delegate.getReturnObject());
        assertEquals("Test Result", serviceTaskStartedEventCaptor.getValue().getResult());
    }

    /**
     * Test exception handling during initialization.
     */
    @Test(expected = ServiceTaskError.class)
    public void testInitializationException() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("someProcessDefinitionId"))
                .thenThrow(new Exception("Exception during getting flow instance id"));

        delegate.run(execution);
    }

    /**
     * Test the get() method for a completed delegate.
     */
    @Test
    public void testGetCompleted() throws Exception {
        delegate.run(execution);

        String result = delegate.get();
        assertEquals("Test Result", result);
    }

    /**
     * Test the get() method for a non-completed delegate.
     */
    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws Exception {
        delegate.run(execution);
        Thread.currentThread().interrupt();

        delegate.get();
    }

    /**
     * Test the get() method with a timeout.
     */
    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws Exception {
        delegate.run(execution);

        Thread.sleep(100);

        delegate.get(50, TimeUnit.MILLISECONDS);
    }

    /**
     * Test the get() method for a failed delegate.
     */
    @Test(expected = ExecutionException.class)
    public void testGetFailed() throws Exception {
        when(execution.getProcessDefinitionId()).thenThrow(new Exception("Exception during getting process definition id"));

        delegate.run(execution);

        delegate.get();
    }
}