package com.oceanbase.odc.service.flow.task;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import static org.mockito.Mockito.*;

public class BaseRuntimeFlowableDelegateTest extends TestCase{

    @Mock
    private DelegateExecution mockDelegateExecution;

    private TestableBaseRuntimeFlowableDelegate testObj;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testObj = new TestableBaseRuntimeFlowableDelegate();
    }

    @Test
    public void testInitTargetTaskInstanceId() throws Exception {
        testObj.initTargetTaskInstanceId(mockDelegateExecution);
        assertNotNull(testObj.getFlowInstanceId());
    }

    @Test
    public void testUpdateFlowInstanceStatus() throws Exception {
        testObj.updateFlowInstanceStatus(FlowStatus.CANCELLED);
        verify(testObj.getFlowInstanceRepository(), times(1)).updateStatusById(anyLong(), any(FlowStatus.class));
    }

    @Test
    public void testIsDone() throws Exception {
        assertFalse(testObj.isDone());
    }

    @Test
    public void testGet() throws Exception {
        FutureTask<String> future = new FutureTask<>(() -> "Test");
        future.run();
        while(!future.isDone()){
            Thread.sleep(1000);
        }
        assertEquals("Test", future.get());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWhenExceptionThrown() throws Exception {
        FutureTask<String> future = new FutureTask<>(() -> {throw new NullPointerException();});
        future.run();
        while(!future.isDone()){
            Thread.sleep(1000);
        }
        future.get();
    }

    @Test(timeout = 2000)
    public void testGetWithTimeout() throws Exception {
        FutureTask<String> future = new FutureTask<>(() -> "Test");
        future.run();
        assertEquals("Test", future.get(1, TimeUnit.SECONDS));
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutWhenExceptionThrown() throws Exception {
        FutureTask<String> future = new FutureTask<>(() -> {throw new NullPointerException();});
        future.run();
        future.get(1, TimeUnit.SECONDS);
    }

    private class TestableBaseRuntimeFlowableDelegate extends BaseRuntimeFlowableDelegate<String> {

        @Override
        protected Callable<String> initCallable(DelegateExecution execution) {
            return () -> "TEST";
        }
    }
}
