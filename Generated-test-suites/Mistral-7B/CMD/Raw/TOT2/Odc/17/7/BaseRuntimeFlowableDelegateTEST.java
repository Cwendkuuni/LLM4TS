package com.oceanbase.odc.service.flow.task;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.flow.BaseFlowableDelegate;
import com.oceanbase.odc.core.shared.constant.FlowStatus;
import com.oceanbase.odc.service.flow.BaseRuntimeFlowableDelegate;
import com.oceanbase.odc.service.flow.exception.ServiceTaskError;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for BaseRuntimeFlowableDelegate.
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private EventPublisher eventPublisher;
    private DelegateExecution execution;
    private FlowableAdaptor flowableAdaptor;
    private FlowInstanceRepository flowInstanceRepository;

    @Before
    public void setup() {
        eventPublisher = mock(EventPublisher.class);
        delegate = new BaseRuntimeFlowableDelegate<>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "test result";
                    }
                };
            }
        };
        delegate.setEventPublisher(eventPublisher);
        execution = mock(DelegateExecution.class);
        flowableAdaptor = mock(FlowableAdaptor.class);
        flowInstanceRepository = mock(FlowInstanceRepository.class);
        delegate.setFlowableAdaptor(flowableAdaptor);
        delegate.setFlowInstanceRepository(flowInstanceRepository);
    }

    @Test
    public void testRunHappyPath() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId")).thenReturn(Optional.of(1L));
        when(flowableAdaptor.getTaskInstanceByActivityId("activityId", 1L)).thenReturn(Optional.of(new FlowTaskInstance()));
        delegate.run(execution);
        Assert.assertEquals("test result", delegate.get());
        Assert.assertTrue(delegate.isDone());
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunErrorPath() throws Exception {
        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId("processDefinitionId"))
                .thenThrow(new RuntimeException("error"));
        delegate.run(execution);
        Assert.assertNull(delegate.get());
        Assert.assertTrue(delegate.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        Assert.assertEquals("test result", delegate.get());
        Assert.assertTrue(delegate.isDone());
    }

    @Test(expected = TimeoutException.class)
    public void testGetTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        delegate.run(execution);
        Assert.assertFalse(delegate.isDone());
        delegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testInitCallable() {
        Callable<String> callable = delegate.initCallable(execution);
        Assert.assertNotNull(callable);
    }
}