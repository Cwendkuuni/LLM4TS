package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoJUnit;

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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MockitoJUnit
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Mock
    private FlowTaskInstance taskInstance;

    @Mock
    private Callable<Object> callable;

    @Getter
    private CountDownLatch latch;

    @Getter
    private RetryExecutor retryExecutor;

    @Getter
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Getter
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Before
    public void setUp() {
        this.retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();
        this.latch = new CountDownLatch(1);
        this.taskInstanceCreatedlistener = new TaskInstanceCreatedListener(delegate);
        this.activeTaskStatisticsListener = new ActiveTaskStatisticsListener(delegate);
    }

    @Test
    public void testInit() {
        assertNotNull(delegate.getRetryExecutor());
        assertNotNull(delegate.getLatch());
        assertNotNull(delegate.getTaskInstanceCreatedlistener());
        assertNotNull(delegate.getActiveTaskStatisticsListener());
    }

    @Test
    public void testRun() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(Optional.of(taskInstance));

        DelegateExecution execution = mock(DelegateExecution.class);

        when(execution.getCurrentActivityId()).thenReturn("activityId");
        when(execution.getProcessDefinitionId()).thenReturn("processDefinitionId");

        delegate.init(eventPublisher);
        delegate.initTargetTaskInstanceId(execution);

        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(eq("processDefinitionId"));
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(eq("activityId"), anyLong());
        verify(taskInstance, times(1)).dealloc();
        verify(taskInstance, times(1)).bindServiceTask(any());
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        when(callable.call()).thenReturn("result");
        delegate.setCallable(callable);

        assertEquals("result", delegate.get());
        verify(latch, times(1)).await();
        verifyNoMoreInteractions(latch);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        when(flowInstanceRepository.updateStatusById(anyLong(), anyInt())).thenReturn(1);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        verify(flowInstanceRepository, times(1)).updateStatusById(eq(delegate.getFlowInstanceId()), eq(FlowStatus.COMPLETED.getValue()));
    }

    @Test
    public void testExecute() {
        DelegateExecution execution = mock(DelegateExecution.class);

        doCallRealMethod().when(delegate).execute(execution);

        delegate.execute(execution);

        verify(delegate, times(1)).execute(eq(execution));
        verify(eventPublisher, times(1)).removeEventListener(eq(taskInstanceCreatedlistener));
        verify(eventPublisher, times(1)).removeEventListener(eq(activeTaskStatisticsListener));
    }

    @Test
    public void testIsDone() {
        when(delegate.isDone()).thenReturn(true);

        assertTrue(delegate.isDone());
    }

    @Test
    public void testGetCallable() throws NoSuchMethodException, SecurityException {
        Method method = BaseRuntimeFlowableDelegate.class.getDeclaredMethod("initCallable", DelegateExecution.class);
        method.setAccessible(true);

        assertEquals(callable, delegate.initCallable(mock(DelegateExecution.class)));
    }

}