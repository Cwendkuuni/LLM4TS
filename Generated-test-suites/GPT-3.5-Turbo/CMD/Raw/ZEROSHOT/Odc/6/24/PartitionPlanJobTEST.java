package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        // Setup
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setScheduleService(scheduleService);
        
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        // Test
        partitionPlanJob.execute(context);

        // Verify
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}