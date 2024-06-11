package com.oceanbase.odc.service.flow.instance;
import com.oceanbase.odc.service.flow.instance.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlowTaskInstanceTest {

    @Test
    public void testSetTargetTaskId() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.setTargetTaskId(null);
        assertNull(instance.getTargetTaskId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCancel_NotOnThisMachine() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.cancel(false);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsDone_NotOnThisMachine() {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.isDone();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetResult_NotOnThisMachine() throws InterruptedException, ExecutionException {
        FlowTaskInstance instance = new FlowTaskInstance();
        instance.getResult();
    }
}