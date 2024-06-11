package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableTestHelper;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 * @date 2023-03-18 15:55
 * @since ODC_release_3.3.0
 */
public class BaseRuntimeFlowableDelegateTest extends BaseFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    @Mock
    private ExecutionStrategyConfig strategyConfig;
    @Mock
    private Callable<String> callable;
    @Mock
    private DelegateExecution execution;
    @Mock
    private ServiceTaskStartedEvent serviceTaskStartedEvent;
    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    private BaseRuntimeFlowableDelegate<String> testee;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testee = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callable;
            }
        };
        testee.setFlowableAdaptor(flowableAdaptor);
        testee.setEventPublisher(eventPublisher);
        testee.setFlowInstanceRepository(flowInstanceRepository);
        testee.setRetryExecutor(retryExecutor);
        testee.setTaskInstanceCreatedlistener(taskInstanceCreatedlistener);
        testee.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
    }

    @Test
    public void testRun_success() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(callable.call()).thenReturn("TestData");
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        testee.run(execution);

        verify(flowableAdaptor).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(callable).call();
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent actualServiceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertSame(testee, actualServiceTaskStartedEvent.getServiceTaskDelegate());
        assertEquals("TestData", testee.get());
        verify(flowInstanceRepository).updateStatusById(1L, FlowStatus.COMPLETED);
    }

    @Test
    public void testRun_failure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException());
        when(callable.call()).thenThrow(new RuntimeException());

        try {
            testee.run(execution);
            fail();
        } catch (Exception e) {
            assertFalse(testee.isDone());
            assertEquals("java.lang.RuntimeException", e.getCause().getClass().getName());
            verify(flowInstanceRepository, never()).updateStatusById(anyLong(), any(FlowStatus.class));
        }
    }

    @Test
    public void testExecute() {
        testee.execute(execution);
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testIsDone() {
        testee.done = true;
        assertTrue(testee.isDone());
    }

    @Test
    public void testGet_success() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(callable.call()).thenReturn("TestData");
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        testee.run(execution);
        assertTrue(testee.isDone());
        assertEquals("TestData", testee.get());
    }

    @Test(expected = InterruptedException.class)
    public void testGet_interrupted() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(callable.call()).thenReturn("TestData");
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        CountDownLatch latch = new CountDownLatch(1);
        testee.latch = latch;
        testee.run(execution);
        Thread.currentThread().interrupt();
        testee.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_failure() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenThrow(new RuntimeException());
        when(callable.call()).thenThrow(new RuntimeException());
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        CountDownLatch latch = new CountDownLatch(1);
        testee.latch = latch;
        testee.run(execution);
        testee.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGet_timeout() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(callable.call()).thenReturn("TestData");
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        CountDownLatch latch = new CountDownLatch(1);
        testee.latch = latch;
        testee.run(execution);
        latch.await(1, TimeUnit.MILLISECONDS);
        testee.get(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testInit_success() {
        testee.init(eventPublisher);
        verify(eventPublisher).addEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).addEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testBindToFlowTaskInstance_success() {
        FlowTaskInstance taskInstance = FlowableTestHelper.createFlowTaskInstance(1L, TaskType.ACTIVITY_TASK, strategyConfig);
        testee.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance).bindServiceTask(testee);
    }

    @Test
    public void testUpdateFlowInstanceStatus_success() throws Exception {
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(retryExecutor).run(() -> flowInstanceRepository.updateStatusById(1L, FlowStatus.COMPLETED), anyBoolean());
    }
}