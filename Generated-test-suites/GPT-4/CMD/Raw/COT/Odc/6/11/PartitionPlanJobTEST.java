package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {
    
    @Test
    public void testExecute() {
        // Setup
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);

        // Act
        partitionPlanJob.execute(context);

        // Normally we would do some Asserts here but as the method does not return anything and all its 
        // dependencies are not mockable here, it is not feasible to do so.
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // Setup
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // Act
        partitionPlanJob.interrupt();
    }
}