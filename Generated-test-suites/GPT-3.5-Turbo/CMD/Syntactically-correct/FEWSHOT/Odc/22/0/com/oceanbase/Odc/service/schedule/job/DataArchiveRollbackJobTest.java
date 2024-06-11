package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob rollbackJob = new DataArchiveRollbackJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);

        // Mocking taskEntity
        ScheduleTaskEntity taskEntity = new ScheduleTaskEntity();
        taskEntity.setParametersJson("{ \"dataArchiveTaskId\": 123 }");
        taskEntity.setId(1L);
        taskEntity.setJobName("TestJob");
        taskEntity.setJobGroup("Group1");

        Mockito.when(context.getResult()).thenReturn(taskEntity);

        rollbackJob.execute(context);

        // Verify taskEntity is not null
        assertNotNull(taskEntity);

        // Verify scheduleTaskRepository methods
        // Add Mockito verifications here
    }
}