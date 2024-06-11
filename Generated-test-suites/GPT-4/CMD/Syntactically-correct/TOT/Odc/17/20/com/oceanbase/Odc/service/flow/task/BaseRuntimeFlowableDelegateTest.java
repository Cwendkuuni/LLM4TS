package com.oceanbase.odc.service.flow.task;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.flowable.engine.delegate.DelegateExecution;

public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate delegate;

    @Before
    public void setUp() {
        delegate = Mockito.mock(BaseRuntimeFlowableDelegate.class);
    }

    @Test
    public void testIsDone() {
        Mockito.when(delegate.isDone()).thenReturn(true);
        assertTrue(delegate.isDone());
    }
    
    @Test
    public void testGet() throws Exception {
        Object expectedReturnObject = new Object();
        Mockito.when(delegate.get()).thenReturn(expectedReturnObject);
        assertEquals(expectedReturnObject, delegate.get());
    }

    @Test(expected=ExecutionException.class)
    public void testGetWithException() throws Exception {
        Mockito.when(delegate.get()).thenThrow(new InterruptedException());
        delegate.get();
    }
    
    @Test
    public void testGetWithTimeout() throws Exception {
        Object expectedReturnObject = new Object();
        Mockito.when(delegate.get(Mockito.anyLong(), Mockito.any())).thenReturn(expectedReturnObject);
        assertEquals(expectedReturnObject, delegate.get(1L, TimeUnit.SECONDS));
    }

    @Test(expected=TimeoutException.class)
    public void testGetWithTimeoutException() throws Exception {
        Mockito.when(delegate.get(Mockito.anyLong(), Mockito.any())).thenThrow(new TimeoutException());
        delegate.get(1L, TimeUnit.SECONDS);
    }

    @Test
    public void testRun() throws Exception {
        Mockito.doNothing().when(delegate).run(Mockito.any(DelegateExecution.class));
        delegate.run(Mockito.mock(DelegateExecution.class));
        Mockito.verify(delegate, Mockito.times(1)).run(Mockito.any(DelegateExecution.class));
    }

    @Test
    public void testExecute() throws Exception {
        Mockito.doNothing().when(delegate).execute(Mockito.any(DelegateExecution.class));
        delegate.execute(Mockito.mock(DelegateExecution.class));
        Mockito.verify(delegate, Mockito.times(1)).execute(Mockito.any(DelegateExecution.class));
    }
}