package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;

    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanService = SpringContextUtil.getBean(PartitionPlanService.class);
        partitionPlanTaskService = SpringContextUtil.getBean(PartitionPlanTaskService.class);
        scheduleService = SpringContextUtil.getBean(ScheduleService.class);
    }

    @Test
    public void testExecute() {
        Long scheduleId = UUID.randomUUID().longValue();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlan.setDatabasePartitionPlanId(databasePartitionPlan.getId());
        tablePartitionPlans.add(tablePartitionPlan);

        scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
        try {
            partitionPlanJob.execute(new JobExecutionContext() {});
            fail("Expected exception not thrown");
        } catch (Exception e) {
            // Expected exception
        }

        scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));
        assertEquals(jobParameters.getDatabasePartitionPlanId(), databasePartitionPlan.getId());
        assertEquals(tablePartitionPlans.size(), 1);
        try {
            partitionPlanJob.execute(new JobExecutionContext() {
                ScheduleEntity scheduleEntity = this.scheduleEntity;

                ScheduleEntity getScheduleById() {
                    return scheduleEntity;
                }
            });
            DatabasePartitionPlanEntity dbPartitionPlan = partitionPlanService.getDatabasePartitionPlanById(
                    jobParameters.getDatabasePartitionPlanId());
            List<TablePartitionPlanEntity> partitionPlans = partitionPlanService
                    .getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId());
            assertTrue(databasePartitionPlan.isConfigEnabled());
            assertEquals(partitionPlans.size(), 1);
            partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                    tablePartitionPlans);
        } catch (Exception e) {
            log.warn("Create partition-plan database change task failed.", e);
            fail("Expected method to execute without exception");
        }

        try {
            scheduleService.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Stop partition plan job failed.", e);
            fail("Expected method to execute without exception");
        }
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}