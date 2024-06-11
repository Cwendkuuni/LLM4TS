package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.junit.jUpgrade.TestedObject;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptorTest;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import lombok.Getter;
import lombok.MockitoAnnotations;
import lombok.extern.slf4j.Slf4j;

/**
 * Test class for BaseRuntimeFlowableDelegate
 */
@Slf4j
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private TestedObject<BaseRuntimeFlowableDelegate<String>> testedObject;

    @Mock
    private Callable<String> callable;

    @Mock
    private ServiceTaskStartedEvent serviceTaskStartedEvent;

    @Getter
    private static final String ACTIVITY_ID = "activityId";
    @Getter
    private static final long TARGET_TASK_INSTANCE_ID = 1L;
    @Getter
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    @Getter
    private static final long FLOW_INSTANCE_ID = 1L;
    @Getter
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();

    @PostConstruct
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(FLOW_INSTANCE_ID);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), eq(FLOW_INSTANCE_ID)))
                .thenReturn(FlowTaskInstance.builder().withId(TARGET_TASK_INSTANCE_ID).withTaskType(TASK_TYPE)
                        .withStrategyConfig(STRATEGY_CONFIG).build());
        when(flowableAdaptor.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);
    }

    @Test
    public void testInitCallable() {
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
        Callable<String> callableSpy = spy(callable);
        when(callableSpy.call()).thenReturn("result");
        when(retryExecutor.build()).thenReturn(retryExecutor);
        when(retryExecutor.run(any(), any())).thenReturn(true);

        testedObject.init(eventPublisher);
        Callable<String> callableReturned = testedObject.initCallable(execution);
        verify(eventPublisher).publishEvent(serviceTaskStartedEvent);
        assertEquals(callableSpy, callableReturned);
    }

    @Test
    public void testRun() throws Exception {
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
        initTargetTaskInstanceId(execution);

        when(callable.call()).thenCallRealMethod();

        testedObject.run(execution);

        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEvent);
        verify(callable, times(1)).call();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
        initTargetTaskInstanceId(execution);

        String result = "result";
        when(callable.call()).thenReturn(result);

        testedObject.get();

        verify(callable, times(1)).call();
        assertEquals(result, testedObject.get());
    }

    @Test
    public void testGetTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        when(execution.getCurrentActivityId()).thenReturn(ACTIVITY_ID);
        initTargetTaskInstanceId(execution);

        when(callable.call()).thenThrow(new RuntimeException());

        try {
            testedObject.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }

        verify(callable, times(1)).call();
        verifyNoMoreInteractions(testedObject);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, any())).thenReturn(true);
        testedObject.updateFlowInstanceStatus(FlowStatus.FLOW_STATUS_COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, eq(FlowStatus.FLOW_STATUS_COMPLETED));
    }

    private void initTargetTaskInstanceId(DelegateExecution execution) {
        testedObject.initTargetTaskInstanceId(execution);
    }
}