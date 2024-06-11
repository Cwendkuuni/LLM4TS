package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    @Test
    public void testExecute_DatabasePartitionPlanIsNull() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn("scheduleId");

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(null);

        partitionPlanJob.execute(context);

        // Verify no partition plan found, schedule job is terminated
        verifyTerminateSchedule(scheduleEntity);
    }

    @Test
    public void testExecute_DatabasePartitionPlanIsDisabled() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn("scheduleId");

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(context);

        // Verify database partition plan is disabled, schedule job is terminated
        verifyTerminateSchedule(scheduleEntity);
    }

    @Test
    public void testExecute_NoTablePartitionPlanFound() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn("scheduleId");

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        // Verify no table partition plan found, schedule job is terminated
        verifyTerminateSchedule(scheduleEntity);
    }

    @Test
    public void testExecute_PartitionPlanTaskExecutionSuccess() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn("scheduleId");

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenReturn(true);

        partitionPlanJob.execute(context);

        // Verify partition plan execution successful, no schedule job termination
        verifyNoTerminateSchedule(scheduleEntity);
    }

    @Test
    public void testExecute_PartitionPlanTaskExecutionFailed() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn("scheduleId");

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleEntity.getId()).thenReturn(1L);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
        partitionPlanJob.setSpringContextUtil(springContextUtil);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenThrow(new RuntimeException());

        partitionPlanJob.execute(context);

        // Verify partition plan execution failed, schedule job is terminated
        verifyTerminateSchedule(scheduleEntity);
    }

    private void verifyTerminateSchedule(ScheduleEntity scheduleEntity) {
        try {
            scheduleService.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Terminate schedule failed.", e);
            throw new RuntimeException(e);
        }
    }

    private void verifyNoTerminateSchedule(ScheduleEntity scheduleEntity) {
        assertNull(scheduleService.getScheduleById(scheduleEntity.getId()));
    }
}