
package com.oceanbase.odc.service.onlineschemachange;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeFlowableTaskTest {

    @Test
    public void testStart() throws Exception {
        // Setup test objects and initial state
        Long taskId = 1L;

        // We can use Mockito library to create mock objects
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        User user = new User(777L, 888L);
        when(FlowTaskUtil.getTaskCreator(execution)).thenReturn(user);

        // Create object to test
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        // Call method to test
        task.start(taskId, taskService, execution);

        // Verify the results using assertions.
        // Since successful and failure cases depend on other methods like onProgressUpdate,
        // we can skip them and go straight to status check.
        assertEquals(TaskStatus.RUNNING, task.status);
    }

    @Test
    public void testSuccessful() {
         // Set Task Status to DONE
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.DONE;

        // Test isSuccessful method
        assertTrue(task.isSuccessful());

        // Set Task Status to FAILED and test isSuccessful method
        task.status = TaskStatus.FAILED;
        assertFalse(task.isSuccessful());
    }

    @Test
    public void testFailure() {
        // Set Task Status to FAILED
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.status = TaskStatus.FAILED;

        // Test isFailure method
        assertTrue(task.isFailure());

        // Set Task Status to DONE and test isFailure method
        task.status = TaskStatus.DONE;
        assertFalse(task.isFailure());
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long taskId = 1L;
        TaskService mockService = mock(TaskService.class);

        boolean result = task.cancel(true, taskId, mockService);

        assertTrue(result);
        assertEquals(TaskStatus.CANCELED, task.status);
    }
}
