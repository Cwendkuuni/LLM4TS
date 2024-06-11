package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecute() throws SchedulerException {
        partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        partitionPlanJob.execute(context);
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_Exception() throws SchedulerException {
        partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        doThrow(new SchedulerException("Mock Exception")).when(context).getFireTime();

        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.interrupt();
    }
}