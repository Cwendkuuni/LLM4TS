import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.AbstractDlmJob;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob.DataArchiveClearParameters;
import com.oceanbase.odc.service.schedule.model.ScheduleTaskEntity;
import com.oceanbase.tools.migrator.common.enums.JobType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DataArchiveDeleteJob.
 */
@Slf4j
class DataArchiveDeleteJobTest {

    @InjectMocks
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    void testExecute_DataArchiveTaskNotFound() {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(JobExecutionException.class, () ->
                dataArchiveDeleteJob.execute(context));
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(),
                TaskStatus.FAILED);
    }

    @Test
    void testExecute_DataArchiveTaskAlreadyDone() {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(),
                TaskStatus.FAILED);
    }

    @Test
    void testExecute_PrepareTasksForClear() {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("dataArchiveTaskId");
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        List<DlmTask> taskUnits = List.of();
        when(JsonUtils.fromJson(any(String.class), eq(new TypeReference<List<DlmTask>>() {}))).thenReturn(taskUnits);

        // When
        dataArchiveDeleteJob.execute(context);

        // Then
        Mockito.verify(taskUnits, Mockito.times(1)).forEach(taskUnit -> {
            taskUnit.setId(DlmJobIdUtil.generateHistoryJobId(taskEntity.getJobName(), taskEntity.getJobGroup(),
                    taskEntity.getId(),
                    taskUnits.size()));
            taskUnit.setJobType(JobType.DELETE);
            taskUnit.setStatus(TaskStatus.PREPARING);
        });
        Mockito.verify(dataArchiveDeleteJob, Mockito.times(1)).executeTask(taskEntity.getId(), taskUnits);

        TaskStatus taskStatus = getTaskStatus(taskUnits);
        Mockito.verify(scheduleTaskRepository, Mockito.times(1)).updateStatusById(taskEntity.getId(), taskStatus);
    }

    private TaskStatus getTaskStatus(List<DlmTask> taskUnits) {
        boolean isAllTaskExecuted = taskUnits.stream().allMatch(DlmTask::isExecuted);
        return isAllTaskExecuted ? TaskStatus.SUCCESS : TaskStatus.FAILED;
    }
}