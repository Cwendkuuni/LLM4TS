package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 * @date 2023-03-15 11:16
 */
public class BaseRuntimeFlowableDelegateTest {

    private static final String TEST_FLOW_INSTANCE_ID = "testFlowInstanceId";
    private static final String TEST_PROCESS_DEFINITION_ID = "testProcessDefinitionId";
    private static final String TEST_ACTIVITY_ID = "testActivityId";
    private static final TaskType TEST_TASK_TYPE = TaskType.TEST_TASK_TYPE;
    private static final ExecutionStrategyConfig TEST_STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final Long TEST_TARGET_TASK_INSTANCE_ID = 1L;

    @org.junit.Before
    public void setup() {
        EventPublisher mockEventPublisher = mock(EventPublisher.class);
        FlowableAdaptor mockFlowableAdaptor = mock(FlowableAdaptor.class);
        FlowInstanceRepository mockFlowInstanceRepository = mock(FlowInstanceRepository.class);

        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return null;
                    }
                };
            }

            @Override
            public void preHandle(DelegateExecution execution) {}
        };

        testSubject.setFlowableAdaptor(mockFlowableAdaptor);
        testSubject.setEventPublisher(mockEventPublisher);
        testSubject.setFlowInstanceRepository(mockFlowInstanceRepository);
    }

    @org.junit.Test
    public void testInitializationWithProcessDefinitionId() {
        // Given
        when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID))
                .thenReturn(TEST_FLOW_INSTANCE_ID);

        // When
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>();
        testSubject.init(null);

        // Then
        assertEquals(TEST_FLOW_INSTANCE_ID, testSubject.getFlowInstanceId());
    }

    @org.junit.Test
    public void testInitializationWithFlowInstanceId() {
        // Given
        when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenCallRealMethod();
        when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(TEST_FLOW_INSTANCE_ID);
        when(mockFlowableAdaptor.getFlowInstanceById(TEST_FLOW_INSTANCE_ID)).thenReturn(new Execution());

        // When
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>() {
            @Override
            public void init(EventPublisher eventPublisher) {
                super.init(eventPublisher);
                this.flowInstanceId = TEST_FLOW_INSTANCE_ID;
            }
        };
        testSubject.init(null);

        // Then
        assertEquals(TEST_FLOW_INSTANCE_ID, testSubject.getFlowInstanceId());
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testInitializationWithNullProcessDefinitionId() {
        // Given
        when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenCallRealMethod();
        when(mockFlowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        // When
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>();
        testSubject.init(null);

        // Then
        fail("Expected AssertionError not thrown");
    }

    @org.junit.Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>();
        when(mockFlowableAdaptor.getFlowInstanceById(TEST_FLOW_INSTANCE_ID))
                .thenReturn(new Execution() {
                    @Override
                    public String getProcessDefinitionId() {
                        return TEST_PROCESS_DEFINITION_ID;
                    }

                    @Override
                    public String getActivityId() {
                        return TEST_ACTIVITY_ID;
                    }
                });

        // When
        testSubject.run(Mockito.mock(DelegateExecution.class));

        // Then
        assertEquals(TEST_FLOW_INSTANCE_ID, testSubject.getFlowInstanceId());
        assertEquals(TEST_ACTIVITY_ID, testSubject.getActivityId());
        assertEquals(TEST_TASK_TYPE, testSubject.getTaskType());
        assertEquals(TEST_STRATEGY_CONFIG, testSubject.getStrategyConfig());
        assertNull(testSubject.getReturnObject());
        assertNull(testSubject.getThrown());
        assertTrue(testSubject.isDone());
    }

    @org.junit.Test
    public void testRunWithException() throws Exception {
        // Given
        when(mockFlowableAdaptor.getFlowInstanceById(TEST_FLOW_INSTANCE_ID))
                .thenThrow(new RuntimeException("Test exception"));

        // When
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>();
        testSubject.run(Mockito.mock(DelegateExecution.class));

        // Then
        assertEquals(TEST_FLOW_INSTANCE_ID, testSubject.getFlowInstanceId());
        assertNotNull(testSubject.getThrown());
        assertTrue(testSubject.isDone());

        verify(mockFlowableAdaptor, times(1)).getFlowInstanceById(TEST_FLOW_INSTANCE_ID);
        verify(mockEventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verifyNoMoreInteractions(mockFlowableAdaptor);
        verifyNoMoreInteractions(mockEventPublisher);
    }

    @org.junit.Test
    public void testUpdateFlowInstanceStatus() {
        // Given
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>();

        // When
        testSubject.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Then
        verify(mockFlowInstanceRepository, times(1)).updateStatusById(TEST_FLOW_INSTANCE_ID, FlowStatus.COMPLETED);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testUpdateFlowInstanceStatusWithNullFlowInstanceId() {
        // Given
        BaseRuntimeFlowableDelegate<Object> testSubject = new BaseRuntimeFlowableDelegate<Object>();

        // When
        testSubject.updateFlowInstanceStatus(null);

        // Then
        fail("Expected IllegalArgumentException not thrown");
    }
}