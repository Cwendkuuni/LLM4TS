import org.junit.Test;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import org.junit.Assert;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute_withExistingDataArchiveTask() {
        // Mocking dependencies
        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity mockTaskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters mockParameters = mock(DataArchiveClearParameters.class);

        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(mockParameters);
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.of(mockTaskEntity));
        when(mockTaskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(mockContext);

        // Verify that the status is updated to DONE
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.DONE));
    }

    @Test
    public void testExecute_withNonExistingDataArchiveTask() {
        // Mocking dependencies
        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity mockTaskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters mockParameters = mock(DataArchiveClearParameters.class);

        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(mockParameters);
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.empty());

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(mockContext);

        // Verify that the status is updated to FAILED
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_withNonFinishedDataArchiveTask() {
        // Mocking dependencies
        JobExecutionContext mockContext = mock(JobExecutionContext.class);
        ScheduleTaskEntity mockTaskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters mockParameters = mock(DataArchiveClearParameters.class);
        ScheduleTaskEntity mockDataArchiveTask = mock(ScheduleTaskEntity.class);

        when(mockContext.getResult()).thenReturn(mockTaskEntity);
        when(JsonUtils.fromJson(anyString(), any())).thenReturn(mockParameters);
        when(scheduleTaskRepository.findById(anyInt())).thenReturn(Optional.of(mockDataArchiveTask));
        when(mockDataArchiveTask.getStatus()).thenReturn(TaskStatus.IN_PROGRESS);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        dataArchiveDeleteJob.execute(mockContext);

        // Verify that the status is updated to FAILED
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyInt(), eq(TaskStatus.FAILED));
    }
}