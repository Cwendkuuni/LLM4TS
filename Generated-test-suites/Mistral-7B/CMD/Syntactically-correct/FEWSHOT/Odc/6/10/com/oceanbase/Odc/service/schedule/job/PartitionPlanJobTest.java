package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
        partitionPlanJob = new PartitionPlanJob();
    }

    @After
    public void tearDown() {
        SpringContextUtil.clean();
    }

    @Test
    public void testExecute_databasePartitionPlanIsDisabled_return() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        JobExecutionContext context = mock(JobExecutionContext.class);
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            //Expected
        }
        verify(scheduleServiceMock, never()).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecute_noTablePartitionPlan_return() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            //Expected
        }
        verify(scheduleServiceMock, never()).terminate(any(ScheduleEntity.class));
    }

    @Test
    public void testExecute_databasePartitionPlanEnabledAndTablePartitionPlanExists_callExecutePartitionPlan() {
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(new TablePartitionPlanEntity());

        when(partitionPlanServiceMock.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        JobExecutionContext context = mock(JobExecutionContext.class);
        try {
            partitionPlanJob.execute(context);
        } catch (Exception e) {
            //Expected
        }
        verify(partitionPlanTaskServiceMock).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testGetScheduleId_returnsScheduleIdFromContext() {
        Long scheduleId = 1L;
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(scheduleId);

        Long returnedScheduleId = partitionPlanJob.getScheduleId(context);

        assertTrue(returnedScheduleId.equals(scheduleId));
    }

    @Test
    public void testGetScheduleId_returnsNullIfScheduleIdNotPresentInContext() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getMergedJobDataMap().get("scheduleId")).thenReturn(null);

        Long returnedScheduleId = partitionPlanJob.getScheduleId(context);

        assertNull(returnedScheduleId);
    }
}