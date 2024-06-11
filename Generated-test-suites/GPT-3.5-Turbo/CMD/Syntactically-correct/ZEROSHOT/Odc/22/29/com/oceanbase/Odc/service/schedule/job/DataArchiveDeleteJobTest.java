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
    public void testExecute_dataArchiveTaskNotFound() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        
        when(context.getResult()).thenReturn(taskEntity);
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class))).thenReturn(dataArchiveClearParameters);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Act
        dataArchiveDeleteJob.execute(context);
        
        // Assert
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_dataArchiveTaskNotFinished() {
        // Arrange
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);
        
        when(context.getResult()).thenReturn(taskEntity);
        when(JsonUtils.fromJson(anyString(), eq(DataArchiveClearParameters.class))).thenReturn(dataArchiveClearParameters);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        
        // Act
        dataArchiveDeleteJob.execute(context);
        
        // Assert
        verify(scheduleTaskRepository, times(1)).updateStatusById(anyLong(), eq(TaskStatus.FAILED));
    }

    // Add more test methods for other scenarios

}