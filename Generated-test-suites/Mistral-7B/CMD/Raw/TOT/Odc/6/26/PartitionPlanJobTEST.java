package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.exception.OdcException;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for PartitionPlanJob class
 */
public class PartitionPlanJobTest {

    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJobParameters jobParameters;
    private DatabasePartitionPlanEntity databasePartitionPlan;
    private List<TablePartitionPlanEntity> tablePartitionPlans;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setFlowInstanceId("flow-instance-id");
        databasePartitionPlan.setConfigEnabled(true);

        tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(Mockito.mock(TablePartitionPlanEntity.class));

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    /**
     * Test execute method when database partition plan is enable and table partition plans are present
     */
    @Test
    public void testExecuteWhenDatabasePartitionPlanIsEnabledAndTablesArePresent() throws OdcException, SchedulerException {
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        assertEquals("flow-instance-id", partitionPlanJob.partitionPlanTaskService.getDatabasePartitionPlanById(1L)
                .getFlowInstanceId());
        assertEquals(tablePartitionPlans, partitionPlanJob.partitionPlanTaskService.getValidTablePlanByDatabasePartitionPlanId(1L));
        Mockito.verify(scheduleService).terminate(Mockito.any(ScheduleEntity.class));
    }

    /**
     * Test execute method when database partition plan is disable
     */
    @Test
    public void testExecuteWhenDatabasePartitionPlanIsDisabled() throws OdcException, SchedulerException {
        databasePartitionPlan.setConfigEnabled(false);

        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        assertNull(partitionPlanJob.partitionPlanTaskService.getDatabasePartitionPlanById(1L));
        Mockito.verify(scheduleService, Mockito.never()).terminate(Mockito.any(ScheduleEntity.class));
    }

    /**
     * Test execute method when table partition plans are empty
     */
    @Test
    public void testExecuteWhenTablesAreEmpty() throws OdcException, SchedulerException {
        List<TablePartitionPlanEntity> emptyTablePartitionPlans = new ArrayList<>();

        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(emptyTablePartitionPlans);

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        assertNull(partitionPlanJob.partitionPlanTaskService.getDatabasePartitionPlanById(1L));
        Mockito.verify(scheduleService, Mockito.never()).terminate(Mockito.any(ScheduleEntity.class));
    }
}