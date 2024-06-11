
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.tools.migrator.common.enums.JobType;

public class DataArchiveDeleteJobTest {

    @Test
    public void testExecute_TaskNotFound() {

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());

        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskNotDone() {

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.IN_PROGRESS);

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));

        job.execute(context);

        verify(scheduleTaskRepository, times(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.FAILED));
    }

    @Test
    public void testExecute_TaskDone() throws JobExecutionException {

        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);

        List<DlmTask> taskUnits = new ArrayList<>();
        DlmTask sampleTask = new DlmTask();
        sampleTask.setStatus(TaskStatus.DONE);
        taskUnits.add(sampleTask);
        dataArchiveTask.setResultJson(JsonUtils.toJson(taskUnits));

        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(JsonUtils.toJson(dataArchiveClearParameters));
        when(taskEntity.getResultJson()).thenReturn(JsonUtils.toJson(taskUnits));
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.of(dataArchiveTask));

        job.execute(context);

        //=========================================================================
        // Tasks status gets updated twice - once during initialization (BEFORE status)
        // and secondly after the tasks execution (AFTER status)
        //=========================================================================
        verify(scheduleTaskRepository, timeout(1)).updateStatusById(eq(taskEntity.getId()), eq(TaskStatus.PREPARING));
        verify(scheduleTaskRepository, timeout(1)).updateStatusById(eq(taskEntity.getId()), any(TaskStatus.class));

        // JobType of processed tasks should be DELETE
        for (DlmTask task : taskUnits) {
            assertEquals(task.getJobType(), JobType.DELETE);
        }
    }
}
