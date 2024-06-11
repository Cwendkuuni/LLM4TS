package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    @InjectMocks
    PartitionPlanJob partitionPlanJob;

    @Test
    public void testExecute() {
        // Mock data
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = mock(List.class);

        // Define mock behavior
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(tablePartitionPlans.isEmpty()).thenReturn(false);

        // Execute
        partitionPlanJob.execute(context);

        // Assertion
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testInterrupt() {
        // Execute
        partitionPlanJob.interrupt();

        // Assertion
        // Expect UnExpectedException to be thrown
    }
}