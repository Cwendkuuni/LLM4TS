import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static org.junit.Assert.*;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Test
    public void testExecuteWithDataArchiveTaskNotFound() {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // Act
        dataArchiveDeleteJob.execute(context);

        // Assert
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteWithDataArchiveTaskNotFinished() {
        // Arrange
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.RUNNING);

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(dataArchiveTask));

        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // Act
        dataArchiveDeleteJob.execute(context);

        // Assert
        assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    // Additional test cases for other scenarios can be added here

}