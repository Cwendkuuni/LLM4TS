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
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());
        scheduleEntity.setJobParametersJson(JsonUtils.toJsonString(jobParameters));

        partitionPlanService.save(databasePartitionPlan);
        scheduleService.save(scheduleEntity);

        JobExecutionContext context = new JobExecutionContext();
        context.setJobDetail(new JobDetail());
        context.setMergedJobDataMap(new JobDataMap());
        context.setJobId(scheduleId);

        partitionPlanJob.execute(context);

        // Add assertions for the expected behavior of the execute method here
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testGetDatabasePartitionPlanById() {
        Long databasePartitionPlanId = UUID.randomUUID().longValue();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasepartitionPlan.setId(databasePartitionPlanId);

        DatabasePartitionPlanEntity result = partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanId);

        assertEquals(databasePartitionPlanId, result.getId());
    }

    @Test
    public void testGetValidTablePlanByDatabasePartitionPlanId() {
        Long databasePartitionPlanId = UUID.randomUUID().longValue();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        tablePartitionPlan1.setDatabasePartitionPlanId(databasePartitionPlanId);
        tablePartitionPlans.add(tablePartitionPlan1);

        List<TablePartitionPlanEntity> result = partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanId);

        assertEquals(tablePartitionPlans.size(), result.size());
        for (int i = 0; i < tablePartitionPlans.size(); i++) {
            assertEquals(tablePartitionPlans.get(i).getId(), result.get(i).getId());
        }
    }

    @Test(expected = SchedulerException.class)
    public void testTerminate() throws SchedulerException {
        Long scheduleId = UUID.randomUUID().longValue();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        scheduleService.terminate(scheduleEntity);
    }
}