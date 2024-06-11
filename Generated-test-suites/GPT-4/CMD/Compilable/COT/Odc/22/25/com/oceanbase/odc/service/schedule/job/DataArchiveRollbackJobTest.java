package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.quartz.JobExecutionContext;

public class DataArchiveRollbackJobTest {

    @Test
    public void testExecute() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        // You have to write further code to set up any needed stubs/mocks for the classes used inside the method.
        job.execute(context);
    }
}