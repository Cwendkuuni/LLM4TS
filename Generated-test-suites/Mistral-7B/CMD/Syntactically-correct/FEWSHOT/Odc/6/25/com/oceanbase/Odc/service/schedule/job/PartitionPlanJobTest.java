package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
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

    private ScheduleEntity scheduleEntityMock;

    private JobExecutionContext jobExecutionContextMock;

    @Before
    public void setUp() {
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;

        scheduleEntityMock = mock(ScheduleEntity.class);
        jobExecutionContextMock = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecuteNullContext() {
        when(scheduleServiceMock.nullSafeGetById((Long) null)).thenReturn(null);

        partitionPlanJob.execute(null);
    }

    @Test
    public void testExecuteNullScheduleEntity() {
        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(null);

        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test
    public void testExecuteWithDisabledDatabasePartitionPlan() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test
    public void testExecuteWithEmptyTablePartitionPlans() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test
    public void testExecuteWithValidDatabaseAndTablePartitionPlans() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tablePartitionPlan1 = new TablePartitionPlanEntity();
        tablePartitionPlan1.setId(1L);
        tablePartitionPlan1.setEnabled(true);
        tablePartitionPlans.add(tablePartitionPlan1);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        when(scheduleServiceMock.nullSafeGetById(1L)).thenReturn(scheduleEntityMock);
        when(scheduleEntityMock.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContextMock);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}