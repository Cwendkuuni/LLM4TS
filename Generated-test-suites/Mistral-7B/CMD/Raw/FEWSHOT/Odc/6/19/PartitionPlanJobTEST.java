package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleEntity;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.PartitionPlanJob;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
    }

    @Test
    public void testExecute_scheduleEntityIsNull_doNothing() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleEntity")).thenReturn(null);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_databasePartitionPlanIsNull_doNothing() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(null);
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_tablePartitionPlansIsNull_doNothing() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(Arrays.asList());
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);
        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_databasePartitionPlanEnabled_executePartitionPlan() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);
        partitionPlanJob.execute(context);

        // validate executePartitionPlan was called
        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test
    public void testExecute_schedulerExceptionThrown() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        when(scheduleService.terminate(scheduleEntity)).thenThrow(new SchedulerException("Terminate exception"));
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);
        partitionPlanJob.execute(context);

        // validate terminate was called
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_unexpectedExceptionThrown() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(), new TablePartitionPlanEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans))
                .thenThrow(new UnExpectedException("Unexpected exception"));
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleEntity")).thenReturn(scheduleEntity);
        partitionPlanJob.execute(context);

        // validate log error message
        verify(log).warn("Create partition-plan database change task failed.", eq(new UnExpectedException("Unexpected exception")));
    }

    @Test
    public void testInterrupt_unexpectedExceptionThrown() {
        partitionPlanJob.interrupt();
        verify(log).warn("Interrupt unexpected exception");
    }
}