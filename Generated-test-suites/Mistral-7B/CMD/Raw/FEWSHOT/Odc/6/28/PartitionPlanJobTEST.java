package com.oceanbase.odc.service.schedule.job;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_validScheduleEntity() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJson(jobParameters);
        scheduleEntity.setJobParametersJson(json);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        JobExecutionContext context = mock(JobExecutionContext.class);
        partitionPlanJob.execute(context);

        Assert.assertFalse(databasePartitionPlan.isConfigEnabled());
        Assert.assertFalse(tablePartitionPlans.isEmpty());
        try {
            partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
        } catch (Exception e) {
            Assert.fail("executePartitionPlan should not throw an exception.");
        }
    }

    @Test
    public void testExecute_invalidScheduleEntity() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        partitionPlanJob.execute(mock(JobExecutionContext.class));

        Assert.assertEquals("Schedule not found,scheduleId=" + scheduleId, log.getErrorMessages().get(0));
    }

    @Test
    public void testExecute_nullScheduleEntity() {
        partitionPlanJob.execute(null);

        Assert.assertEquals("scheduleEntity is null", log.getErrorMessages().get(0));
    }
}