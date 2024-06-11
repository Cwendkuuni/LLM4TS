
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {

    @Test
    public void execute_NoDataArchiveTask() {
        //given
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        Long dataArchiveTaskId = 123L;
        DataArchiveClearParameters params = new DataArchiveClearParameters();

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntity.getId()).thenReturn(dataArchiveTaskId);
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(Optional.empty());

        //when
        job.execute(context);

        //then
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void execute_DataArchiveTaskNotDone() {
        //given
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        Long dataArchiveTaskId = 123L;
        DataArchiveClearParameters params = new DataArchiveClearParameters();

        //when
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntity.getId()).thenReturn(dataArchiveTaskId);
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.STARTED); // status is not set to DONE

        job.execute(context);

        //then
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), TaskStatus.FAILED);
    }

    @Test
    public void execute_DataArchiveTaskDone() {
        //given
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        ScheduleTaskEntity dataArchiveTask = mock(ScheduleTaskEntity.class);
        Long dataArchiveTaskId = 123L;
        DataArchiveClearParameters params = new DataArchiveClearParameters();

        //when
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(params));
        when(taskEntity.getId()).thenReturn(dataArchiveTaskId);
        when(scheduleTaskRepository.findById(params.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));
        when(dataArchiveTask.getStatus()).thenReturn(TaskStatus.DONE);

        job.execute(context);

        //then
        verify(scheduleTaskRepository).updateStatusById(taskEntity.getId(), taskStatus);
    }
}
