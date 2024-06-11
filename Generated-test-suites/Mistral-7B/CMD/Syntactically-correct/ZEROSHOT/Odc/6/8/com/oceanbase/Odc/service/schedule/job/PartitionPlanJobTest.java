package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.google.common.collect.ImmutableMap;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void init() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId("flow-instance-id");
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setTableName("table_name");
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);
        partitionPlanJob.execute(context);

        // Verify that the database partition plan is enabled and has valid table partition plans.
        partitionPlanJob.execute(context);

        // Verify that the partition plan is executed.
        partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);

        // Verify that the schedule is terminated if the database partition plan is disabled or has no valid table
        // partition plans.
        DatabasePartitionPlanEntity disabledDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        disabledDatabasePartitionPlan.setFlowInstanceId("flow-instance-id");
        disabledDatabasePartitionPlan.setId(1L);
        disabledDatabasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(disabledDatabasePartitionPlan);
        when(scheduleService.terminate(scheduleEntity)).thenReturn(true);
        partitionPlanJob.execute(context);
        assertFalse(scheduleEntity.isEnabled());

        // Verify that the exception is not thrown when the partition plan is executed.
        when(partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenThrow(new UnExpectedException());
        partitionPlanJob.execute(context);
        assertNull(jobParameters.getLastExceptionMessage());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}