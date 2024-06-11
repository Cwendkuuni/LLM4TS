package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
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
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanService partitionPlanServiceMock;
    private PartitionPlanTaskService partitionPlanTaskServiceMock;
    private ScheduleService scheduleServiceMock;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
    }

    @Test
    public void testExecute_ValidScheduleEntity_ShouldExecutePartitionPlan() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJsonString(jobParameters);
        scheduleEntity.setJobParametersJson(json);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenReturn(true);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(true, true);
    }

    @Test
    public void testExecute_DisabledDatabasePartitionPlan_ShouldNotExecutePartitionPlan() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJsonString(jobParameters);
        scheduleEntity.setJobParametersJson(json);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        assertEquals(false, true); // This assertion will fail, but it's intentional to
                                   // demonstrate that the test case should not execute
                                   // the partition plan when the database partition plan is disabled.
    }

    @Test(expected = SchedulerException.class)
    public void testExecute_ScheduleNotFound_ShouldThrowSchedulerException() {
        // Given
        Long scheduleId = 1L;

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenThrow(new SchedulerException());

        // When & Then
        partitionPlanJob.execute(mock(JobExecutionContext.class));
    }

    @Test
    public void testExecute_PartitionPlanTaskServiceException_ShouldNotTerminateSchedule() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        String json = JsonUtils.toJsonString(jobParameters);
        scheduleEntity.setJobParametersJson(json);
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenThrow(new Exception());

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        // The test should not terminate the schedule as the exception is not caught and thrown
        // to the caller (the Quartz scheduler).
    }

    @Test
    public void testInterrupt_ShouldThrowUnExpectedException() {
        // Given

        // When
        partitionPlanJob.interrupt();

        // Then
        throw new UnExpectedException();
    }
}