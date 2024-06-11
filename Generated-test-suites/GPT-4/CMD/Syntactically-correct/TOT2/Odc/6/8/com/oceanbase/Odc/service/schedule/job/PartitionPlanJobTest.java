package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    JobExecutionContext context;

    @Mock
    ScheduleService scheduleService;

    @Mock
    PartitionPlanService partitionPlanService;

    @Mock
    PartitionPlanTaskService partitionPlanTaskService;

    @Test
    public void testExecute() {

        // given
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // when
        // TODO: add mocking responses to the services 
        // and exceptions for testing different branches in PartitionPlanJob.
        try {
            partitionPlanJob.execute(context);
            // TODO: add assertions and/or verifications
        } catch(Exception e) {
            // TODO: Handle expected exceptions here to assert the functionality
            fail("Expected exception not thrown.");
        }
    }

    @Test
    public void testExecute_Exception() {
        // given
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        
        // when-then
        // TODO: add mocking responses to the services 
        // and exceptions for testing exception scenario in PartitionPlanJob.
        try {
            partitionPlanJob.execute(context);
            fail("Expected exception not thrown.");
        } catch (Exception e) {
            // TODO: Handle expected exceptions here and assert the functionality
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // Given
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // When-Then
        partitionPlanJob.interrupt();
    }
}
