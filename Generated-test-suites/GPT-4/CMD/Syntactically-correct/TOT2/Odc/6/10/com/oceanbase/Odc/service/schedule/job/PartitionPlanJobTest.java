package com.oceanbase.odc.service.schedule.job;
import org.junit.Test;
import static org.junit.Assert.*;
import org.quartz.impl.JobExecutionContextImpl;
import org.junit.Before;
import org.mockito.*;


public class PartitionPlanJobTest {
    @Mock
    private JobExecutionContext context;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
    }

    // testing the execute method
    @Test
    public void testExecuteMethod() {
        try {
            partitionPlanJob.execute(context);
        }
        catch (Exception e) {
            fail("The execute method is failed with an exception: " + e.getMessage());
        }
    }

    // testing the interrupt method
    @Test(expected = UnExpectedException.class)
    public void testInterruptMethod() {
        partitionPlanJob.interrupt();
    }

}
