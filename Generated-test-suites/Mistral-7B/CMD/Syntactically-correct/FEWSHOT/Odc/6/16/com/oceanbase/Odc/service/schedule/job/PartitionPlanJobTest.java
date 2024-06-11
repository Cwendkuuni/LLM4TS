package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

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

    private PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
    private PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
    private ScheduleService scheduleService = mock(ScheduleService.class);
    private SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
    private PartitionPlanJob partitionPlanJob;

    @Test
    public void testPartitionPlanJobConstruction() {
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        partitionPlanJob = new PartitionPlanJob();

        assertSame(partitionPlanService, partitionPlanJob.partitionPlanService);
        assertSame(partitionPlanTaskService, partitionPlanJob.partitionPlanTaskService);
        assertSame(scheduleService, partitionPlanJob.scheduleService);
    }

    @Test
    public void testExecuteWithValidData() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));

        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        verifyExecutedWithValidData(databasePartitionPlan, tablePartitionPlans);
    }

    @Test
    public void testExecuteWithDisabledDatabasePartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        log.info("Database partition plan is disabled, stopping partition-plan job.");
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecuteWithEmptyTablePartitionPlans() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleEntity.getId()).thenReturn(scheduleId);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        log.info("No tables need to process, stopping partition-plan job.");
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.interrupt();

        fail("Interrupt method should not be called.");
    }

    private void verifyExecutedWithValidData(DatabasePartitionPlanEntity databasePartitionPlan,
            List<TablePartitionPlanEntity> tablePartitionPlans) {
        assertTrue(databasePartitionPlan.isConfigEnabled());
        assertTrue(tablePartitionPlans.size() > 0);

        partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }
}