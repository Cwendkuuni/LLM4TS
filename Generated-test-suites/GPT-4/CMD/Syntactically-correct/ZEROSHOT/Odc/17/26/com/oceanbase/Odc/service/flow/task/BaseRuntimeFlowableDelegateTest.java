
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.shared.constant.FlowStatus;


public class BaseRuntimeFlowableDelegateTest {
    
    @Mock
    private FlowableAdaptor flowableAdaptorMock;

    private BaseRuntimeFlowableDelegate delegate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        delegate = new BaseRuntimeFlowableDelegate() {
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution) {
                return () -> null;
            }
        };
       // mocking flowableAdaptor in delegate
       delegate.flowableAdaptor = flowableAdaptorMock;
    }

    @Test
    public void testRun() throws Exception {
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getProcessDefinitionId()).thenReturn("TEST_ID");
        when(execution.getCurrentActivityId()).thenReturn("ACTIVITY_ID");

        when(flowableAdaptorMock.getFlowInstanceIdByProcessDefinitionId("TEST_ID")).thenReturn(Optional.of(1L));
        FlowTaskInstance taskInstance = mock(FlowTaskInstance.class);
        when(flowableAdaptorMock.getTaskInstanceByActivityId("ACTIVITY_ID", 1L)).thenReturn(Optional.of(taskInstance));

        assertNotNull(delegate.run(execution));
    }

    @Test
    public void testExecute() {
        DelegateExecution execution = mock(DelegateExecution.class);
        delegate.execute(execution);
    }

    @Test
    public void testIsDone() {
        assertNotNull(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithInterruptedException() throws Exception {
        delegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeout() throws Exception {
        delegate.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testGet() throws Exception {
        delegate.get();
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        assertNotNull(delegate.updateFlowInstanceStatus(FlowStatus.NORMAL));
    }
}
