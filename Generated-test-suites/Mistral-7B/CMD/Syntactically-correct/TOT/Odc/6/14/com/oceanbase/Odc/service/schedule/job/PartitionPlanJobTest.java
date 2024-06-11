package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private PartitionPlanTaskService partitionPlanTaskService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        SpringContextUtil mockedSpringContextUtil = mock(SpringContextUtil.class);
        when(mockedSpringContextUtil.getBean(PartitionPlanService.class)).thenReturn(this.partitionPlanService = mock(PartitionPlanService.class));
        when(mockedSpringContextUtil.getBean(PartitionPlanTaskService.class))
                .thenReturn(this.partitionPlanTaskService = mock(PartitionPlanTaskService.class));
        when(mockedSpringContextUtil.getBean(ScheduleService.class)).thenReturn(this.scheduleService = mock(ScheduleService.class));

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setScheduleService(scheduleService);
    }

    @Test
    public void execute_validSchedule() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test
    public void execute_disabledDatabasePartitionPlan() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(log, times(1)).info(anyString());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void execute_emptyTablePartitionPlans() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlan.getId());

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(log, times(1)).info(anyString());
        verify(scheduleService, times(1)).terminate(scheduleEntity);
    }
}