package com.oceanbase.odc.service.onlineschemachange;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeFlowableTaskTest {
    private OnlineSchemaChangeFlowableTask task;

    @Before
    public void setUp() {
        task = new OnlineSchemaChangeFlowableTask();
    }

    @Test
    public void test_successful() {
        task.status = TaskStatus.DONE;
        assertTrue(task.isSuccessful());
    }

    @Test
    public void test_failure() {
        task.status = TaskStatus.FAILED;
        assertTrue(task.isFailure());
    }

    @Test(expected=NullPointerException.class)
    public void test_nullTaskService_onFailure() {
        task.onFailure(123L, null);
    }

    @Test
    public void test_nullTaskService_onProgressUpdate() {
        // Ensure no exception is thrown
        try {
            task.onProgressUpdate(123L, null);
        }catch (Throwable ex) {
            fail();
        }
    }

    @Test
    public void test_nullTaskService_OnSuccessful() {
        task.status = TaskStatus.FAILED;
        try {
            task.onSuccessful(123L, null);
            assertEquals(TaskStatus.EXECUTION_SUCCEEDED, task.status);
        }catch (Exception ex){
            fail();
        }
    }

    @Test(expected=NullPointerException.class)
    public void test_nullTaskService_onTimeout() {
        task.onTimeout(123L, null);
    }

    @Test(expected=NullPointerException.class)
    public void test_cancel_nullTaskId() {
        task.cancel(true, null, null);
    }

    @Test
    public void test_cancel() {
        assertFalse(task.cancel(false, 123L, null));
    }

    @Test
    public void test_isCancelled_notSetStatus() {
        assertFalse(task.isCancelled());
    }
}