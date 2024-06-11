package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
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
import com.oceanbase.odc.service.partitionplan.PartitionPlanServiceMock;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskServiceMock;
import com.oceanbase.odc.service.schedule.ScheduleServiceMock;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;
    private PartitionPlanServiceMock partitionPlanServiceMock;
    private PartitionPlanTaskServiceMock partitionPlanTaskServiceMock;
    private ScheduleServiceMock scheduleServiceMock;

    @Before
    public void setup() {
        partitionPlanServiceMock = new PartitionPlanServiceMock();
        partitionPlanTaskServiceMock = new PartitionPlanTaskServiceMock();
        scheduleServiceMock = new ScheduleServiceMock();
        partitionPlanJob = new PartitionPlanJob();
        SpringContextUtil.registerSingleton(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.registerSingleton(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.registerSingleton(ScheduleService.class, scheduleServiceMock);
    }

    @After
    public void tearDown() {
        SpringContextUtil.clear();
    }

    @Test
    public void testExecute_whenScheduleFoundAndPartitionPlanEnabled_shouldExecutePartitionPlan() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(
                new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(scheduleEntity.getJobParametersJson()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn(Long.toString(scheduleId));

        // When
        partitionPlanJob.execute(context);

        // Then
        partitionPlanTaskServiceMock.assertExecutePartitionPlanCalledWith(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test
    public void testExecute_whenScheduleNotFound_shouldLogAndReturn() {
        // Given
        Long scheduleId = 1L;

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenThrow(new SchedulerException());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn(Long.toString(scheduleId));

        // When
        partitionPlanJob.execute(context);

        // Then
        log.warn("Schedule not found,scheduleId={}", scheduleId);
    }

    @Test
    public void testExecute_whenDatabasePartitionPlanDisabled_shouldLogAndReturn() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(
                new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(scheduleEntity.getJobParametersJson()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId"))
                .thenReturn(Long.toString(scheduleId));

        // When
        partitionPlanJob.execute(context);

        // Then
        log.info(
                "database partition plan is disable or no table need to process,start to stop partition-plan job,scheduleId={}",
                scheduleId);
    }

    @Test
    public void testExecute_whenExceptionThrownDuringPartitionPlanExecution_shouldLogAndReturn() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(
                new TablePartitionPlanEntity(),
                new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(scheduleEntity.getJobParametersJson()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId"))
                .thenReturn(Long.toString(scheduleId));

        when(partitionPlanTaskServiceMock.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans))
                .thenThrow(new Exception());

        // When
        partitionPlanJob.execute(context);

        // Then
        try {
            scheduleServiceMock.terminate(scheduleEntity);
        } catch (SchedulerException e) {
            log.warn("Stop partition plan job failed.", e);
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInterrupt_shouldThrowException() {
        // Given
        JobExecutionContext context = mock(JobExecutionContext.class);

        // When & Then
        Throwable exception = assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());

        // Then
        assertNull(exception.getCause());
    }
}