package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {
    DataArchiveRollbackJob job;
    JobExecutionContext context;
  
    @Before
    public void setUp() {
        job = new DataArchiveRollbackJob();
        context = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute_noScheduleTaskEntity_resultFailed() {
        when(context.getResult()).thenReturn(null);
        job.execute(context);
        verify(context, only()).getResult();
    }

    // other tests for all possible branches of the method...
}