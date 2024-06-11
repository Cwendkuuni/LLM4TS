package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext context;

    @Before
    public void setUp() throws Exception {
        this.partitionPlanJob = new PartitionPlanJob();
        this.context = Mockito.mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        boolean success = true;
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            success = false;
        }
        assertTrue("Execution should be successful", success);
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteReturnsException() {
        JobExecutionContext faultycontext = null;
        partitionPlanJob.execute(faultycontext);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}