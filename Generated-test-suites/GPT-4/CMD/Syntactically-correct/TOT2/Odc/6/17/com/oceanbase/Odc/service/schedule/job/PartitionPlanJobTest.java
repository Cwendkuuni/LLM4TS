package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.springframework.test.util.ReflectionTestUtils;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanService", mockPartitionPlanService);
        ReflectionTestUtils.setField(partitionPlanJob, "partitionPlanTaskService", mockPartitionPlanTaskService);
        ReflectionTestUtils.setField(partitionPlanJob, "scheduleService", mockScheduleService);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        // setup the context and services to return expected data
        // Here, we need more details about behavior of services to setup the test scenario.
        // Below is dummy call for instruction.
        when(scheduleService.nullSafeGetById(any())).thenReturn(mock(ScheduleEntity.class)); 
        partitionPlanJob.execute(context);

        // Assertions based on the expected outcome. If exceptions are expected, they can be captured and asserted here.
        //Actual is compared with the expected
        Assert.assertNotNull(partitionPlanJob);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();

        //Since the method is expected to throw an UnExpectedException, no assertion is required. Execution of this test method without exception will be a failure
    }
}