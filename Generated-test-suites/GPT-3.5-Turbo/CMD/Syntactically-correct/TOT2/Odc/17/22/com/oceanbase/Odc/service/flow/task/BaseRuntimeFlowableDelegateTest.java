
package com.oceanbase.odc.service.flow.task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testRun_WithValidExecution_ShouldSucceed() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(delegate.initCallable(execution)).thenReturn(() -> {return "Success";});
        delegate.run(execution);

        assertTrue(delegate.isDone());
        assertEquals("Success", delegate.get());
    }

    @Test(expected = Exception.class)
    public void testRun_WithExceptionInInitCallable_ShouldThrowServiceTaskError() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(delegate.initCallable(execution)).thenThrow(new Exception("InitCallable Exception"));
        delegate.run(execution);
    }

    @Test(expected = Exception.class)
    public void testRun_WithExceptionInCall_ShouldThrowOriginalException() throws Exception {
        DelegateExecution execution = Mockito.mock(DelegateExecution.class);

        when(delegate.initCallable(execution)).thenReturn(() -> {throw new Exception("Call Exception");});
        delegate.run(execution);
    }

    @Test
    public void testGet_WithTimeout_ShouldThrowTimeoutException() throws Exception {
        delegate = new BaseRuntimeFlowableDelegate<String>() {
            @Override
            protected Callable<String> initCallable(DelegateExecution execution) {
                return () -> {
                    TimeUnit.SECONDS.sleep(3);
                    return "Done";
                };
            }
        };

        delegate.run(Mockito.mock(DelegateExecution.class));
        try {
            delegate.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testBindToFlowTaskInstance_WithMatchingTaskInstance_ShouldBind() {
        FlowTaskInstance taskInstance = Mockito.mock(FlowTaskInstance.class);
        taskInstance.setId(1L);

        delegate.setTargetTaskInstanceId(1L);
        delegate.bindToFlowTaskInstance(taskInstance);

        assertEquals(delegate, taskInstance.getServiceTask());
        verify(taskInstance, times(1)).dealloc();
    }

}
