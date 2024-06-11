package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Test;
import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {
    @Test
    public void executeTest() {
        JobExecutionContext context = null; 
        PartitionPlanJob planJob = new PartitionPlanJob();
        try {
            planJob.execute(context);
        } catch (Exception e) {
            //Test that an exception is thrown when context is null
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test(expected=UnExpectedException.class)
    public void interruptTest() {
        PartitionPlanJob planJob = new PartitionPlanJob();
        planJob.interrupt();
    }

}