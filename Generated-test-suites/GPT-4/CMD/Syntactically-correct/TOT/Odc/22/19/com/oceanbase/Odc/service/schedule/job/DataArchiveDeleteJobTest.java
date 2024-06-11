package com.oceanbase.odc.service.schedule.job;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import java.util.Collections;
import java.util.Optional;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

public class DataArchiveDeleteJobTest {

    JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
    DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
    ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();

    @Test
    public void testExecuteDataArchiveTaskNotFound() {
        taskEntity.setResult(null);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        dataArchiveDeleteJob.execute(context);
        Assert.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteDataArchiveTaskNotDone() {
        Optional<ScheduleTaskEntity> optionalScheduleTask = Optional.of(new ScheduleTaskEntity());
        taskEntity.setResult(optionalScheduleTask);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        dataArchiveDeleteJob.execute(context);
        Assert.assertEquals(TaskStatus.FAILED, taskEntity.getStatus());
    }

    @Test
    public void testExecuteDataArchiveTaskDone() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        Optional<ScheduleTaskEntity> optionalScheduleTask = Optional.of(dataArchiveTask);
        taskEntity.setResult(optionalScheduleTask);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        dataArchiveDeleteJob.execute(context);
        // Assert successful status
    }

    @Test
    public void testExecuteDataArchiveTaskListEmpty() {
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setStatus(TaskStatus.DONE);
        dataArchiveTask.setResultJson("[]");
        Optional<ScheduleTaskEntity> optionalScheduleTask = Optional.of(dataArchiveTask);
        taskEntity.setResult(optionalScheduleTask);
        Mockito.when(context.getResult()).thenReturn(taskEntity);
        dataArchiveDeleteJob.execute(context);
        // Assert the status
    }
}

