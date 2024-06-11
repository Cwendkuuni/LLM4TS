
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.delegate.ActivityBehavior;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private FlowInstanceRepository flowInstanceRepository;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Mock
    private DelegateExecution execution;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegateTestable();

    @Before
    public void setUp() {
        Context.setProcessEngineConfiguration(ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault());
    }

    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate.init(eventPublisher);
        verify(eventPublisher).addEventListener(any());
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(execution);
        verify(eventPublisher).removeEventListener(any());
    }

    @Test
    public void testIsDone_Default_False() {
        Assert.assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGet_Interrupted_ThrowsException() throws InterruptedException, ExecutionException {
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGet_LongTimeUnit_Interrupted_ThrowsException() throws InterruptedException, ExecutionException, TimeoutException {
        baseRuntimeFlowableDelegate.get(100, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        FlowTaskInstance flowTaskInstance = new FlowTaskInstance();
        flowTaskInstance.setId(1L);
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(flowTaskInstance);

        verify(flowTaskInstance).bindServiceTask(any());
        Assert.assertEquals(flowTaskInstance.getId(), baseRuntimeFlowableDelegate.getTargetTaskInstanceId());
    }

    private static class BaseRuntimeFlowableDelegateTestable extends BaseRuntimeFlowableDelegate<Object> {
        @Override
        protected Callable<Object> initCallable(DelegateExecution execution) {
            return () -> null;
        }
    }
}
