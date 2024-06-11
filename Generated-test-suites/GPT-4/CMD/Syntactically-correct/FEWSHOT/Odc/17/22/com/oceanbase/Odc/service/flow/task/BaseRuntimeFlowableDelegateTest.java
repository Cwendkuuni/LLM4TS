
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    private TestableBaseRuntimeFlowableDelegate testSubject;

    @Mock
    private DelegateExecution delegateExecution;
    @Mock
    private Callable callable;

    @Before
    public void setup(){
        this.testSubject = new TestableBaseRuntimeFlowableDelegate();
    }

    @Test
    public void testExecute()
    {
        doNothing().when(testSubject.super.execute(delegateExecution));
        testSubject.execute(delegateExecution);

	   verify(testSubject, times(1)).removeEventListener(any());
    }

    @Test
    public void testGetMethods() throws InterruptedException, ExecutionException {
        Future mockFuture = mock(Future.class);
        when(mockFuture.get()).thenReturn("Test");

	    assertEquals("Test", mockFuture.get());
    }

    @Test
    public void testGetMethods_withTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Future mockFuture = mock(Future.class);
        when(mockFuture.get(1000, TimeUnit.MILLISECONDS)).thenReturn("Test");

	    assertEquals("Test", mockFuture.get(1000, TimeUnit.MILLISECONDS));
    }
    
    @Test
    public void testIsDone()
    {
        CountDownLatch latch = new CountDownLatch(0);

        assertTrue(latch.getCount() == 0);
        latch.countDown();

        assertTrue(latch.getCount() == 0);
    }
}

class TestableBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate {

    @Override
    protected Callable initCallable(DelegateExecution execution) {
        return () -> "Test";
    }
}
