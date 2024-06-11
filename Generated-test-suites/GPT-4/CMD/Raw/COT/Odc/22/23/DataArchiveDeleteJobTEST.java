package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.core.shared. constant.TaskStatus;
import java.util.Optional;

public class DataArchiveDeleteJobTest {
    private DataArchiveDeleteJob dataArchiveJob;
    private JobExecutionContext context;
    private ScheduleTaskEntity taskEntity;
    private DataArchiveClearParameters parameters;
   
    @Before
    public void setup() {
        context = mock(JobExecutionContext.class);
        taskEntity = mock(ScheduleTaskEntity.class);
        parameters = new DataArchiveClearParameters();
        dataArchiveJob = new DataArchiveDeleteJob();
    }

    @Test
    public void execute_dataArchiveTaskNotExist_statusFailed() {
        parameters.setDataArchiveTaskId("1");
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson())
                .thenReturn(JsonUtils.toJson(parameters));
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        dataArchiveJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void execute_dataArchiveTaskExistButNotDone_statusFailed() {
        parameters.setDataArchiveTaskId("1");
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson())
                .thenReturn(JsonUtils.toJson(parameters));
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.PREPARING);
        dataArchiveJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(anyString(), eq(TaskStatus.FAILED));
    }

    @Test
    public void execute_dataArchiveTaskDone_executeTask() {
        parameters.setDataArchiveTaskId("1");
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson())
                .thenReturn(JsonUtils.toJson(parameters));
        when(scheduleTaskRepository.findById(anyString()))
                .thenReturn(Optional.of(taskEntity));
        when(taskEntity.getStatus()).thenReturn(TaskStatus.DONE);
        dataArchiveJob.execute(context);
        verify(scheduleTaskRepository).updateStatusById(anyString(), any());
    }
}