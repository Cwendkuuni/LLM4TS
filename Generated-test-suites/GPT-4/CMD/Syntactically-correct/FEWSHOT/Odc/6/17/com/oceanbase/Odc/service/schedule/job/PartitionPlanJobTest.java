package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {
   
    @Test
    public void testExecute() throws Exception {
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters param = new PartitionPlanJobParameters();
        param.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(param));

        DatabasePartitionPlanEntity databasePlan = new DatabasePartitionPlanEntity();
        databasePlan.setConfigEnabled(true);
        databasePlan.setFlowInstanceId(1L);

        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePlan);

        TablePartitionPlanEntity tablePlan = new TablePartitionPlanEntity();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Arrays.asList(tablePlan));

        PartitionPlanJob job = new PartitionPlanJob();
        job.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();
        job.interrupt();
    }
}