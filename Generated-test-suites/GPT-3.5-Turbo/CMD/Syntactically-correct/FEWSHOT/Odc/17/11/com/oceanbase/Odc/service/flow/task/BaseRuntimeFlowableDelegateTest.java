package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.oceanbase.odc.common.util.RetryExecutor;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.core.shared.constant.TaskType;
import com.oceanbase.odc.core.shared.constant.ResourceType;
import com.oceanbase.odc.metadb.flow.FlowInstanceRepository;
import com.oceanbase.odc.service.flow.FlowableAdaptor;
import com.oceanbase.odc.service.flow.event.ServiceTaskStartedEvent;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import com.oceanbase.odc.service.flow.instance.FlowTaskInstance;
import com.oceanbase.odc.service.flow.listener.ActiveTaskStatisticsListener;
import com.oceanbase.odc.service.flow.model.ExecutionStrategyConfig;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution executionMock;

    @Mock
    private FlowableAdaptor flowableAdaptorMock;

    @Mock
    private FlowInstanceRepository flowInstanceRepositoryMock;

    @Mock
    private ServiceTaskStartedEvent eventMock;

    @Mock
    private Callable<String> callableMock;

    private RetryExecutor retryExecutor = RetryExecutor.builder().retryIntervalMillis(1000).retryTimes(3).build();

    @Test
    public void testInitTargetTaskInstanceId() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptorMock);
        delegate.setFlowInstanceRepository(flowInstanceRepositoryMock);

        Mockito.when(executionMock.getProcessDefinitionId()).thenReturn("1");
        Mockito.when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId("1")).thenReturn(Optional.of(1L));
        Mockito.when(flowableAdaptorMock.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.eq(1L)))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        delegate.initTargetTaskInstanceId(executionMock);

        assertEquals("1", delegate.getFlowInstanceId());
        assertNotNull(delegate.getTargetTaskInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
    }

    @Test
    public void testRun() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };
        delegate.setFlowableAdaptor(flowableAdaptorMock);
        delegate.setFlowInstanceRepository(flowInstanceRepositoryMock);
        delegate.setEventPublisher(eventPublisherMock);

        Mockito.when(executionMock.getCurrentActivityId()).thenReturn("1");
        Mockito.when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId("1")).thenReturn(Optional.of(1L));
        Mockito.when(flowableAdaptorMock.getTaskInstanceByActivityId(Mockito.anyString(), Mockito.eq(1L)))
                .thenReturn(Optional.of(new FlowTaskInstance()));

        Mockito.doNothing().when(retryExecutorMock).run(Mockito.any(), Mockito.any());

        delegate.run(executionMock);

        assertNotNull(delegate.getFlowInstanceId());
        assertNotNull(delegate.getTaskType());
        assertNotNull(delegate.getStrategyConfig());
        assertFalse(delegate.isDone());
    }

    @Test
    public void testIsDone() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        assertFalse(delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        delegate.done = true;
        delegate.returnObject = "Test";

        assertEquals("Test", delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        delegate.done = true;
        delegate.thrown = new Exception();

        delegate.get();
    }

    @Test
    public void testPreHandle() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        delegate.preHandle(executionMock);

        // Add test logic for preHandle method
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        delegate.done = true;
        delegate.returnObject = "Test";

        assertEquals("Test", delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeoutException() throws Exception {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(1L);

        delegate.setTargetTaskInstanceId(1L);
        delegate.setActivityId("1");

        delegate.bindToFlowTaskInstance(taskInstance);

        // Add test logic for bindToFlowTaskInstance method
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        BaseRuntimeFlowableDelegate<String> delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return callableMock;
            }
        };

        delegate.setFlowInstanceId(1L);

        delegate.updateFlowInstanceStatus(FlowStatus.COMPLETED);

        // Add test logic for updateFlowInstanceStatus method
    }
}