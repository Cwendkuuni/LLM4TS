package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
    PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
    ScheduleService scheduleService = mock(ScheduleService.class);

    PartitionPlanJob partitionPlanJob = new PartitionPlanJob(partitionPlanTaskService, partitionPlanService, scheduleService);

    @Test
    public void testExecute() {
        // Setup
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class)).thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        // Verify
        partitionPlanJob.execute(context);

        // Assert
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(1L);
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(1L);
        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (Exception e) {
            assertEquals(UnExpectedException.class, e.getClass());
        }
    }
}