package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.annotations.MockitoOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;;

@MockitoOptions(unitTests = {BaseRuntimeFlowableDelegateTest.class})
@SpringBootTest
@SpringJUnitConfig(classes = BaseRuntimeFlowableDelegate.class)
@ActiveProfiles("test")
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution execution;
    private EventPublisher eventPublisher;
    private FlowInstanceRepository flowInstanceRepository;
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    private CountDownLatch latch;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventPublisher = mock(EventPublisher.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
        latch = new CountDownLatch(1);

        eventPublisher.removeEventListener(activeTaskStatisticsListener);
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class)))
                .thenReturn(true);

        execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("activityId");
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            void initTargetTaskInstanceId(DelegateExecution execution) {
                this.flowInstanceId = 1L;
                this.targetTaskInstanceId = 1L;
                this.taskType = TaskType.TASK_TYPE_1;
                this.strategyConfig = new ExecutionStrategyConfig();
            }

            @Override
            Callable<String> initCallable(DelegateExecution execution) {
                return () -> "result";
            }
        };
        delegate.init(eventPublisher);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setEventPublisher(eventPublisher);
        delegate.setActiveTaskStatisticsListener(activeTaskStatisticsListener);
        delegate.setLatch(latch);
        delegate.setRetryExecutor(new RetryExecutor());
    }

    @Test
    public void testExecute() {
        delegate.execute(execution);
        verify(execution, never()).terminate();
        verify(activeTaskStatisticsListener, times(1)).taskExecutionFinished(any(FlowTaskInstance.class), eq("success"));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGet() throws Exception {
        delegate.run(execution);
        delegate.get();
        verify(execution).getVariable("returnObject");
        assertEquals("result", delegate.getReturnObject());
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        delegate.run(execution);
        TimeUnit.SECONDS.sleep(2);
        try {
            delegate.get(3, TimeUnit.SECONDS);
            fail("should throw TimeoutException");
        } catch (TimeoutException e) {
            // expected
        }
        verify(execution, never()).terminate();
        verify(activeTaskStatisticsListener, times(1)).taskExecutionFailed(any(FlowTaskInstance.class), eq("timeout"));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGetError() throws Exception {
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(false);

        delegate.run(execution);
        delegate.get();
        verify(execution).terminate();
        verify(activeTaskStatisticsListener, times(1)).taskExecutionFailed(any(FlowTaskInstance.class), eq("error"));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGetInterrupted() throws Exception {
        Thread.currentThread().interrupt();

        delegate.run(execution);
        try {
            delegate.get();
            fail("should throw InterruptedException");
        } catch (InterruptedException e) {
            // expected
        }
        verify(execution).terminate();
        verify(activeTaskStatisticsListener, times(1)).taskExecutionFailed(any(FlowTaskInstance.class), eq("interrupted"));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }

    @Test
    public void testGetDone() throws Exception {
        delegate.run(execution);
        verify(execution, never()).terminate();
        verify(activeTaskStatisticsListener, times(1)).taskExecutionFinished(any(FlowTaskInstance.class), eq("success"));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
        delegate.get();
        verifyNoInteractions(execution);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    public void testGetErrorDuringExecution() throws Exception {
        Callable<String> callable = () -> {
            throw new Exception("test exception");
        };
        delegate.initCallable(execution) = callable;
        delegate.run(execution);

        try {
            delegate.get();
            fail("should throw ExecutionException");
        } catch (ExecutionException e) {
            assertEquals("test exception", e.getCause().getMessage());
        }
        verify(execution).terminate();
        verify(activeTaskStatisticsListener, times(1)).taskExecutionFailed(any(FlowTaskInstance.class), eq("error"));
        verify(eventPublisher).publishEvent(any(ServiceTaskStartedEvent.class));
    }
}