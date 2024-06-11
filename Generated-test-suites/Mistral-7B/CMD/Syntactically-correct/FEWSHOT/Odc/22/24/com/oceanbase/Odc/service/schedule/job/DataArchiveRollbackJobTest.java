package com.oceanbase.odc.service.schedule.job;

import static com.oceanbase.odc.service.schedule.job.DataArchiveRollbackJob.JobType.ROLLBACK;
import static com.oceanbase.odc.service.schedule.model.TaskStatus.FAILED;
import static com.oceanbase.odc.service.schedule.model.TaskStatus.PREPARING;
import static com.oceanbase.odc.service.schedule.model.TaskStatus.DONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.odc.service.schedule.repository.ScheduleTaskRepository;

@Slf4j
public class DataArchiveRollbackJobTest {

    @Mock
    private ScheduleTaskRepository scheduleTaskRepository;

    private DataArchiveRollbackJob dataArchiveRollbackJob;

    @Before
    public void setup() {
        initMocks(this);
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
    }

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        // Given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long dataArchiveTaskId = 1L;

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(new ScheduleTaskEntity());

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTaskId), eq(FAILED));
    }

    @Test
    public void testExecute_DataArchiveTaskFound() {
        // Given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long dataArchiveTaskId = 1L;

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(dataArchiveTaskId);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(dataArchiveTask);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        List<DlmTask> dlmTasks = Collections.singletonList(new DlmTask());

        when(scheduleTaskRepository.findById(eq(dataArchiveTaskId))).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenAnswer(invocation -> {
            TaskStatus status = invocation.getArgument(1);
            log.warn("Update status: {}", status);
            return status;
        });

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(eq(dataArchiveTaskId));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTaskId), eq(DONE));
    }

    @Test
    public void testExecute_DataArchiveTaskFound_RollbackTasksPreparing() {
        // Given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long dataArchiveTaskId = 1L;

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(dataArchiveTaskId);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(dataArchiveTask);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        List<DlmTask> dlmTasks = Collections.singletonList(new DlmTask());
        dlmTasks.get(0).setStatus(PREPARING);

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenAnswer(invocation -> {
            TaskStatus status = invocation.getArgument(1);
            log.warn("Update status: {}", status);
            return status;
        });

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(eq(dataArchiveTaskId));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTaskId), eq(DONE));
        verify(dlmTasks, times(1)).forEach(task -> {
            task.setId(anyLong());
            task.setSourceDatabaseId(task.getTargetDatabaseId());
            task.setTargetDatabaseId(task.getSourceDatabaseId());
            task.setJobType(ROLLBACK);
            task.setStatus(DONE);
        });
        verify(scheduleTaskRepository, times(1)).saveAll(dlmTasks);
    }

    @Test
    public void testExecute_DataArchiveTaskFound_RollbackTasksFailed() {
        // Given
        String jobName = "testJobName";
        String jobGroup = "testJobGroup";
        Long dataArchiveTaskId = 1L;

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(dataArchiveTaskId);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getResult()).thenReturn(dataArchiveTask);

        DataArchiveRollbackParameters rollbackParameters = new DataArchiveRollbackParameters();
        rollbackParameters.setDataArchiveTaskId(dataArchiveTaskId);

        List<DlmTask> dlmTasks = Collections.singletonList(new DlmTask());

        when(scheduleTaskRepository.findById(dataArchiveTaskId)).thenReturn(Optional.of(dataArchiveTask));
        when(scheduleTaskRepository.updateStatusById(anyLong(), any(TaskStatus.class))).thenAnswer(invocation -> {
            TaskStatus status = invocation.getArgument(1);
            log.warn("Update status: {}", status);
            return status;
        });

        // When
        dataArchiveRollbackJob.execute(context);

        // Then
        verify(scheduleTaskRepository, times(1)).findById(eq(dataArchiveTaskId));
        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(dataArchiveTaskId), eq(FAILED));
    }
}