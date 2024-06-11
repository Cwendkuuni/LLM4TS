package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution execution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    private BaseRuntimeFlowableDelegate<TestObject> testDelegate;

    private static class TestObject {}

    @Before
    public void setUp() {
        testDelegate = spy(new BaseRuntimeFlowableDelegate<TestObject>() {
            @Override
            protected Callable<TestObject> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        });
        testDelegate.setActivityId("testActivityId");
        testDelegate.setFlowInstanceId(1L);
        testDelegate.setTargetTaskInstanceId(2L);
        testDelegate.setTaskType(TaskType.TYPE1);
        testDelegate.setStrategyConfig(new ExecutionStrategyConfig());
        testDelegate.setFlowableAdaptor(flowableAdaptor);
        testDelegate.setEventPublisher(eventPublisher);
        testDelegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testInitTargetTaskInstanceId() {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString())).thenReturn(Optional.of(Optional.of(1L)));
        when(flowableAdaptor.getTaskInstanceByActivityId("testActivityId", 1L)).thenReturn(Optional.of(new FlowTaskInstance()));

        testDelegate.initTargetTaskInstanceId(execution);

        assertEquals(1L, testDelegate.getFlowInstanceId().longValue());
        assertEquals(2L, testDelegate.getTargetTaskInstanceId().longValue());
        assertEquals(TaskType.TYPE1, testDelegate.getTaskType());
        assertNotNull(testDelegate.getStrategyConfig());
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance taskInstance = new FlowTaskInstance();
        taskInstance.setId(2L);

        testDelegate.setTargetTaskInstanceId(2L);
        testDelegate.setActivityId("testActivityId");

        testDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(any());

        taskInstance.setId(3L);
        testDelegate.bindToFlowTaskInstance(taskInstance);
        verify(taskInstance, times(1)).bindServiceTask(any());
    }

    @Test
    public void testExecute() {
        testDelegate.execute(execution);
        verify(eventPublisher).removeEventListener(any());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        testDelegate.get();
        // Not throwing exception is considered as success
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        testDelegate.get(1, TimeUnit.MILLISECONDS);
    }

}