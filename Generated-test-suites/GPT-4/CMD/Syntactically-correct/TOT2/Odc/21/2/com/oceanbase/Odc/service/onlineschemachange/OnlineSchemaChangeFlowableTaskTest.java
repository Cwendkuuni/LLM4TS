package com.oceanbase.odc.service.onlineschemachange;
//    - import statements -
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeFlowableTaskTest {

    @Test
    public void testIsSuccessful() {
         OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
         task.status = TaskStatus.DONE;
         assertTrue(task.isSuccessful());
    }

    @Test(expected = NullPointerException.class)
    public void testIsSuccessfulNullTask() {
         OnlineSchemaChangeFlowableTask task = null;
         assertFalse(task.isSuccessful());
    }

    @Test
    public void testIsSuccessfulNotDone() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.RUNNING;
        assertFalse(task.isSuccessful());
    }

    // Additional tests for other methods and edge cases...
}