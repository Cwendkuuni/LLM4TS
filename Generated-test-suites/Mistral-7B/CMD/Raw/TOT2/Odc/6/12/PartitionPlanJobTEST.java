package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleService;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanJobParameters;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.schedule.PartitionPlanJob;

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
    public void testExecuteNormalCase() throws SchedulerException, Exception {
        // Given
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        // When
        partitionPlanJob.execute(context);

        // Then
        assertFalse(partitionPlanJob.isInterrupted());
        assertNull(partitionPlanJob.getException());
    }

    @Test
    public void testExecuteDisableDatabasePartitionPlan() throws SchedulerException {
        // Given
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        // When
        partitionPlanJob.execute(context);

        // Then
        assertFalse(partitionPlanJob.isInterrupted());
        assertNull(partitionPlanJob.getException());
        assertNull(scheduleService.terminate(scheduleEntity));
    }

    @Test
    public void testExecuteNullDatabasePartitionPlan() throws SchedulerException {
        // Given
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = null;
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        // When
        partitionPlanJob.execute(context);

        // Then
        assertFalse(partitionPlanJob.isInterrupted());
        assertNull(partitionPlanJob.getException());
        assertNull(scheduleService.terminate(scheduleEntity));
    }

    @Test
    public void testExecuteEmptyTablePartitionPlans() throws SchedulerException {
        // Given
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        // When
        partitionPlanJob.execute(context);

        // Then
        assertFalse(partitionPlanJob.isInterrupted());
        assertNull(partitionPlanJob.getException());
        assertNull(scheduleService.terminate(scheduleEntity));
    }

    @Test(expected = UnExpectedException.class)
    public void testExecuteInterrupted() throws SchedulerException {
        // Given
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(
                jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenThrow(new UnExpectedException());

        // When
        partitionPlanJob.execute(context);

        // Then
        assertFalse(partitionPlanJob.isInterrupted());
        assertNotNull(partitionPlanJob.getException());
    }

    @Test
    public void testInterrupt() {
        // Given

        // When
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());

        // Then
        assertFalse(partitionPlanJob.isInterrupted());
        assertNotNull(partitionPlanJob.getException());
    }
}