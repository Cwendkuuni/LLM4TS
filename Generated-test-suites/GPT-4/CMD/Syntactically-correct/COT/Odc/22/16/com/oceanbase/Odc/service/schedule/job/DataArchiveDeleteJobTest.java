package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob = null;

    @Before
    public void setUp() {
        dataArchiveDeleteJob = new DataArchiveDeleteJob();
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);

        dataArchiveDeleteJob.execute(context);

        verify(context, times(1)).getResult();
    }

    @Test
    public void testDataArchiveTaskNotPresent() {
        JobExecutionContext context = mock(JobExecutionContext.class);

        when(context.getResult()).thenReturn(null);

        dataArchiveDeleteJob.execute(context);

        verify(context, times(1)).getResult();
    }

    @Test
    public void testDataArchiveTaskStatusNotDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleTaskEntity scheduleTaskEntity = mock(ScheduleTaskEntity.class);
        when(context.getResult()).thenReturn(scheduleTaskEntity);
        when(scheduleTaskEntity.getStatus()).thenReturn(TaskStatus.FAILED);

        dataArchiveDeleteJob.execute(context);

        verify(context, times(1)).getResult();
        verify(scheduleTaskEntity, times(1)).getStatus();
    }
}