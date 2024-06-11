package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private PartitionPlanService partitionPlanServiceMock;
    private ScheduleService scheduleServiceMock;

    @Before
    public void setUp() {
        partitionPlanTaskServiceMock = Mockito.mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = Mockito.mock(PartitionPlanService.class);
        scheduleServiceMock = Mockito.mock(ScheduleService.class);

        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_ValidScheduleEntityAndPartitionPlan_Success() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        verify(partitionPlanTaskServiceMock, times(1)).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_DisabledDatabasePartitionPlan_Exception() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        // No need to assert anything, as an UnExpectedException is expected
    }

    @Test
    public void testExecute_EmptyTablePartitionPlans_NoOp() {
        // Given
        UUID scheduleId = UUID.randomUUID();
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setFlowInstanceId(UUID.randomUUID().toString());
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        verify(partitionPlanTaskServiceMock, never()).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test
    public void testInterrupt_ThrowUnExpectedException() {
        // Given

        // When
        // Then
        verify(partitionPlanJob, throwsException(UnExpectedException.class));
    }

    @After
    public void tearDown() {
        SpringContextUtil.cleanUp();
    }
}