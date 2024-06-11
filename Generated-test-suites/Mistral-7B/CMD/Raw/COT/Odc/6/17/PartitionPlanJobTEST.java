package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import org.junit.Before;
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

import java.util.List;
import java.util.UUID;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    @Before
    public void setup() {
        partitionPlanTaskService = SpringContextUtil.getBean(PartitionPlanTaskService.class);
        partitionPlanService = SpringContextUtil.getBean(PartitionPlanService.class);
        scheduleService = SpringContextUtil.getBean(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_ValidScheduleEntity_Success() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());

        scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

        try {
            partitionPlanJob.execute(new JobExecutionContext());
            scheduleEntity.setStatus(ScheduleEntity.Status.RUNNING);
            scheduleService.saveOrUpdate(scheduleEntity);
            partitionPlanService.saveOrUpdate(databasePartitionPlan);
            partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
            assertEquals(ScheduleEntity.Status.COMPLETED, scheduleEntity.getStatus());
        } catch (Exception e) {
            log.warn("Test execute partition plan job failed.", e);
            fail();
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_NullScheduleEntity_ThrowException() {
        partitionPlanJob.execute(new JobExecutionContext());
    }

    @Test
    public void testExecute_DisabledDatabasePartitionPlan_SkipPartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());

        ScheduleEntity scheduleEntityDisable = new ScheduleEntity();
        scheduleEntityDisable.setId(scheduleId);
        scheduleEntityDisable.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(UUID.randomUUID().toString());
        databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

        try {
            partitionPlanJob.execute(new JobExecutionContext());
            scheduleEntityDisable.setStatus(ScheduleEntity.Status.RUNNING);
            scheduleService.saveOrUpdate(scheduleEntityDisable);
            partitionPlanService.saveOrUpdate(databasePartitionPlan);
            assertFalse(tablePartitionPlans.isEmpty());
            partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
            assertEquals(ScheduleEntity.Status.SKIPPED, scheduleEntityDisable.getStatus());
        } catch (Exception e) {
            log.warn("Test execute partition plan job failed.", e);
            fail();
        }
    }

    @Test
    public void testExecute_EmptyTablePartitionPlans_SkipPartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(UUID.randomUUID().toString());

        ScheduleEntity scheduleEntityEmpty = new ScheduleEntity();
        scheduleEntityEmpty.setId(scheduleId);
        scheduleEntityEmpty.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(UUID.randomUUID().toString());
        databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of();

        try {
            partitionPlanJob.execute(new JobExecutionContext());
            scheduleEntityEmpty.setStatus(ScheduleEntity.Status.RUNNING);
            scheduleService.saveOrUpdate(scheduleEntityEmpty);
            partitionPlanService.saveOrUpdate(databasePartitionPlan);
            assertFalse(tablePartitionPlans.isEmpty());
            partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
            assertEquals(ScheduleEntity.Status.SKIPPED, scheduleEntityEmpty.getStatus());
        } catch (Exception e) {
            log.warn("Test execute partition plan job failed.", e);
            fail();
        }
    }

    @Test(expected = SchedulerException.class)
    public void testInterrupt_ThrowSchedulerException() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testInterrupt_DoNothing() {
        partitionPlanJob.interrupt();
    }
}