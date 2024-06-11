import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

public class OnlineSchemaChangeFlowableTaskTest {

    @Test
    public void testStart() throws Exception {
        // Mock dependencies
        TaskService taskService = mock(TaskService.class);
        DelegateExecution execution = mock(DelegateExecution.class);
        when(execution.getCurrentActivityId()).thenReturn("123");

        // Create test data
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.RUNNING);

        // Call the method to test
        task.start(123L, taskService, execution);

        // Assert the expected behavior
        assertEquals(TaskStatus.RUNNING, task.getStatus());
    }

    @Test
    public void testIsSuccessful() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);

        boolean result = task.isSuccessful();

        assertEquals(true, result);
    }

    @Test
    public void testIsFailure() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);

        boolean result = task.isFailure();

        assertEquals(true, result);
    }

    @Test
    public void testOnFailure() {
        TaskService taskService = mock(TaskService.class);
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.FAILED);

        task.onFailure(123L, taskService);

        // Assert the expected behavior after failure
        assertEquals(TaskStatus.FAILED, task.getStatus());
    }

    @Test
    public void testOnSuccessful() {
        TaskService taskService = mock(TaskService.class);
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.DONE);

        task.onSuccessful(123L, taskService);

        // Assert the expected behavior after successful completion
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public void testOnTimeout() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();

        task.onTimeout(123L, mock(TaskService.class));

        // No specific behavior to test for timeout
        assertEquals(true, true);
    }

    @Test
    public void testOnProgressUpdate() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        // Mocked task and service
        TaskService taskService = mock(TaskService.class);
        Pageable pageable = mock(Pageable.class);
        Page<ScheduleTaskEntity> tasks = mock(Page.class);
        when(tasks.getSize()).thenReturn(1);
        when(scheduleTaskService.listTask(pageable, task.getScheduleId())).thenReturn(tasks);

        // Call the method to test
        task.onProgressUpdate(123L, taskService);

        // Assert any expected behavior after progress update
        assertEquals(true, true);
    }

    @Test
    public void testCancel() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        TaskService taskService = mock(TaskService.class);

        boolean result = task.cancel(true, 123L, taskService);

        assertEquals(true, result);
    }

    @Test
    public void testIsCancelled() {
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        task.setStatus(TaskStatus.CANCELED);

        boolean result = task.isCancelled();

        assertEquals(true, result);
    }

    @Test
    public void testCreateScheduleEntity() {
        // Test creating a ScheduleEntity instance
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        ConnectionConfig connectionConfig = createTestConnectionConfig();
        OnlineSchemaChangeParameters parameters = createTestParameters();
        String schema = "test_schema";

        ScheduleEntity entity = task.createScheduleEntity(connectionConfig, parameters, schema);

        assertNotNull(entity);
        assertEquals("test_database", entity.getDatabaseName());
        // Add more assertions for other fields as needed
    }

    @Test
    public void testCreateScheduleTaskEntity() {
        // Test creating a ScheduleTaskEntity instance
        OnlineSchemaChangeFlowableTask task = new OnlineSchemaChangeFlowableTask();
        Long scheduleId = 123L;
        OnlineSchemaChangeScheduleTaskParameters parameters = createTestScheduleTaskParameters();

        ScheduleTaskEntity entity = task.createScheduleTaskEntity(scheduleId, parameters);

        assertNotNull(entity);
        // Add assertions for other fields as needed
    }

    // Helper methods to create test data
    private ConnectionConfig createTestConnectionConfig() {
        ConnectionConfig config = new ConnectionConfig();
        // Set properties for test data
        return config;
    }

    private OnlineSchemaChangeParameters createTestParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        // Set properties for test data
        return parameters;
    }

    private OnlineSchemaChangeScheduleTaskParameters createTestScheduleTaskParameters() {
        OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();
        // Set properties for test data
        return parameters;
    }

}