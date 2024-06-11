package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {

    @Test
    public void testExecute() {
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
        
        partitionPlanJob.execute(context);
        
        verify(scheduleService, times(1)).nullSafeGetById(1L);
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(any(Long.class));
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(any(Long.class));
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(Long.class), any(List.class));
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}