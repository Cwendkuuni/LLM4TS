import static org.junit.Assert.*;
import org.junit.Test;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        // Typical Use Case
        // Create JobExecutionContext
        JobExecutionContext context = new JobExecutionContext();

        // Create ScheduleTaskEntity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{}");
        
        // Mock scheduleTaskRepository
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // Mock JsonUtils
        when(JsonUtils.fromJson(anyString(), any(Class.class))).thenReturn(new DataArchiveRollbackParameters());

        // Create DataArchiveRollbackJob instance
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.setScheduleTaskRepository(scheduleTaskRepository);

        // Execute the method
        job.execute(context);

        // Assert that the task status is updated
        assertEquals(TaskStatus.DONE, taskEntity.getStatus());

        // Edge Cases
        // If dataArchiveTask is not present
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        job.execute(context);
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }
}