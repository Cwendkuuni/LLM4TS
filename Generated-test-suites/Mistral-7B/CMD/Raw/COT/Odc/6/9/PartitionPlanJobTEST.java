package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testExecute_nullScheduleEntity() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        ScheduleService scheduleService = mock(ScheduleService.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(new SchedulerException());

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleTaskUtils = scheduleTaskUtils;
        partitionPlanJob.springContextUtil = springContextUtil;

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        partitionPlanJob.execute(context);

        assertFalse("Schedule not found,scheduleId=" + scheduleId, partitionPlanJob.isExecuted());
    }

    @Test
    public void testExecute_disabledDatabasePartitionPlan() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        ScheduleService scheduleService = mock(ScheduleService.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.isEnabled()).thenReturn(false);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleTaskUtils = scheduleTaskUtils;
        partitionPlanJob.springContextUtil = springContextUtil;

        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");

        partitionPlanJob.execute(null);

        assertNull("database partition plan is disable or no table need to process",
                partitionPlanJob.getDatabasePartitionPlan());
    }

    @Test
    public void testExecute_emptyTablePartitionPlans() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        ScheduleService scheduleService = mock(ScheduleService.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleTaskUtils = scheduleTaskUtils;
        partitionPlanJob.springContextUtil = springContextUtil;

        when(scheduleEntity.isEnabled()).thenReturn(true);
        when(scheduleEntity.getJobParametersJson()).thenReturn(
                "{\"databasePartitionPlanId\":1}");

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        partitionPlanService.getDatabasePartitionPlanById = mock(DatabasePartitionPlanEntity.class)
                .andReturn(databasePartitionPlan);
        partitionPlanService.getValidTablePlanByDatabasePartitionPlanId = mock(List.class)
                .andReturn(tablePartitionPlans);

        partitionPlanJob.execute(null);

        assertNull("database partition plan is disable or no table need to process",
                partitionPlanJob.getDatabasePartitionPlan());
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_exceptionDuringPartitionPlanExecution() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        ScheduleService scheduleService = mock(ScheduleService.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleTaskUtils = scheduleTaskUtils;
        partitionPlanJob.springContextUtil = springContextUtil;

        when(scheduleEntity.isEnabled()).thenReturn(true);
        when(scheduleEntity.getJobParametersJson()).thenReturn(
                "{\"databasePartitionPlanId\":1}");

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(databasePartitionPlan.getFlowInstanceId()).thenReturn(1L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(mock(TablePartitionPlanEntity.class));

        partitionPlanService.getDatabasePartitionPlanById = mock(DatabasePartitionPlanEntity.class)
                .andReturn(databasePartitionPlan);
        partitionPlanService.getValidTablePlanByDatabasePartitionPlanId = mock(List.class)
                .andReturn(tablePartitionPlans);

        when(partitionPlanTaskService.executePartitionPlan(anyLong(), anyList()))
                .thenThrow(new Exception());

        partitionPlanJob.execute(null);
    }

    @Test
    public void testGetDatabasePartitionPlan_validDatabasePartitionPlan() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        ScheduleService scheduleService = mock(ScheduleService.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        ScheduleTaskUtils scheduleTaskUtils = mock(ScheduleTaskUtils.class);
        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);

        partitionPlanJob.scheduleService = scheduleService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleTaskUtils = scheduleTaskUtils;
        partitionPlanJob.springContextUtil = springContextUtil;

        when(scheduleEntity.isEnabled()).thenReturn(true);
        when(scheduleEntity.getJobParametersJson()).thenReturn(
                "{\"databasePartitionPlanId\":1}");

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        partitionPlanService.getDatabasePartitionPlanById = mock(DatabasePartitionPlanEntity.class)
                .andReturn(databasePartitionPlan);

        DatabasePartitionPlanEntity result = partitionPlanJob.getDatabasePartitionPlan();

        assertThat(result).isEqualTo(databasePartitionPlan);
    }
}