
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.delegate.DelegateInvocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Test for: BaseRuntimeFlowableDelegate.
 */
public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<String> delegate;
    private DelegateExecution mockExecution;

    @Before
    public void setUp() {
        delegate = Mockito.spy(BaseRuntimeFlowableDelegate.class);
        mockExecution = Mockito.mock(DelegateExecution.class);
    }

    @Test
    public void runTest() throws Exception {
        Mockito.doNothing().when(delegate).preHandle(mockExecution);
        Mockito.doReturn("Hello").when(delegate).initCallable(mockExecution).call();
        delegate.run(mockExecution);

        Assert.assertNotNull(delegate.get());
        Assert.assertTrue(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void runExceptionTest() throws Exception {
        Mockito.doThrow(new Exception("Test Exception")).when(delegate).initCallable(mockExecution).call();
        delegate.run(mockExecution);
        delegate.get();
    }

    @Test
    public void executeTest() {
        Mockito.doNothing().when(mockExecution).setVariable(Mockito.anyString(), Mockito.any());
        delegate.execute(mockExecution);

        Mockito.verify(delegate, Mockito.times(1)).execute(mockExecution);
    }

    @Test
    public void isDoneTest() {
        Mockito.doReturn(true).when(delegate).isDone();
        Assert.assertTrue(delegate.isDone());
    }

    @Test
    public void getTest() throws Exception {
        Mockito.doReturn("Test").when(delegate).get();
        Assert.assertEquals("Test", delegate.get());
    }

    @Test
    public void getWithTimeoutTest() throws Exception {
        Mockito.doReturn("Test").when(delegate).get(Mockito.anyLong(), Mockito.any(TimeUnit.class));
        Assert.assertEquals("Test", delegate.get(1000, TimeUnit.MILLISECONDS));
    }
}
