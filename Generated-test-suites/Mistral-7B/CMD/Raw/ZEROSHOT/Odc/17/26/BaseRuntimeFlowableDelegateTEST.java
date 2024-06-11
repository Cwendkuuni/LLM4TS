package com.oceanbase.odc.service.flow.task;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitTest;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.Verify;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegateTest;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

@SpringJUnitTest
@ContextConfiguration(classes = BaseRuntimeFlowableDelegate.class)
public class BaseRuntimeFlowableDelegateTest {

    @MockBean
    private FlowableAdaptor flowableAdaptor;

    @MockBean
    private EventPublisher eventPublisher;

    @MockBean
    private FlowInstanceRepository flowInstanceRepository;

    @Autowired
    private BaseRuntimeFlowableDelegate<String> testee;

    @Value("${test.activityId}")
    private String testActivityId;

    @Value("${test.flowInstanceId}")
    private Long testFlowInstanceId;

    @Value("${test.targetTaskInstanceId}")
    private Long testTargetTaskInstanceId;

    @Value("${test.taskType}")
    private TaskType testTaskType;

    @Value("${test.strategyConfig}")
    private ExecutionStrategyConfig testStrategyConfig;

    @MockBean
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @MockBean
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @MockBean
    private RetryExecutor retryExecutor;

    @BeforeEach
    public void setup() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("testProcessDefinitionId"))
                .thenReturn(testFlowInstanceId);
        when(flowableAdaptor.getFlowInstanceRepository()).thenReturn(flowInstanceRepository);
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivityId", testFlowInstanceId))
                .thenReturn(Optional.of(new FlowTaskInstance()));
        when(flowInstanceRepository.updateStatusById(testFlowInstanceId, any(FlowStatus.class)))
                .thenReturn(1L);
        testee = new BaseRuntimeFlowableDelegate<>("testActivityId", testTargetTaskInstanceId, testTaskType, testFlowInstanceId,
                testStrategyConfig, flowableAdaptor, eventPublisher, flowInstanceRepository) {
            {
                this.retryExecutor = Mockito.spy(retryExecutor);
                this.taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
                this.activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
            }
        };
        testee.init(eventPublisher);
    }

    @Test
    public void testRun() throws Exception {
        // Given
        Callable<String> callable = mock(Callable.class);
        when(testee.initCallable(Mockito.any(DelegateExecution.class))).thenReturn(callable);

        // When
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);
        testee.run(execution);

        // Then
        verify(eventPublisher).publishEvent(Mockito.isInstanceOf(ServiceTaskStartedEvent.class));
        verify(flowableAdaptor, never()).getFlowInstanceRepository();

        // When and Then (preHandle)
        testee = new BaseRuntimeFlowableDelegate<>("testActivityId", testTargetTaskInstanceId, testTaskType, testFlowInstanceId,
                testStrategyConfig, flowableAdaptor, eventPublisher, flowInstanceRepository) {
            {
                this.retryExecutor = Mockito.spy(retryExecutor);
                this.taskInstanceCreatedlistener = mock(TaskInstanceCreatedListener.class);
                this.activeTaskStatisticsListener = mock(ActiveTaskStatisticsListener.class);
                this.preHandle(execution);
            }
        };
        verify(flowableAdaptor, times(1)).getFlowInstanceRepository();

        // When and Then (updateFlowInstanceStatus)
        testee.updateFlowInstanceStatus(FlowStatus.COMPLETED);
        verify(flowInstanceRepository, times(1)).updateStatusById(testFlowInstanceId, FlowStatus.COMPLETED);

        // When and Then (bindToFlowTaskInstance)
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        testee.bindToFlowTaskInstance(flowTaskInstance);
        verify(flowTaskInstance, times(1)).bindServiceTask(testee);
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        // Given
        Callable<String> callable = mock(Callable.class);
        when(testee.initCallable(Mockito.any(DelegateExecution.class))).thenReturn(callable);
        when(callable.call()).thenReturn("result");

        // When
        Future<String> future = testee.get();

        // Then
        assertThat(future.get()).isEqualTo("result");
    }

    @Test
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        // Given
        Callable<String> callable = mock(Callable.class);
        when(testee.initCallable(Mockito.any(DelegateExecution.class))).thenReturn(callable);
        when(callable.call()).thenThrow(new RuntimeException());
        doThrow(new TimeoutException()).when(retryExecutor).run(Mockito.any(Callable.class), Mockito.any(Class.class));

        // When and Then
        Assertions.assertThrows(TimeoutException.class, () -> testee.get(1, TimeUnit.SECONDS));
    }

    @Test
    public void testDone() {
        // Given
        testee.done = true;

        // Then
        assertThat(testee.isDone()).isTrue();
    }

    @Test
    public void testIsDoneFalse() {
        // Given
        testee.done = false;

        // Then
        assertThat(testee.isDone()).isFalse();
    }

}