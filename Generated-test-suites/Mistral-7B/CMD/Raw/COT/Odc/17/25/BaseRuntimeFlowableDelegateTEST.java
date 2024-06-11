package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.BaseFlowableDelegateTestUtil;
import com.oceanbase.odc.service.flow.test.BaseRuntimeFlowableDelegateTestUtil;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> testObject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BaseFlowableDelegateTestUtil.mockRuntimeExecution(testObject.getRuntimeExecutionContext());
        BaseRuntimeFlowableDelegateTestUtil.mockExecution(testObject.getExecution(), Execution.class);
        BaseRuntimeFlowableDelegateTestUtil.mockExecution(testObject.getDelegateExecution(), DelegateExecution.class);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
    }

    @Test
    public void testRun() {
        BaseRuntimeFlowableDelegateTestUtil.mockCallable(testObject);

        BaseRuntimeFlowableDelegateTestUtil.testRun(testObject, eventPublisher);

        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject).initCallable(eq(testObject.getDelegateExecution()));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject.getCallable()).call();
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithException() {
        BaseRuntimeFlowableDelegateTestUtil.mockCallableWithException(testObject);

        BaseRuntimeFlowableDelegateTestUtil.testRun(testObject, eventPublisher);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject.getCallable()).call();

        assertEquals(testObject.getThrown().getClass(), ServiceTaskError.class);
        assertEquals(testObject.getThrown().getMessage(), "Initialization task failed");
    }

    @Test
    public void testGet() throws Exception {
        BaseRuntimeFlowableDelegateTestUtil.mockCallable(testObject);

        BaseRuntimeFlowableDelegateTestUtil.testGet(testObject, eventPublisher, null);

        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject).initCallable(eq(testObject.getDelegateExecution()));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject.getCallable()).call();

        assertNotNull(testObject.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGetInterrupted() throws Exception {
        BaseRuntimeFlowableDelegateTestUtil.mockCallable(testObject);

        CountDownLatch latch = new CountDownLatch(1);
        BaseRuntimeFlowableDelegateTestUtil.testGet(testObject, eventPublisher, latch);

        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject).initCallable(eq(testObject.getDelegateExecution()));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject.getCallable()).call();

        latch.await();
        fail("Expected InterruptedException");
    }

    @Test(expected = ExecutionException.class)
    public void testGetExecutionException() throws Exception {
        BaseRuntimeFlowableDelegateTestUtil.mockCallableWithException(testObject);

        BaseRuntimeFlowableDelegateTestUtil.testGet(testObject, eventPublisher, null);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject.getCallable()).call();

        assertEquals(testObject.getThrown().getClass(), ExecutionException.class);
        assertEquals(testObject.getThrown().getCause().getMessage(), "Initialization task failed");
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeoutException() throws Exception {
        BaseRuntimeFlowableDelegateTestUtil.mockCallable(testObject);

        CountDownLatch latch = new CountDownLatch(1);

        BaseRuntimeFlowableDelegateTestUtil.testGet(testObject, eventPublisher, latch);

        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(testObject).preHandle(eq(testObject.getDelegateExecution()));
        verify(testObject.getCallable()).call();

        latch.await();
        fail("Expected TimeoutException");
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegateTestUtil.mockFlowTaskInstance(testObject, 1L);

        testObject.bindToFlowTaskInstance(BaseRuntimeFlowableDelegateTestUtil.getFlowTaskInstance(1L));

        assertEquals(testObject.getTargetTaskInstanceId(), 1L);
        verify(BaseRuntimeFlowableDelegateTestUtil.getFlowTaskInstance(1L)).bindServiceTask(testObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBindToFlowTaskInstanceInvalid() {
        BaseRuntimeFlowableDelegateTestUtil.mockFlowTaskInstance(testObject, 1L);

        testObject.bindToFlowTaskInstance(BaseRuntimeFlowableDelegateTestUtil.getFlowTaskInstance(2L));

        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegateTestUtil.mockFlowInstanceRepository(flowInstanceRepository, 1L, FlowStatus.RUNNING);

        testObject.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
    }

    @Test
    public void testPreHandle() {
        DelegateExecution delegateExecution = mock(DelegateExecution.class);
        testObject.preHandle(delegateExecution);

        verifyNoMoreInteractions(delegateExecution);
    }

}