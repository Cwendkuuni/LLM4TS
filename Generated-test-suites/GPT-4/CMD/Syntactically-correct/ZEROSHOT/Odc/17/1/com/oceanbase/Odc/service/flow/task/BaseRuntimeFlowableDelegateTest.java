
package com.oceanbase.odc.service.flow.task;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class BaseRuntimeFlowableDelegateTest {

    @InjectMocks
    BaseRuntimeFlowableDelegate testDelegate;

    @Mock
    private DelegateExecution delegateExecution;

    @Before
    public void setup() {
        when(delegateExecution.getCurrentActivityId()).thenReturn("testActivityId");
    }

    @Test
    public void testExecute() {
        try {
            testDelegate.execute(delegateExecution);
            verify(delegateExecution).getCurrentActivityId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsDone() {
        try {
            boolean done = testDelegate.isDone();
            Assert.assertFalse(done);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ExecutionException.class)
    public void testGet() throws ExecutionException, InterruptedException {
            testDelegate.get();
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeout() throws Exception {
        testDelegate.get(1L, TimeUnit.SECONDS);
    }

    @Test
    public void testPreHandle() {
        testDelegate.preHandle(delegateExecution);
        verify(delegateExecution).getCurrentActivityId();
    }
}
