package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest extends BaseRuntimeFlowableDelegateTest {

    @Autowired
    @Qualifier("mockFlowableAdaptor")
    private FlowableAdaptor flowableAdaptor;

    @Autowired
    @Qualifier("mockEventPublisher")
    private EventPublisher eventPublisher;

    @Autowired
    private FlowInstanceRepository flowInstanceRepository;

    @Captor
    private ArgumentCaptor<FlowStatus> flowStatusCaptor;

    private BaseRuntimeFlowableDelegate<String> delegate;
    private Execution mockExecution;
    private CountDownLatch latch;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockExecution = mock(Execution.class);
        delegate = spy(new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> "testResult";
            }
        });
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.eventPublisher = eventPublisher;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = RetryExecutor.builder().build();
        delegate.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        delegate.activeTaskStatisticsListener = new ActiveTaskStatisticsListener(delegate);
        delegate.latch = latch = new CountDownLatch(1);
    }

    @After
    public void tearDown() {
        latch = null;
    }

    @Test
    public void testInit() {
        assertTrue(delegate.getRetryExecutor() != null);
        assertTrue(delegate.getLatch() != null);
        assertTrue(delegate.getTaskInstanceCreatedlistener() != null);
        assertTrue(delegate.getActiveTaskStatisticsListener() != null);
    }

    @Test
    public void testInitCallable() throws Exception {
        assertEquals("testResult", delegate.call().get());
    }

    @Test
    public void testRun() throws Exception {
        delegate.init(eventPublisher);
        delegate.run(mockExecution);
        verify(eventPublisher).publishEvent(new ServiceTaskStartedEvent(delegate, mockExecution.getThread()));
    }

    @Test
    public void testExecute() throws Exception {
        delegate.execute(mockExecution);
        verify(eventPublisher).removeEventListener(delegate.getTaskInstanceCreatedlistener());
        verify(eventPublisher).removeEventListener(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testIsDone() throws Exception {
        assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        delegate.init(eventPublisher);
        delegate.run(mockExecution);
        assertEquals("testResult", delegate.get());
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        delegate.run(mockExecution);
        delegate.get(1, TimeUnit.SECONDS);
        assertEquals("testResult", delegate.get());
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.init(eventPublisher);
        delegate.run(mockExecution);
        delegate.get(0, TimeUnit.SECONDS);
        assertFalse(delegate.isDone());
        throw new AssertionError("Should throw TimeoutException");
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        delegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(delegate);
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "flowInstanceId", 1L, () -> true);
        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository).updateStatusById(1L, flowStatusCaptor.capture());
        assertTrue(flowStatusCaptor.getValue() == FlowStatus.COMPLETED);
    }
}