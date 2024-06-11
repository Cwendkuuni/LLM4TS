package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for BaseRuntimeFlowableDelegate
 *
 * @author yh263208
 * @date 2023-03-21 11:48
 */
@Slf4j
@SpringJUnitConfig
@ContextConfiguration(classes = BaseRuntimeFlowableDelegateTest.TestConfig.class)
public class BaseRuntimeFlowableDelegateTest {

    @Getter
    private static class TestConfig {
        @Bean
        public BaseRuntimeFlowableDelegate testee() {
            return new BaseRuntimeFlowableDelegateTest.TestBaseRuntimeFlowableDelegate();
        }

        @Bean
        public FlowableAdaptor flowableAdaptor() {
            return Mockito.mock(FlowableAdaptor.class);
        }

        @Bean
        public EventPublisher eventPublisher() {
            return Mockito.mock(EventPublisher.class);
        }

        @Bean
        public FlowInstanceRepository flowInstanceRepository() {
            return Mockito.mock(FlowInstanceRepository.class);
        }
    }

    @Getter
    private static class TestBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {
        @Autowired
        private FlowableAdaptor flowableAdaptor;
        @Autowired
        private EventPublisher eventPublisher;
        @Autowired
        private FlowInstanceRepository flowInstanceRepository;

        public TestBaseRuntimeFlowableDelegate() {
            super();
        }

        @PostConstruct
        public void init(EventPublisher eventPublisher) {
            super.init(eventPublisher);
        }

        @Override
        protected void preHandle(DelegateExecution execution) {
            // Empty implementation
        }

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "Test result";
        }
    }

    @Test
    public void testInit() {
        BaseRuntimeFlowableDelegate testee = Mockito.spy(TestConfig.testee());
        assertNull(testee.getActivityId());
        assertNull(testee.getTargetTaskInstanceId());
        assertEquals(TaskType.UNDEFINED, testee.getTaskType());
        assertNull(testee.getFlowInstanceId());
        assertNull(testee.getStrategyConfig());
        assertNull(testee.getFlowableAdaptor());
        assertNull(testee.getEventPublisher());
        assertNull(testee.getFlowInstanceRepository());
        assertNull(testee.getReturnObject());
        assertNull(testee.getThrown());
        assertFalse(testee.isDone());
        assertEquals(1, testee.getLatch().getCount());
        assertNull(testee.getRetryExecutor());
        assertNull(testee.getTaskInstanceCreatedlistener());
        assertNull(testee.getActiveTaskStatisticsListener());

        assertEquals("TestBaseRuntimeFlowableDelegate", testee.getClass().getName());
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate testee = Mockito.spy(TestConfig.testee());

        // Test empty execution
        testee.run(null);
        verify(testee.getEventPublisher(), times(0)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        assertNull(testee.getActivityId());
        assertNull(testee.getTargetTaskInstanceId());
        assertNull(testee.getFlowInstanceId());
        assertNull(testee.getStrategyConfig());
        assertNull(testee.getReturnObject());
        assertNull(testee.getThrown());
        assertTrue(testee.isDone());
        assertEquals(0, testee.getLatch().getCount());

        // Test null execution
        DelegateExecution execution = null;
        try {
            testee.run(execution);
            fail();
        } catch (Exception e) {
            // Expected
        }
        verify(testee.getEventPublisher(), times(0)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        assertNull(testee.getActivityId());
        assertNull(testee.getTargetTaskInstanceId());
        assertNull(testee.getFlowInstanceId());
        assertNull(testee.getStrategyConfig());
        assertNull(testee.getReturnObject());
        assertNull(testee.getThrown());
        assertTrue(testee.isDone());
        assertEquals(0, testee.getLatch().getCount());

        // Test initialization with a valid execution
        execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("testActivity");
        when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");

        testee.run(execution);
        verify(testee.getEventPublisher(), times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        assertEquals("testActivity", testee.getActivityId());
        assertNotNull(testee.getTargetTaskInstanceId());
        assertEquals(TaskType.UNDEFINED, testee.getTaskType());
        assertNull(testee.getFlowInstanceId());
        assertNull(testee.getStrategyConfig());
        assertEquals("Test result", testee.getReturnObject());
        assertNull(testee.getThrown());
        assertTrue(testee.isDone());
        assertEquals(0, testee.getLatch().getCount());

        // Test initialization with a error
        when(testee.getFlowableAdaptor().getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId"))
                .thenThrow(new RuntimeException("Error"));
        try {
            testee.run(execution);
            fail();
        } catch (ServiceTaskError e) {
            // Expected
        }
        verify(testee.getEventPublisher(), times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        assertEquals("testActivity", testee.getActivityId());
        assertNotNull(testee.getTargetTaskInstanceId());
        assertEquals(TaskType.UNDEFINED, testee.getTaskType());
        assertNull(testee.getFlowInstanceId());
        assertNull(testee.getStrategyConfig());
        assertNull(testee.getReturnObject());
        assertNotNull(testee.getThrown());
        assertTrue(testee.isDone());
        assertEquals(0, testee.getLatch().getCount());

        // Test initialization with a timeout
        when(testee.getFlowableAdaptor().getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId"))
                .thenCall(() -> {
                    throw new RuntimeException("Error");
                });
        try {
            testee.get(5, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            // Expected
        }
        verify(testee.getEventPublisher(), times(1)).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        assertEquals("testActivity", testee.getActivityId());
        assertNotNull(testee.getTargetTaskInstanceId());
        assertEquals(TaskType.UNDEFINED, testee.getTaskType());
        assertNull(testee.getFlowInstanceId());
        assertNull(testee.getStrategyConfig());
        assertNull(testee.getReturnObject());
        assertNull(testee.getThrown());
        assertTrue(testee.isDone());
        assertEquals(0, testee.getLatch().getCount());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate testee = Mockito.spy(TestConfig.testee());
        FlowTaskInstance flowTaskInstance = Mockito.mock(FlowTaskInstance.class);

        testee.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(testee);
        assertEquals("Monitor task instance creation and bind tasks, taskInstanceId=" + flowTaskInstance.getId() + ", activityId="
                + testee.getActivityId(), testee.getLog().getMessage());

        // Test binding to a different instance
        FlowTaskInstance differentFlowTaskInstance = Mockito.mock(FlowTaskInstance.class);
        testee.bindToFlowTaskInstance(differentFlowTaskInstance);
        verify(differentFlowTaskInstance, times(0)).bindServiceTask(testee);
        verify(flowTaskInstance, times(1)).bindServiceTask(testee);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate testee = Mockito.spy(TestConfig.testee());
        when(testee.getFlowInstanceRepository().updateStatusById(testee.getFlowInstanceId(), FlowStatus.COMPLETED))
                .thenReturn(true);

        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(testee.getFlowInstanceRepository(), times(1)).updateStatusById(testee.getFlowInstanceId(), FlowStatus.COMPLETED);
    }
}