package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    private final DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
   
    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);
    }

    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullContext() {
        dataArchiveDeleteJob.execute(null);
    }
   
    @Test(expected = NullPointerException.class)
    public void testExecuteWithNullJson() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn(null);
        dataArchiveDeleteJob.execute(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithNullDataArchiveTaskId() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        DataArchiveClearParameters dataArchiveClearParameters = mock(DataArchiveClearParameters.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("valid json");
        when(dataArchiveClearParameters.getDataArchiveTaskId()).thenReturn(null);
        dataArchiveDeleteJob.execute(context);
    }

    @Test
    public void testExecuteWithMissingDataArchiveTask() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity taskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(any())).thenReturn(Optional.empty());
        dataArchiveDeleteJob.execute(context);
    }
}