package com.oceanbase.odc.service.flow.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class BaseRuntimeFlowableDelegateTest {
    private BaseRuntimeFlowableDelegate<Integer> target;

    @Mock
    private CountDownLatch latch;

    @Before
    public void setUp() {
        target = Mockito.mock(BaseRuntimeFlowableDelegate.class, Mockito.CALLS_REAL_METHODS);
        Whitebox.setInternalState(target, "latch", latch);
    }

    @Test
    public void testIsDone() {
        Whitebox.setInternalState(target, "done", true);
        assertTrue(target.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Whitebox.setInternalState(target, "returnObject", 100);
        Whitebox.setInternalState(target, "done", true);
        PowerMockito.doNothing().when(latch).await();
        Integer result = target.get();
        assertNotNull(result);
        assertEquals(100, result.intValue());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithFailure() throws InterruptedException, ExecutionException {
        Whitebox.setInternalState(target, "done", true);
        Whitebox.setInternalState(target, "thrown", new Exception());
        PowerMockito.doNothing().when(latch).await();
        target.get();
    }

    // similar tests can be written for the "get" method with parameters
}
