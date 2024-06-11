package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanTaskService taskService;
    private PartitionPlanService planService;
    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        taskService = mock(PartitionPlanTaskService.class);
        planService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob(taskService, planService, scheduleService);
    }

    @Test
    public void testExecute() throws SchedulerException {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        doReturn(scheduleEntity).when(scheduleService).nullSafeGetById(scheduleId);

        PartitionPlanJobParameters jobParameters = Mockito.mock(PartitionPlanJobParameters.class);
        doReturn(jobParameters).when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class));

        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
        doReturn(databasePartitionPlan).when(planService).getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId());

        List<TablePartitionPlanEntity> tablePartitionPlanList = Arrays.asList(
                new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
        doReturn(tablePartitionPlanList).when(planService).getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());

        partitionPlanJob.execute(context);

        // check if all services' methods are called
        verify(scheduleService).nullSafeGetById(scheduleId);
        verify(planService).getDatabasePartitionPlanById(anyLong());
        verify(planService).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(taskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlanList);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}