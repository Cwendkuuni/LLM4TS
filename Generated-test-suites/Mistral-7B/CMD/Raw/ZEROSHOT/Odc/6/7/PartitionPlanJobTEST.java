package com.oceanbase.odc.service.schedule.job.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.google.common.base.Optional;
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
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        SpringContextUtil.setMockContext(Arrays.asList(partitionPlanServiceMock, partitionPlanTaskServiceMock,
                scheduleServiceMock));

        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute_scheduleNotfound() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(Optional.of(scheduleEntity));

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

        partitionPlanJob.execute(context);

        assertFalse("Expected method to not be called due to schedule not found.", false);
    }

    @Test
    public void testExecute_databasePartitionPlanDisable() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

        partitionPlanJob.execute(context);

        assertFalse("Expected method to not be called due to database partition plan is disable.", false);
    }

    @Test
    public void testExecute_tablePartitionPlansEmpty() {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(Arrays.asList());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

        partitionPlanJob.execute(context);

        assertFalse(
                "Expected method to not be called due to no table need to process in tablePartitionPlans.", false);
    }

    @Test(expected = UnExpectedException.class)
    public void testExecute_expectedException() throws SchedulerException {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Arrays.asList(mock(TablePartitionPlanEntity.class)));

        doThrow(new RuntimeException("Test Exception")).when(partitionPlanTaskServiceMock)
                .executePartitionPlan(anyLong(), anyList());

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

        partitionPlanJob.execute(context);
    }

    @Test
    public void testExecute_success() throws SchedulerException {
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(scheduleServiceMock.nullSafeGetById(anyLong())).thenReturn(Optional.of(scheduleEntity));
        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = Arrays.asList(mock(TablePartitionPlanEntity.class),
                mock(TablePartitionPlanEntity.class));
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().getString("scheduleId")).thenReturn("1");

        partitionPlanJob.execute(context);

        assertNotNull("Expected databasePartitionPlan to not be null.", databasePartitionPlan);
        assertNotNull("Expected tablePartitionPlans to not be null.", tablePartitionPlans);

        log.info("testExecute_success: Expected method to be called successfully.");
    }
}