package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import org.quartz.JobExecutionContext;

import static org.junit.Assert.*;

public class PartitionPlanJobTest {

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
	// Assume that we have mocked JobExecutionContext correctly
    @Test
    public void testExecute() {
        JobExecutionContext context = getMockedContext();
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(context);
        
        // This requires assertions to check the desired outcome depending upon the mocked context
    	// TO DO: impl assertions
    }
}