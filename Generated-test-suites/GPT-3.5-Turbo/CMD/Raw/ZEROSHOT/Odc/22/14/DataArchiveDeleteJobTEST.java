import static org.junit.Assert.*;
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
        // Mocking JobExecutionContext
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Mocking ScheduleTaskEntity
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":1}");
        when(taskEntity.getId()).thenReturn(1);

        // Mocking data archive task
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        when(dataArchiveTask.getId()).thenReturn(1);
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        // Mocking scheduleTaskRepository
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        when(dataArchiveTask.getResultJson()).thenReturn("[{\"id\":1,\"status\":\"DONE\"}]");
        when(dataArchiveTaskOption.isPresent()).thenReturn(true);
        when(scheduleTaskRepository.findById(1)).thenReturn(dataArchiveTask);
        when(scheduleTaskRepository.updateStatusById(1, TaskStatus.PREPARING)).thenReturn(true);

        dataArchiveDeleteJob.execute(context);

        // Verify the status update
        verify(scheduleTaskRepository, times(1)).updateStatusById(1, TaskStatus.PREPARING);
    }

}