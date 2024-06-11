package com.oceanbase.odc.service.schedule.job;

import org.quartz.JobExecutionContext;
import org.junit.Test;
import com.oceanbase.odc.service.schedule.job.DataArchiveDeleteJob;

public class DataArchiveDeleteJobTest {
    @Test
    public void executeTest() {
        DataArchiveDeleteJob job = new DataArchiveDeleteJob();
        JobExecutionContext context = null;
        try {
            job.execute(context);
            fail("Should have thrown an exception as context is null");
        } catch (NullPointerException e) {
            // Success, this is expected as context is null
        }
    }

    // More tests can go here such as edge case testing, exception testing etc.
}
