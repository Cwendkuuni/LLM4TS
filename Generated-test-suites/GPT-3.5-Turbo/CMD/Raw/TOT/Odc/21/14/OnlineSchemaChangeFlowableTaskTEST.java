import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.flow.task.BaseODCFlowTaskDelegate;
import com.oceanbase.odc.service.flow.task.model.OnlineSchemaChangeTaskResult;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeFlowableTask;
import com.oceanbase.odc.service.schedule.ScheduleTaskService;
import com.oceanbase.odc.service.task.TaskService;

@RunWith(SpringJUnit4ClassRunner.class)
public class OnlineSchemaChangeFlowableTaskTest {

    private OnlineSchemaChangeFlowableTask onlineSchemaChangeFlowableTask;
    private ScheduleEntity scheduleEntity;
    private TaskService taskService;
    private ScheduleTaskService scheduleTaskService;

    @Before
    public void setup() {
        onlineSchemaChangeFlowableTask = new OnlineSchemaChangeFlowableTask();
        scheduleEntity = mock(ScheduleEntity.class);
        taskService = mock(TaskService.class);
        scheduleTaskService = mock(ScheduleTaskService.class);
        onlineSchemaChangeFlowableTask.setScheduleId(1234);
    }

    @Test
    public void testStart() throws Exception {
        // Assert
        assertNotNull(onlineSchemaChangeFlowableTask.start(1L, taskService, null));
    }

    @Test
    public void testIsSuccessful() {
        // Arrange
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.DONE);
        
        // Act
        boolean isSuccessful = onlineSchemaChangeFlowableTask.isSuccessful();
        
        // Assert
        assertTrue(isSuccessful);
    }

    @Test
    public void testIsFailure() {
        // Arrange
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.FAILED);
        
        // Act
        boolean isFailure = onlineSchemaChangeFlowableTask.isFailure();
        
        // Assert
        assertTrue(isFailure);
    }

    @Test
    public void testOnFailure() {
        // Assert
        onlineSchemaChangeFlowableTask.onFailure(1L, taskService);
    }

    @Test
    public void testOnSuccessful() {
        // Assert
        onlineSchemaChangeFlowableTask.onSuccessful(1L, taskService);
    }

    @Test
    public void testOnTimeout() {
        // Assert
        onlineSchemaChangeFlowableTask.onTimeout(1L, taskService);
    }

    @Test
    public void testOnProgressUpdate() {
        // Assert
        onlineSchemaChangeFlowableTask.onProgressUpdate(1L, taskService);
    }

    @Test
    public void testCancel() {
        // Assert
        assertTrue(onlineSchemaChangeFlowableTask.cancel(true, 1L, taskService));
    }

    @Test
    public void testIsCancelled() {
        // Arrange
        onlineSchemaChangeFlowableTask.setStatus(TaskStatus.CANCELED);
        
        // Act
        boolean isCancelled = onlineSchemaChangeFlowableTask.isCancelled();
        
        // Assert
        assertTrue(isCancelled);
    }    

}