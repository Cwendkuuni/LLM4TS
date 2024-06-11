package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveDeleteJobTest {

    private DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();

    @Test
    public void testExecute_DataArchiveTaskNotFound() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);

        // Verify task status is updated to FAILED
        // Implement the verification based on the logic in the execute method
    }

    @Test
    public void testExecute_DataArchiveTaskNotDone() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);

        // Verify task status is updated to FAILED
        // Implement the verification based on the logic in the execute method
    }

    @Test
    public void testExecute_Success() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);

        // Verify task status is updated to the expected status
        // Implement the verification based on the logic in the execute method
    }
}