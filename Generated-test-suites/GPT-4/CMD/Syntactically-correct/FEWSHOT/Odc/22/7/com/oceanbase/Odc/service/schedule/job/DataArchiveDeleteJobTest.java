package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {
    
    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        // Mock ScheduleTaskRepository
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);

        // Mocking the behaviour for JsonUtils#fromJson
        List<DlmTask> dlmTasks = Arrays.asList(new DlmTask());
        when(JsonUtils.fromJson(anyString(), any(TypeReference.class))).thenReturn(dlmTasks);

        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1))
                .updateStatusById(anyString(), eq(TaskStatus.PREPARING));
    }

    @Test
    public void testExecuteNonExistentTask() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("nonexistent");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        // Mock ScheduleTaskRepository
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1))
                .updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecuteTaskNotDone() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);

        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId("taskId");
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));

        // Mock ScheduleTaskRepository
        when(dataArchiveDeleteJob.scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.RUNNING);
        
        dataArchiveDeleteJob.execute(context);

        verify(dataArchiveDeleteJob.scheduleTaskRepository, times(1))
                .updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }
}