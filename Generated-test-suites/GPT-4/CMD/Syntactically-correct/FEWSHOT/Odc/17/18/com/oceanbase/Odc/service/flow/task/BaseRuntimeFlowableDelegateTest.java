
package com.oceanbase.odc.service.flow.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BaseRuntimeFlowableDelegateTest {

    @Mock
    private DelegateExecution delegateExecution;

    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<Object>(){
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution){
                return () -> null;
            }
        });
    }

    @Test
    public void testRun() throws Exception {
        Mockito.when(delegateExecution.getCurrentActivityId()).thenReturn("activity");
        delegate.run(delegateExecution);
        Mockito.verify(delegate, Mockito.times(1)).initCallable(delegateExecution);
    }

    @Test
    public void testExecute() {
        delegate.execute(delegateExecution);
        Mockito.verify(delegate, Mockito.times(1)).removeListeners();
    }

    @Test
    public void testIsDone() throws Exception {
        assertEquals(false, delegate.isDone());
        delegate.run(delegateExecution);
        assertEquals(true, delegate.isDone());
    }

    @Test
    public void testGet() throws Exception {
        delegate.run(delegateExecution);
        assertNull(delegate.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithException() throws Exception {
        delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<Object>(){
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution){
                return () -> {throw new Exception();};
            }
        });
    
        delegate.run(delegateExecution);
        delegate.get();
    }

    @Test
    public void testGetWithTimeout() throws Exception {
        delegate.run(delegateExecution);
        assertNull(delegate.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutException() throws Exception {
        delegate = Mockito.spy(new BaseRuntimeFlowableDelegate<Object>(){
            @Override
            protected Callable<Object> initCallable(DelegateExecution execution){
                return () -> {throw new Exception();};
            }
        });
    
        delegate.run(delegateExecution);
        delegate.get(1, TimeUnit.SECONDS);
    }
}
