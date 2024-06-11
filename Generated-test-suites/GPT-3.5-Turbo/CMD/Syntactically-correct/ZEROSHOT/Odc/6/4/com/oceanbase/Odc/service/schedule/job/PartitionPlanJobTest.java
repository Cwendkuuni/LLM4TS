package com.oceanbase.odc.service.schedule.job;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import java.util.Collections;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        long scheduleId = 1L;

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Collections.singletonList(tablePartitionPlan));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            // Expected exception
            return;
        }
        fail("Expected UnExpectedException was not thrown");
    }
}