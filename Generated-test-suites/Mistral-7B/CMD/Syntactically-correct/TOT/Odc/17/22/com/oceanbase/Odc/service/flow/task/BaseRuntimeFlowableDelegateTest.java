import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.common.constant.FlowStatus;
import com.oceanbase.odc.common.exception.RuntimeExceptionWrapper;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.ExecutorService;
import com.oceanbase.odc.core.flow.FlowableAdaptor;
import com.oceanbase.odc.core.flow.FlowInstanceRepository;
import com.oceanbase.odc.core.shared.event.EventPublisher;
import com.oceanbase.odc.core.shared.util.RetryExecutor;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepositoryImpl;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.FlowableAdaptorImpl;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({BaseRuntimeFlowableDelegate.class, ExecutorService.class})
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private RetryExecutor retryExecutor;

    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;

    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;

    @Captor
    private ArgumentCaptor<ServiceTaskStartedEvent> serviceTaskStartedEventCaptor;

    private ExecutionStrategyConfig strategyConfig;
    private DelegateExecution execution;
    private Callable<Object> callable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(ExecutorService.class);
        PowerMockito.mockStatic(FlowInstanceRepository.class);
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<>();
        baseRuntimeFlowableDelegate.init(eventPublisher);
        strategyConfig = new ExecutionStrategyConfig();
        execution = mock(DelegateExecution.class);
        callable = mock(Callable.class);

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(any())).thenReturn(1L);
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong())).thenReturn(mock(FlowTaskInstance.class));
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenReturn(true);
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(TaskInstanceCreatedListener.class))).thenReturn(null);
        when(eventPublisher.removeEventListener(any(ActiveTaskStatisticsListener.class))).thenReturn(null);
        when(retryExecutor.run(any(Callable.class), any(Function.class))).thenAnswer(invocation -> invocation.callRealMethod());
    }

    @Test
    public void testRun() throws Exception {
        // Given
        doAnswer(invocation -> {
            baseRuntimeFlowableDelegate.activityId = "activityId";
            baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
            baseRuntimeFlowableDelegate.callable = callable;
            baseRuntimeFlowableDelegate.preHandle(execution);
            return null;
        }).when(baseRuntimeFlowableDelegate).run(execution);

        // When
        baseRuntimeFlowableDelegate.run(execution);

        // Then
        verify(eventPublisher).publishEvent(serviceTaskStartedEventCaptor.capture());
        ServiceTaskStartedEvent serviceTaskStartedEvent = serviceTaskStartedEventCaptor.getValue();
        assertThat(serviceTaskStartedEvent, is(notNullValue()));
        assertThat(serviceTaskStartedEvent.getTaskInstance(), is(notNullValue()));
        baseRuntimeFlowableDelegate.get();
    }

    @Test
    public void testRunWithException() throws Exception {
        // Given
        when(callable.call()).thenThrow(new RuntimeExceptionWrapper("Test error"));

        // When & Then
        try {
            baseRuntimeFlowableDelegate.run(execution);
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            // Expected exception
        }
        verify(eventPublisher).removeEventListener(taskInstanceCreatedlistener);
        verify(eventPublisher).removeEventListener(activeTaskStatisticsListener);
    }

    @Test
    public void testGet() throws Exception {
        // Given
        doAnswer(invocation -> {
            baseRuntimeFlowableDelegate.activityId = "activityId";
            baseRuntimeFlowableDelegate.initTargetTaskInstanceId(execution);
            baseRuntimeFlowableDelegate.callable = callable;
            baseRuntimeFlowableDelegate.preHandle(execution);
            Object result = callable.call();
            baseRuntimeFlowableDelegate.done = true;
            baseRuntimeFlowableDelegate.returnObject = result;
            return result;
        }).when(baseRuntimeFlowableDelegate).run(execution);

        // When
        Object result = baseRuntimeFlowableDelegate.get();

        // Then
        verify(baseRuntimeFlowableDelegate).done = true;
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testGetTimeout() throws Exception {
        // Given
        when(callable.call()).thenCallRealMethod();
        when(flowInstanceRepository.updateStatusById(anyLong(), any(FlowStatus.class))).thenAnswer(i -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return true;
        });

        // When & Then
        try {
            baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
            fail("Expected TimeoutException was not thrown");
        } catch (TimeoutException e) {
            // Expected exception
        }
    }
}