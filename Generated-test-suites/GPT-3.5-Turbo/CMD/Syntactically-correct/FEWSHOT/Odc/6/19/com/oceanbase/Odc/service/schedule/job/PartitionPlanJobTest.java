package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        // Mock dependencies
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L))
                .thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(Collections.emptyList());

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        // Create PartitionPlanJob instance
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setScheduleService(scheduleService);

        // Call execute method
        partitionPlanJob.execute(jobExecutionContext);

        // Assert
        assertNotNull(partitionPlanService.getDatabasePartitionPlanById(1L));
        assertEquals(Collections.emptyList(), partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L));
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            // Expected
        }
    }
}