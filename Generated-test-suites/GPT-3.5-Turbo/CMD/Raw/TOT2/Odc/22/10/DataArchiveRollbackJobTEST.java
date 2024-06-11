import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute() {
        // Typical use case
        // Mock JobExecutionContext
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);

        // Mock taskEntity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(123L);
        String parametersJson = "{\"dataArchiveTaskId\":123}";
        taskEntity.setParametersJson(parametersJson);

        // Mock dataArchiveTask
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(123L);
        dataArchiveTask.setJobName("dataArchiveJob");
        dataArchiveTask.setJobGroup("dataArchiveGroup");
        dataArchiveTask.setResultJson("[{\"id\":1,\"sourceDatabaseId\":1,\"targetDatabaseId\":2,\"status\":\"DONE\"}]");
        dataArchiveTask.setStatus(TaskStatus.PREPARING);

        // Mock scheduleTaskRepository
        ScheduleTaskRepo scheduleTaskRepository = mock(ScheduleTaskRepo.class);
        when(scheduleTaskRepository.findById(123L)).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.setScheduleTaskRepository(scheduleTaskRepository); // Inject mock repository

        // Execute the method
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Assertions
        // Verify that the task status is updated
        verify(scheduleTaskRepository).updateStatusById(eq(123L), eq(TaskStatus.DONE));

        // Edge cases and exception handling scenarios can be added here

    }
}