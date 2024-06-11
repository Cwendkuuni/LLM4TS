package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.quartz.JobExecutionContext;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {
    PartitionPlanJob partitionPlanJob;
    JobExecutionContext context;
    
    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        context = mock(JobExecutionContext.class);
    }
    
    // Basic test 
    @Test
    public void testExecute() {
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
    
    // Testing edge case when schedule Id cannot be fetched
    @Test
    public void testExecute_ScheduleIdNotFound() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(null);
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    // Testing edge cases and exceptions when scheduler exception occurs
    @Test
    public void testExecute_SchedulerException() {
        when(scheduleService.terminate(any(ScheduleEntity.class))).thenThrow(SchedulerException.class);
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }

    // Edge cases and exceptions when database change task fail
    @Test
    public void testExecute_DatabaseChangeTaskFailed(){
         doThrow(new RuntimeException()).when(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
         try{
             partitionPlanJob.execute(context);
         } catch (Exception e) {
             assertTrue(e instanceof RuntimeException);
         }
     }
}
