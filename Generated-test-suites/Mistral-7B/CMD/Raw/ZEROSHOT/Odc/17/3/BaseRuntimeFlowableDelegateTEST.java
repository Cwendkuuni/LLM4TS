package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.event.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SpringJUnitConfig(classes = BaseRuntimeFlowableDelegateTest.TestConfig.class)
public class BaseRuntimeFlowableDelegateTest {

    @org.junit.Before
    public void setup() {
        // Initialize mocks
        eventPublisher = mock(EventPublisher.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        retryExecutor = mock(RetryExecutor.class);
        BaseRuntimeFlowableDelegate<Integer> delegate = new TestBaseRuntimeFlowableDelegate();
        delegate.setEventPublisher(eventPublisher);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
        delegate.setRetryExecutor(retryExecutor);
    }

    @org.junit.Test
    public void testInit() {
        // Test initialization
        assertEquals("TestBaseRuntimeFlowableDelegate", delegate.getClass().getSimpleName());
        assertNull(delegate.getActivityId());
        assertNull(delegate.getTargetTaskInstanceId());
        assertNull(delegate.getTaskType());
        assertNull(delegate.getFlowInstanceId());
        assertNull(delegate.getStrategyConfig());
        assertNull(delegate.getReturnObject());
        assertNull(delegate.getThrown());
        assertFalse(delegate.isDone());
        assertEquals(1, delegate.latch.getCount());
        assertNotNull(delegate.retryExecutor);
        assertNotNull(delegate.taskInstanceCreatedlistener);
        assertNotNull(delegate.activeTaskStatisticsListener);
    }

    @org.junit.Test
    public void testExecute() throws Exception {
        // Test execute method
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = () -> 42;
        when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.execute(execution);

        // Assert event publisher was called
        ArgumentCaptor<ServiceTaskStartedEvent> eventArgumentCaptor = ArgumentCaptor.forClass(ServiceTaskStartedEvent.class);
        verify(eventPublisher).publishEvent(eventArgumentCaptor.capture());
        ServiceTaskStartedEvent event = eventArgumentCaptor.getValue();
        assertEquals(delegate, event.getDelegate());

        // Assert callable was executed
        Integer result = callable.call();
        assertEquals(42, result);

        // Assert delegate state was updated
        assertTrue(delegate.isDone());
        assertNull(delegate.getThrown());
        assertEquals(42, delegate.getReturnObject());

        // Assert latch was signaled
        assertEquals(0, delegate.latch.getCount());
    }

    @org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        // Test get method
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = () -> 42;
        when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.run(execution);

        // Assert delegate state was updated
        assertTrue(delegate.isDone());
        assertNull(delegate.getThrown());
        assertEquals(42, delegate.getReturnObject());

        // Test get method
        Integer result = delegate.get();
        assertEquals(42, result);

        // Test get method with timeout
        assertNull(delegate.get(10, TimeUnit.SECONDS));
    }

    @org.junit.Test(expected = ExecutionException.class)
    public void testGetWithException() throws InterruptedException, ExecutionException, TimeoutException {
        // Test get method with exception
        DelegateExecution execution = mock(DelegateExecution.class);

        Callable<Integer> callable = () -> {
            throw new RuntimeException("Test Exception");
        };
        when(delegate.initCallable(execution)).thenReturn(callable);

        delegate.run(execution);

        try {
            delegate.get();
            fail("Expected ExecutionException");
        } catch (ExecutionException e) {
            assertEquals("Test Exception", e.getCause().getMessage());
        }
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        // Test updateFlowInstanceStatus method
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository).updateStatusById(delegate.getFlowInstanceId(), eq(FlowStatus.COMPLETED));
    }

    @Configuration
    static class TestConfig {

        @Bean
        public EventPublisher eventPublisher() {
            return mock(EventPublisher.class);
        }

        @Bean
        public FlowableAdaptor flowableAdaptor() {
            return mock(FlowableAdaptor.class);
        }

        @Bean
        public FlowInstanceRepository flowInstanceRepository() {
            return mock(FlowInstanceRepository.class);
        }

        @Bean
        public RetryExecutor retryExecutor() {
            return mock(RetryExecutor.class);
        }

        @Bean
        public BaseRuntimeFlowableDelegate<Integer> testBaseRuntimeFlowableDelegate() {
            return new TestBaseRuntimeFlowableDelegate();
        }

    }

    static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<Integer> {

        @Override
        protected void preHandle(DelegateExecution execution) {
            // Empty implementation
        }

        @Override
        protected Callable<Integer> initCallable(DelegateExecution execution) {
            return () -> 42;
        }

    }

}