package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.mockito.Mock;
import org.quartz.JobExecutionContext;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DataArchiveDeleteJobTest {

    @Mock
    private JobExecutionContext context;

    @Test
    public void testExecute() {
        DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

        // Test data
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        DataArchiveClearParameters dataArchiveClearParameters = new DataArchiveClearParameters();
        dataArchiveClearParameters.setDataArchiveTaskId(1L);
        taskEntity.setParametersJson(JsonUtils.toJson(dataArchiveClearParameters));

        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        dataArchiveTask.setStatus(TaskStatus.DONE);

        when(context.getResult()).thenReturn(taskEntity);
        when(scheduleTaskRepository.findById(dataArchiveClearParameters.getDataArchiveTaskId())).thenReturn(Optional.of(dataArchiveTask));

        dataArchiveDeleteJob.execute(context);

        // Verify
        assertEquals(TaskStatus.DONE, dataArchiveTask.getStatus());
    }
}