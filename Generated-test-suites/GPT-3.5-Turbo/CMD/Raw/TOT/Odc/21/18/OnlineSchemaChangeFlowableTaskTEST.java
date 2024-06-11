import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.metadb.task.TaskEntity;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    @InjectMocks
    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;

    @Mock
    private ScheduleTaskService mockScheduleTaskService;

    @Mock
    private TaskService mockTaskService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStart() {
        // Test if start method initializes the task correctly
        Long taskId = 1L;
        DelegateExecution execution = mock(DelegateExecution.class);
        
        try {
            onlineSchemaChangeFlowableTask.start(taskId, mockTaskService, execution);
        } catch (Exception e) {
            fail("Exception thrown while starting task: " + e.getMessage());
        }
        
        // Verify that taskService.start() method was called
        verify(mockTaskService).start(taskId);
    }

    @Test
    public void testIsSuccessful() {
        // Test isSuccessful method returns true when task status is DONE
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        assertTrue(onlineSchemaChangeFlowableTask.isSuccessful());

        // Test isSuccessful method returns false when task status is not DONE
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        assertFalse(onlineSchemaChangeFlowableTask.isSuccessful());
    }

    @Test
    public void testIsFailure() {
        // Test isFailure method returns true when task status is FAILED
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        assertTrue(onlineSchemaChangeFlowableTask.isFailure());

        // Test isFailure method returns false when task status is not FAILED
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.RUNNING);
        assertFalse(onlineSchemaChangeFlowableTask.isFailure());
    }

    @Test
    public void testOnFailure() {
        // Test onFailure method logs a warning message
        Long taskId = 1L;
        onlineSchemaChangeFlowableTask.onFailure(taskId, mockTaskService);

        // Verify that log contains warning message
        verify(log).warn("Online schema change task failed, taskId={}", taskId);
    }

    @Test
    public void testOnSuccessful() {
        // Test onSuccessful method logs an info message
        Long taskId = 1L;
        onlineSchemaChangeFlowableTask.onSuccessful(taskId, mockTaskService);

        // Verify that log contains info message
        verify(log).info("Online schema change task succeed, taskId={}", taskId);
    }

    @Test
    public void testOnTimeout() {
        // Test onTimeout method logs a warning message
        Long taskId = 1L;
        onlineSchemaChangeFlowableTask.onTimeout(taskId, mockTaskService);

        // Verify that log contains warning message
        verify(log).warn("Online schema change task timeout, taskId={}", taskId);
    }

}