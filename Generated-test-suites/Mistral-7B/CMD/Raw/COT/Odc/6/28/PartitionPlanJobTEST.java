package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanService partitionPlanServiceMock;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_ValidScheduleEntity() {
        // given
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan = Mockito.mock(TablePartitionPlanEntity.class);
        tablePartitionPlans.add(tablePartitionPlan);

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        // when
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(anyLong(), eq(tablePartitionPlans));
    }

    @Test
    public void testExecute_ScheduleEntityNotFound() {
        // given
        long scheduleId = 1L;

        // when
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(log, times(1)).warn("Schedule not found,scheduleId={}", scheduleId);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        // when
        partitionPlanJob.interrupt();

        // then
        // UnExpectedException should be thrown
    }

    @Test
    public void testExecute_DatabasePartitionPlanDisabled() {
        // given
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        // when
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(scheduleServiceMock, times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testExecute_EmptyTablePartitionPlans() {
        // given
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();

        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(new ArrayList<>());

        // when
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // then
        verify(log, times(1)).info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                anyLong());
    }

}