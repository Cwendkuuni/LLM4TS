package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        // Given
        // Mock the dependencies
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        
        // Mock the method calls
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(tablePartitionPlans.isEmpty()).thenReturn(false);

        // Execute the method
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(context);

        // Verify interactions
        verify(scheduleService).terminate(scheduleEntity);
        verify(partitionPlanTaskService).executePartitionPlan(eq(databasePartitionPlan.getFlowInstanceId()), eq(tablePartitionPlans));
    }

    @Test
    public void testInterrupt() {
        // Given
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // Execute the method
        try {
            partitionPlanJob.interrupt();
            fail("Expected an UnExpectedException to be thrown");
        } catch (UnExpectedException e) {
            // Expected exception
        }
    }
}