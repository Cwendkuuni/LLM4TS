package com.oceanbase.odc.service.flow.task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.Callable;
import org.flowable.engine.delegate.DelegateExecution;

public class BaseRuntimeFlowableDelegateTest {

    private BaseRuntimeFlowableDelegate<TestObject> baseRuntimeFlowableDelegate;

    @Before
    public void setUp() {
        baseRuntimeFlowableDelegate = new BaseRuntimeFlowableDelegate<TestObject>() {
            @Override
            protected Callable<TestObject> initCallable(DelegateExecution execution) {
                return null;
            }
        };
    }

    @Test
    public void testInit() {
        baseRuntimeFlowableDelegate.init(null);
    }

    @Test(expected = ServiceTaskError.class)
    public void testRunWithNullExecution() throws Exception {
        baseRuntimeFlowableDelegate.run(null);
    }

    @Test
    public void testPreHandle() {
        baseRuntimeFlowableDelegate.preHandle(null);
    }

    @Test
    public void testExecute() {
        baseRuntimeFlowableDelegate.execute(null);
    }

    @Test
    public void testIsDone() {
        Assert.assertFalse(baseRuntimeFlowableDelegate.isDone());
    }

    @Test(expected = InterruptedException.class)
    public void testGet() throws Exception {
        baseRuntimeFlowableDelegate.get();
    }

    @Test(expected = TimeoutException.class)
    public void testGetWithTimeout() throws Exception {
        baseRuntimeFlowableDelegate.get(1, TimeUnit.SECONDS);
    }

    @Test
    public void testBindToFlowTaskInstance() {
        baseRuntimeFlowableDelegate.bindToFlowTaskInstance(null);
    }

    @Test
    public void testUpdateFlowInstanceStatus() {
        baseRuntimeFlowableDelegate.updateFlowInstanceStatus(null);
    }

    @Test
    public void testInitCallable() {
        Assert.assertNull(baseRuntimeFlowableDelegate.initCallable(null));
    }

    private static class TestObject {
    }
}