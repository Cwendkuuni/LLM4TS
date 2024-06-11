package com.oceanbase.odc.service.flow.instance;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class FlowTaskInstanceTest {

    @Test
    public void testBindServiceTask() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        BaseRuntimeFlowableDelegate<?> target = new BaseRuntimeFlowableDelegate<>();
        instance.bindServiceTask(target);
        assertTrue(instance.isPresentOnThisMachine());
    }

    @Test
    public void testConfirmExecute() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        instance.confirmExecute();
        // Add assertion here
    }

    @Test
    public void testAbort() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        instance.abort();
        // Add assertion here
    }

    @Test
    public void testCancel() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        try {
            instance.cancel(true);
        } catch (UnsupportedOperationException e) {
            assertEquals("Task isn't on this machine", e.getMessage());
        }
    }

    @Test
    public void testIsDone() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        try {
            instance.isDone();
        } catch (UnsupportedOperationException e) {
            assertEquals("Task isn't on this machine", e.getMessage());
        }
    }

    @Test
    public void testGetResult() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        try {
            instance.getResult();
        } catch (UnsupportedOperationException e) {
            assertEquals("Task isn't on this machine", e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testGetResultWithTimeout() {
        FlowTaskInstance instance = new FlowTaskInstance();
        assertNotNull(instance);
        try {
            instance.getResult(1, TimeUnit.SECONDS);
        } catch (UnsupportedOperationException e) {
            assertEquals("Task isn't on this machine", e.getMessage());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            fail("Unexpected exception thrown");
        }
    }

}