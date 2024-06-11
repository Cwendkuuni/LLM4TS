package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.*;

public class PartitionPlanJobTest {

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // Create new instance of PartitionPlanJob
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // This method should throw an UnExpectedException
        partitionPlanJob.interrupt();
    }

}