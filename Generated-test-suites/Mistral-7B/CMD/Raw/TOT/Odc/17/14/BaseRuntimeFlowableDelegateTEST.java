package com.oceanbase.odc.service.flow.task;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.MockitoAnnotations;import org.mockito.MockitoSession;import org.mockito.invocation.InvocationOnMock;import org.mockito.stub.Answer;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.core.flow.BaseRuntimeFlowableDelegate.TaskType;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.listener.TaskInstanceCreatedListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;
    @Mock
    private FlowableAdaptor flowableAdaptor;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private FlowInstanceRepository flowInstanceRepository;
    @Mock
    private RetryExecutor retryExecutor;
    @Mock
    private ServiceTaskStartedEvent serviceTaskStartedEvent;
    @Mock
    private CountDownLatch latch;
    @Mock
    private ActiveTaskStatisticsListener activeTaskStatisticsListener;
    @Mock
    private TaskInstanceCreatedListener taskInstanceCreatedlistener;
    @Mock
    private FlowTaskInstance taskInstance;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> testee;

    private MockitoSession mockitoSession;

    @Before
    public void setup() {
        mockitoSession = MockitoAnnotations.initMocks(this);
        when(execution.getCurrentActivityId()).thenReturn("testActivityId");
        when(execution.getProcessDefinitionId()).thenReturn("testProcessDefinitionId");
        when(execution.getId()).thenReturn(1L);
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(eq("testProcessDefinitionId"))).thenReturn(2L);
        when(flowInstanceRepository.updateStatusById(eq(2L), eq(FlowStatus.EXECUTING))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        when(flowableAdaptor.getTaskInstanceByActivityId(eq("testActivityId"), eq(2L))).thenReturn(taskInstance);
        when(taskInstance.getId()).thenReturn(3L);
        when(taskInstance.getTaskType()).thenReturn(TaskType.TEST_TASK_TYPE);
        when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig());
        when(taskInstance.bindServiceTask(any(BaseRuntimeFlowableDelegate.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });
        doAnswer(new Answer<FlowTaskInstance>() {
            @Override
            public FlowTaskInstance answer(InvocationOnMock invocation) throws Throwable {
                return taskInstance;
            }
        }).when(flowableAdaptor).getTaskInstanceByActivityId(eq("testActivityId"), eq(2L));
        when(eventPublisher.publishEvent(any(ServiceTaskStartedEvent.class))).thenReturn(serviceTaskStartedEvent);
        when(eventPublisher.removeEventListener(any()))
                .thenAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock invocation) throws Throwable {
                        return null;
                    }
                });
        when(eventPublisher.addEventListener(eq(activeTaskStatisticsListener))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });
        when(eventPublisher.addEventListener(eq(taskInstanceCreatedlistener))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });
        when(retryExecutor.build()).thenReturn(retryExecutor);
        when(retryExecutor.retryIntervalMillis(anyInt())).thenAnswer(new Answer<RetryExecutor>() {
            @Override
            public RetryExecutor answer(InvocationOnMock invocation) throws Throwable {
                return retryExecutor;
            }
        });
        when(retryExecutor.retryTimes(anyInt())).thenAnswer(new Answer<RetryExecutor>() {
            @Override
            public RetryExecutor answer(InvocationOnMock invocation) throws Throwable {
                return retryExecutor;
            }
        });
        when(latch.await(anyLong(), any(TimeUnit.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        when(latch.await()).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        when(latch.getCount()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return 1;
            }
        });
    }

    @Test
    public void testInitCallable() {
        PreConditions.assertTrue(testee.initCallable(execution) instanceof Callable);
    }

    @Test
    public void testGet() throws Exception {
        testee.initCallable(execution);
        testee.bindToFlowTaskInstance(taskInstance);
        testee.preHandle(execution);
        testee.run(execution);
        testee.get();
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEvent);
        verify(taskInstance, times(1)).bindServiceTask(testee);
        verify(flowInstanceRepository, times(1)).updateStatusById(eq(2L), eq(FlowStatus.EXECUTING));
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        testee.initCallable(execution);
        testee.bindToFlowTaskInstance(taskInstance);
        testee.preHandle(execution);
        testee.run(execution);
        testee.get(1, TimeUnit.SECONDS);
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEvent);
        verify(taskInstance, times(1)).bindServiceTask(testee);
        verify(flowInstanceRepository, times(1)).updateStatusById(eq(2L), eq(FlowStatus.EXECUTING));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithExecutionException() throws Exception {
        when(taskInstance.getStrategyConfig()).thenReturn(new ExecutionStrategyConfig() {
            @Override
            public int getMaxRetryCount() {
                return 0;
            }
        });
        testee.initCallable(execution);
        testee.bindToFlowTaskInstance(taskInstance);
        testee.preHandle(execution);
        doThrow(new RuntimeException("Test exception")).when(testee).call();
        testee.run(execution);
        testee.get();
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEvent);
        verify(taskInstance, times(1)).bindServiceTask(testee);
        verify(flowInstanceRepository, never()).updateStatusById(eq(2L), eq(FlowStatus.EXECUTING));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws Exception {
        testee.initCallable(execution);
        testee.bindToFlowTaskInstance(taskInstance);
        testee.preHandle(execution);
        doThrow(new RuntimeException("Test exception")).when(testee).call();
        testee.run(execution);
        testee.get(1, TimeUnit.MILLISECONDS);
        verify(eventPublisher, times(1)).publishEvent(serviceTaskStartedEvent);
        verify(taskInstance, times(1)).bindServiceTask(testee);
        verify(flowInstanceRepository, never()).updateStatusById(eq(2L), eq(FlowStatus.EXECUTING));
    }

    @After
    public void teardown() {
        mockitoSession.finishMocking();
    }
}