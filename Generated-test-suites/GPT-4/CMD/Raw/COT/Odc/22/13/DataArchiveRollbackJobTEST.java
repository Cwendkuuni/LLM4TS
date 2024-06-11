import static org.mockito.Mockito.*;
import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(context);
        // Need to verify if any expected behaviour is performed in this method
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullTaskEntity() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(null);
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(context);
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        // Assuming the ScheduleTaskRepository field in DataArchiveRollbackJob
        // is named "scheduleTaskRepository"
        when(job.scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        job.execute(context);
        // Check if failed status is set
        verify(job.scheduleTaskRepository).updateStatusById(any(), eq(TaskStatus.FAILED));
    }
}