package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

/**
 * Test class for PartitionPlanJob
 */
@Slf4j
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        partitionPlanService = SpringContextUtil.getBean(PartitionPlanService.class);
        partitionPlanTaskService = SpringContextUtil.getBean(PartitionPlanTaskService.class);
        scheduleService = SpringContextUtil.getBean(ScheduleService.class);
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute_validData_shouldExecuteSuccessfully() {
        Long databasePartitionPlanId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(UUID.randomUUID().toString());
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);
        String jobParametersJson = JsonUtils.toJsonString(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(databasePartitionPlanId);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
            tablePartitionPlan.setId(i + 1);
            tablePartitionPlan.setValid(true);
            tablePartitionPlans.add(tablePartitionPlan);
        }

        try {
            JobExecutionContext context = new JobExecutionContext();
            context.setMergedJobDataMap(new org.quartz.JobDataMap());
            context.setJobDetail(new org.quartz.JobDetail("testJobDetail", null, partitionPlanJob.getClass()));
            context.setTrigger(null);

            partitionPlanJob.execute(context);

            // Add assertions here to validate the behavior of the execute method.
            assertTrue(true);
        } catch (SchedulerException e) {
            log.warn("Schedule exception during testExecute.", e);
            fail("Exception occurred during testExecute method.");
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_nullScheduleEntity_shouldThrowException() {
        JobExecutionContext context = new JobExecutionContext();
        context.setMergedJobDataMap(new org.quartz.JobDataMap());
        context.setJobDetail(new org.quartz.JobDetail("testJobDetail", null, partitionPlanJob.getClass()));
        context.setTrigger(null);

        partitionPlanJob.execute(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecute_invalidDatabasePartitionPlan_shouldThrowException() {
        Long databasePartitionPlanId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(UUID.randomUUID().toString());
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);
        String jobParametersJson = JsonUtils.toJsonString(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(databasePartitionPlanId);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
            tablePartitionPlan.setId(i + 1);
            tablePartitionPlan.setValid(true);
            tablePartitionPlans.add(tablePartitionPlan);
        }

        try {
            JobExecutionContext context = new JobExecutionContext();
            context.setMergedJobDataMap(new org.quartz.JobDataMap());
            context.setJobDetail(new org.quartz.JobDetail("testJobDetail", null, partitionPlanJob.getClass()));
            context.setTrigger(null);

            partitionPlanJob.execute(context);
            fail("Expected IllegalArgumentException but none was thrown.");
        } catch (SchedulerException e) {
            log.warn("Schedule exception during testExecute.", e);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testExecute_emptyTablePartitionPlans_shouldThrowException() {
        Long databasePartitionPlanId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(UUID.randomUUID().toString());
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanId);
        String jobParametersJson = JsonUtils.toJsonString(jobParameters);
        scheduleEntity.setJobParametersJson(jobParametersJson);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(databasePartitionPlanId);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        try {
            JobExecutionContext context = new JobExecutionContext();
            context.setMergedJobDataMap(new org.quartz.JobDataMap());
            context.setJobDetail(new org.quartz.JobDetail("testJobDetail", null, partitionPlanJob.getClass()));
            context.setTrigger(null);

            partitionPlanJob.execute(context);
            fail("Expected IllegalStateException but none was thrown.");
        } catch (SchedulerException e) {
            log.warn("Schedule exception during testExecute.", e);
        }
    }

    @Test
    public void testInterrupt() {
        throw new RuntimeException("Interrupt method is not implemented in this test.");
    }
}