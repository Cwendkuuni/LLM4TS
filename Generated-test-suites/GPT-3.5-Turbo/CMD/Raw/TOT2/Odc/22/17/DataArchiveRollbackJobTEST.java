import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        // Typical use case
        // Mock the taskEntity and rollbackParameters
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setId(1L);
        taskEntity.setParametersJson("{\"dataArchiveTaskId\": 2}");
        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(dataArchiveRollbackJob.getScheduleTaskRepository()).thenReturn(scheduleTaskRepository);

        // Mock the dataArchiveTask
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setResultJson("[{\"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"status\": \"PREPARING\"}]");
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Edge cases
        // Test when dataArchiveTask is not present
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Test when taskUnits is empty
        dataArchiveTask.setResultJson("[]");
        dataArchiveRollbackJob.execute(jobExecutionContext);

        // Error scenarios
        // Test when taskUnit status is not preparing
        dataArchiveTask.setResultJson("[{\"sourceDatabaseId\": 1, \"targetDatabaseId\": 2, \"status\": \"DONE\"}]");
        dataArchiveRollbackJob.execute(jobExecutionContext);
    }
}