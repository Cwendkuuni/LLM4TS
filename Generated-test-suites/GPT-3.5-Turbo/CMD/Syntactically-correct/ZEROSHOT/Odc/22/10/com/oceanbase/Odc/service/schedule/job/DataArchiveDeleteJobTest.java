import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute() {
        // Mock JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Create dummy ScheduleTaskEntity
        ScheduleTaskEntity scheduleTaskEntity = new ScheduleTaskEntity();
        scheduleTaskEntity.setId(1L);
        scheduleTaskEntity.setParametersJson("{ \"dataArchiveTaskId\": 2 }");
        scheduleTaskEntity.setResultJson("{ \"id\": 1, \"name\": \"Task 1\" }");
        scheduleTaskEntity.setStatus(TaskStatus.DONE);
        
        // Mock ScheduleTaskRepository
        ScheduleTaskRepository scheduleTaskRepository = mock(ScheduleTaskRepository.class);
        when(scheduleTaskRepository.findById(2L)).thenReturn(Optional.of(scheduleTaskEntity));

        // Set ScheduleTaskRepository in DataArchiveDeleteJob
        dataArchiveDeleteJob.setScheduleTaskRepository(scheduleTaskRepository);

        // Execute the method
        dataArchiveDeleteJob.execute(context);

        // Verify that the tasks are prepared
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.PREPARING);

        // Verify that the status is updated
        verify(scheduleTaskRepository, times(1)).updateStatusById(1L, TaskStatus.DONE);
    }
}