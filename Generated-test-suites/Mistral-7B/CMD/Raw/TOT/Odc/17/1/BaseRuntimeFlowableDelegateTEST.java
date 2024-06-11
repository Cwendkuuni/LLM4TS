package com.oceanbase.odc.service.flow.test.task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.OngoingStubbing;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<String> testee;

    @Spy
    private EventPublisher eventPublisher = mock(EventPublisher.class);
    @Spy
    private FlowInstanceRepository flowInstanceRepository = mock(FlowInstanceRepository.class);
    @Spy
    private FlowableAdaptor flowableAdaptor = mock(FlowableAdaptor.class);

    private CountDownLatch latch;
    private Callable<String> callable;
    private RetryExecutor retryExecutor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        retryExecutor = RetryExecutor.builder().retryIntervalMillis(1).retryTimes(1).build();
        testee.init(eventPublisher);
        latch = new CountDownLatch(1);
        callable = mock(Callable.class);
        testee.retryExecutor = retryExecutor;
        testee.latch = latch;
    }

    @Test
    public void testInit() {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class)))
                .thenReturn(null);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        testee.init(eventPublisher);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId("processDefinitionId");
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testPreHandle() {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        testee.preHandle(mock(DelegateExecution.class));
        verifyNoMoreInteractions(callable);
    }

    @Test
    public void testRunWithSuccess() throws Exception {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(callable.call()).thenReturn("success");

        testee.activityId = "activityId";
        testee.targetTaskInstanceId = 1L;
        testee.flowInstanceId = 1L;
        testee.strategyConfig = new ExecutionStrategyConfig();
        testee.flowableAdaptor = flowableAdaptor;
        testee.eventPublisher = eventPublisher;
        testee.flowInstanceRepository = flowInstanceRepository;
        testee.latch = latch;
        testee.retryExecutor = retryExecutor;

        testee.run(mock(DelegateExecution.class));

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
        assertFalse(latch.await(3, TimeUnit.SECONDS));
        assertEquals("success", testee.returnObject);
        assertNull(testee.thrown);
    }

    @Test
    public void testRunWithException() throws Exception {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("Test exception"));

        testee.activityId = "activityId";
        testee.targetTaskInstanceId = 1L;
        testee.flowInstanceId = 1L;
        testee.strategyConfig = new ExecutionStrategyConfig();
        testee.flowableAdaptor = flowableAdaptor;
        testee.eventPublisher = eventPublisher;
        testee.flowInstanceRepository = flowInstanceRepository;
        testee.latch = latch;
        testee.retryExecutor = retryExecutor;

        try {
            testee.run(mock(DelegateExecution.class));
            fail();
        } catch (ServiceTaskError e) {
            verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
            verifyZeroInteractions(callable);
            assertFalse(latch.await(3, TimeUnit.SECONDS));
            assertSame(e.getCause(), new RuntimeException("Test exception"));
            assertEquals(FlowStatus.FAILED, testee.getStatus());
            assertEquals("Test exception", testee.getErrorMessage());
        }
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(callable.call()).thenReturn("success");

        testee.activityId = "activityId";
        testee.targetTaskInstanceId = 1L;
        testee.flowInstanceId = 1L;
        testee.strategyConfig = new ExecutionStrategyConfig();
        testee.flowableAdaptor = flowableAdaptor;
        testee.eventPublisher = eventPublisher;
        testee.flowInstanceRepository = flowInstanceRepository;
        testee.latch = latch;
        testee.retryExecutor = retryExecutor;

        testee.get();

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(callable, times(1)).call();
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.COMPLETED));
        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertEquals("success", testee.returnObject);
        assertNull(testee.thrown);
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(callable.call()).thenThrow(new RuntimeException("Test exception"));

        testee.activityId = "activityId";
        testee.targetTaskInstanceId = 1L;
        testee.flowInstanceId = 1L;
        testee.strategyConfig = new ExecutionStrategyConfig();
        testee.flowableAdaptor = flowableAdaptor;
        testee.eventPublisher = eventPublisher;
        testee.flowInstanceRepository = flowInstanceRepository;
        testee.latch = latch;
        testee.retryExecutor = retryExecutor;

        try {
            testee.get(3, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
            verifyZeroInteractions(callable);
            assertFalse(latch.await(3, TimeUnit.SECONDS));
            assertNull(testee.returnObject);
            assertNull(testee.thrown);
        }
    }

    @Test
    public void testBindToFlowTaskInstance() throws Exception {
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(1L);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(callable.call()).thenReturn("success");

        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(taskInstance.getId()).thenReturn(1L);

        testee.activityId = "activityId";
        testee.targetTaskInstanceId = 1L;
        testee.flowInstanceId = 1L;
        testee.strategyConfig = new ExecutionStrategyConfig();
        testee.flowableAdaptor = flowableAdaptor;
        testee.eventPublisher = eventPublisher;
        testee.flowInstanceRepository = flowInstanceRepository;
        testee.latch = latch;
        testee.retryExecutor = retryExecutor;

        testee.bindToFlowTaskInstance(taskInstance);

        verify(taskInstance, times(1)).bindServiceTask(testee);
        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyZeroInteractions(callable);
        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.STARTED));
        assertEquals(FlowStatus.STARTED, testee.getStatus());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        testee.updateFlowInstanceStatus(FlowStatus.STARTED);

        verify(flowInstanceRepository, times(1)).updateStatusById(anyLong(), eq(FlowStatus.STARTED));
    }

    @Test
    public void testUpdateFlowInstanceStatusWithException() throws Exception {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenThrow(new RuntimeException("Test exception"));

        testee.updateFlowInstanceStatus(FlowStatus.STARTED);

        verifyZeroInteractions(flowInstanceRepository);
    }
}