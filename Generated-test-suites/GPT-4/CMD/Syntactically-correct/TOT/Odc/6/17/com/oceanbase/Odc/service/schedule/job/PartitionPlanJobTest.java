package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PartitionPlanJobTest {

    private PartitionPlanJob job;
    private JobExecutionContext context;

    @Before
    public void setUp() {

        job = new PartitionPlanJob();

        context = Mockito.mock(JobExecutionContext.class);

    }

    @Test(expected = UnExpectedException.class)
    public void testInterruptShouldThrowException() {
        job.interrupt();
    }

    @Test
    public void testExecuteShouldCompleteSuccessfully() {
        try {
            job.execute(context);
        } catch(JobExecutionException jee) {
            fail("Job execution should not fail.");
        }
    }

    @After
    public void tearDown() {
        // Cleanup
    }
}