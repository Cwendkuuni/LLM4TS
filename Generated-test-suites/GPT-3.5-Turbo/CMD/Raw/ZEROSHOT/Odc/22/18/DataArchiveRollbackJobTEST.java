package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.mockito.Mockito.*;

public class DataArchiveRollbackJobTest {

    private DataArchiveRollbackJob dataArchiveRollbackJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        dataArchiveRollbackJob = new DataArchiveRollbackJob();
        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        dataArchiveRollbackJob.execute(jobExecutionContext);
        // Add assertions here to verify the behavior of the execute method
    }
}