
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.ActivityBehavior;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Integer> testDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Mock
    private FlowableAdaptor flowableAdaptor;

    @Before
    public void setUp() {

        when(delegateExecution.getCurrentActivityId()).thenReturn("TestActivityId");

        when(flowableAdaptor.getFlowInstanceIdByProcessDefinitionId(anyString()))
                .thenReturn(Optional.ofNullable(Optional.of(1000L)));
        when(flowableAdaptor.getTaskInstanceByActivityId(anyString(), anyLong()))
                .thenReturn(Optional.ofNullable(mock(FlowTaskInstance.class)));
    }

    @Test
    public void testExecute() throws Exception {

        when(delegateExecution.getCurrentFlowElement()).thenReturn(mock(ActivityBehavior.class));

        testDelegate.execute(delegateExecution);

        verify(delegateExecution, times(2)).getCurrentActivityId();
        verify(flowableAdaptor, times(1)).getFlowInstanceIdByProcessDefinitionId(anyString());
        verify(flowableAdaptor, times(1)).getTaskInstanceByActivityId(anyString(), anyLong());
    }

    @Test
    public void testRun() throws Exception {

        ExecutionEntityImpl executionEntity = new ExecutionEntityImpl();
        executionEntity.setCurrentFlowElement(mock(ActivityBehavior.class));

        testDelegate.run(executionEntity);

        assertTrue(testDelegate.isDone());
    }

    @Test
    public void testGet() throws Exception {

        Integer expectedValue = 5;
        testDelegate = new BaseRuntimeFlowableDelegate<Integer>() {

            @Override
            protected Callable<Integer> initCallable(DelegateExecution execution) {
                return () -> expectedValue;
            }
        };
        
        testDelegate.run(delegateExecution);
        
        assertEquals(expectedValue, testDelegate.get());
    }
    
    @Test(expected = ExecutionException.class)
    public void testGetThrowException() throws Exception {
        testDelegate.run(mock(DelegateExecution.class));
        testDelegate.get();
    }
}
