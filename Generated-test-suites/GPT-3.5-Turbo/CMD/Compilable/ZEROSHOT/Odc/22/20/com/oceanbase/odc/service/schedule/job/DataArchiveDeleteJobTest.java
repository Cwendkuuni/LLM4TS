package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataArchiveDeleteJobTest {

    @Autowired
    private DataArchiveDeleteJob dataArchiveDeleteJob;

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        dataArchiveDeleteJob.execute(context);
        
        // Add your assertions here to test the execute method
        // For example:
        // assertEquals(expectedValue, actualValue);
    }
}