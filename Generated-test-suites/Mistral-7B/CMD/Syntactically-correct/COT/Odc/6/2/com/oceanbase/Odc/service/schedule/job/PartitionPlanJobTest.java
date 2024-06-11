package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.google.common.base.Optional;
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
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;

    @Before
    public void init() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_scheduleEntityNull() {
        when(scheduleService.nullSafeGetById(1L)).thenThrow(new RuntimeException());
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        // Schedule not found, so no need to check the further logic
    }

    @Test
    public void testExecute_databasePartitionPlanNull() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        // Database partition plan is disable, so no need to check the further logic
    }

    @Test
    public void testExecute_tablePartitionPlansEmpty() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(new ArrayList<>());
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        // No table need to process, so the partition-plan job should stop
    }

    @Test
    public void testExecute_databasePartitionPlanEnableAndValidTablePartitionPlans() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        // Database partition plan is enable and there is a valid table to process,
        // so the partition-plan job should execute the partitionPlanTaskService.executePartitionPlan() method
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_partitionPlanTaskServiceFail() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskService.executePartitionPlan(anyLong(), any(List.class)))
                .thenThrow(new RuntimeException());
        partitionPlanJob.execute(mock(JobExecutionContext.class));
        // Create partition-plan database change task failed
    }

    @Test
    public void testInterrupt() {
        partitionPlanJob.interrupt();
        // Interrupt method should throw UnExpectedException
    }
}