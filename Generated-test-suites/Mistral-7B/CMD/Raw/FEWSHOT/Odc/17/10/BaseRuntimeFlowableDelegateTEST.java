package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;

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
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;
import com.oceanbase.odc.service.flow.test.BaseTest;
import lombok.SneakyThrows;

/**
 * Test class for {@link BaseRuntimeFlowableDelegate}
 *
 * @author yh263208
 * @date 2023-03-28 10:43
 */
public class BaseRuntimeFlowableDelegateTest extends BaseTest {

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    private static final String ACTIVITY_ID = "activityId";
    private static final Long FLOW_INSTANCE_ID = 1L;
    private static final Long TARGET_TASK_INSTANCE_ID = 1L;
    private static final TaskType TASK_TYPE = TaskType.TASK_TYPE_1;
    private static final ExecutionStrategyConfig STRATEGY_CONFIG = new ExecutionStrategyConfig();
    private static final FlowTaskInstance FLOW_TASK_INSTANCE = Mockito.mock(FlowTaskInstance.class);

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                throw new UnsupportedOperationException();
            }
        };
        delegate.init(eventPublisher);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID))
                .thenReturn(FLOW_INSTANCE_ID);
        when(flowableAdaptor.getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID))
                .thenReturn(Optional.of(FLOW_TASK_INSTANCE));
        when(flowInstanceRepository.updateStatusById(FLOW_INSTANCE_ID, any(FlowStatus.class)))
                .thenReturn(1);
        delegate.flowableAdaptor = flowableAdaptor;
        delegate.flowInstanceRepository = flowInstanceRepository;
        delegate.retryExecutor = retryExecutor;
        delegate.eventPublisher = eventPublisher;
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(retryExecutor.run(any(), any()))
                .thenReturn(Optional.of(FLOW_INSTANCE_ID));

        PreConditions.validExists(ResourceType.ODC_FLOW_INSTANCE, "processDefinitionId", PROCESS_DEFINITION_ID,
                () -> retryExecutor.run(
                        () -> flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID),
                        Optional::isPresent).isPresent());

        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(PROCESS_DEFINITION_ID);
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(ACTIVITY_ID, FLOW_INSTANCE_ID);
        verify(flowInstanceRepository, times(1)).updateStatusById(FLOW_INSTANCE_ID, any(FlowStatus.class));
    }

    @Test
    public void testBindToFlowTaskInstance() {
        when(FLOW_TASK_INSTANCE.getId()).thenReturn(TARGET_TASK_INSTANCE_ID);
        when(FLOW_TASK_INSTANCE.getTaskType()).thenReturn(TASK_TYPE);
        when(FLOW_TASK_INSTANCE.getStrategyConfig()).thenReturn(STRATEGY_CONFIG);

        delegate.bindToFlowTaskInstance(FLOW_TASK_INSTANCE);

        verify(FLOW_TASK_INSTANCE, times(1)).bindServiceTask(delegate);
    }

    @Test
    public void testRun() throws Exception {
        String activityId = ACTIVITY_ID;
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn(activityId);

        Callable<String> callable = mock(Callable.class);
        delegate.activityId = activityId;
        delegate.initTargetTaskInstanceId(execution);
        delegate.callable = callable;

        doAnswer(invocationOnMock -> {
            delegate.preHandle(execution);
            return delegate.callable.call();
        }).when(delegate).run(execution);

        delegate.run(execution);

        verify(eventPublisher, times(1)).publishEvent(any(ServiceTaskStartedEvent.class));
        verify(delegate, times(1)).preHandle(execution);
        verify(callable, times(1)).call();
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = mock(Callable.class);

        delegate.activityId = ACTIVITY_ID;
        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));
        delegate.callable = callable;
        delegate.latch = new CountDownLatch(1);

        delegate.run(Mockito.mock(DelegateExecution.class));

        delegate.get();

        verify(delegate, times(1)).isDone();
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> callable = mock(Callable.class);

        delegate.activityId = ACTIVITY_ID;
        delegate.initTargetTaskInstanceId(Mockito.mock(DelegateExecution.class));
        delegate.callable = callable;
        delegate.latch = new CountDownLatch(1);

        when(callable.call()).thenThrow(new RuntimeException());

        try {
            delegate.get(1, TimeUnit.SECONDS);
            fail();
        } catch (TimeoutException e) {
            // expected
        }
    }

    @Test
    public void testIsDone() throws Exception {
        when(delegate.done).thenReturn(true);

        assertTrue(delegate.isDone());
    }

    // Add more test cases for other methods if needed

}