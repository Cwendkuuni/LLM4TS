package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {
    
    @Test
    public void testPartitionPlanJobConstructor() {
        PartitionPlanJob job = new PartitionPlanJob();
        assertNotNull(job);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("scheduleId", 1L);
        when(context.getMergedJobDataMap()).thenReturn(jobDataMap);
        PartitionPlanJob job = new PartitionPlanJob();
        job.execute(context);
        // assert the results here
    }

    @Test(expected = UnExpectedException.class)
    public void interrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        job.interrupt();
    }
}