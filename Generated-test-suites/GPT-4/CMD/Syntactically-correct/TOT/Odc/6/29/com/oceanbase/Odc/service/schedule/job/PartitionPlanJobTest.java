package com.oceanbase.odc.service.schedule.job;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyLong;
import static org.junit.Assert.assertThrows;

import org.quartz.JobExecutionContext;
import org.junit.Before;
import org.junit.Test;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext context;

    @Before
    public void setUp(){
        // Initialize the class to be tested
        partitionPlanJob = new PartitionPlanJob();

        // Mock the JobExecutionContext for testing execute method.
        context = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() {
        // If the execute method can run without throwing an exception, 
        // the test will pass, as we cannot further check the internal workings of execute here.
        try {
            partitionPlanJob.execute(context);
        } catch(Exception e) {
            fail("Execution should not throw an exception.");
        }
    }

    @Test
    public void testInterrupt() {
        // The interrupt method should always throw an UnExpectedException.
        assertThrows(UnExpectedException.class, () -> 
        {
            partitionPlanJob.interrupt();
        });
    }
}