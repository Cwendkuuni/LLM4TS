package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

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
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute_EnabledDatabasePartitionPlan_ValidTablePartitionPlans() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        tablePartitionPlan.setId(1L);
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(null);

        // Then
        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_DisabledDatabasePartitionPlan() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);

        // When
        partitionPlanJob.execute(null);

        // Then
        verify(log).info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}", scheduleId);
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_EmptyTablePartitionPlans() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(1L);
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(null);

        // Then
        verify(log).info("database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}", scheduleId);
        verify(scheduleService).terminate(scheduleEntity);
    }
}