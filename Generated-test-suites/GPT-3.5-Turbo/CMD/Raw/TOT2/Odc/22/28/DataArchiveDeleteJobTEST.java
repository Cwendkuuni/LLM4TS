import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);

        // When
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));

        // When
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_SuccessfulExecution() {
        // Given
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(new ScheduleTaskEntity()));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any())).thenReturn(null);

        // When
        dataArchiveDeleteJob.execute(jobExecutionContext);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), any());
    }

    // More test cases for edge cases and exception handling scenarios can be added

}