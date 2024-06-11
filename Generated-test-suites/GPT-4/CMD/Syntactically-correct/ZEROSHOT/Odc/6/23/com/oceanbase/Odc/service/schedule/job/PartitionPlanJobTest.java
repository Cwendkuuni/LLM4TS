package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.quartz.SchedulerException;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(partitionPlanService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());

        partitionPlanJob.execute(mock(JobExecutionContext.class));
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyString(), anyList());
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testExecuteWhenScheduleServiceThrowsAnException() throws Exception {
        expectedEx.expect(RuntimeException.class);
        when(scheduleService.nullSafeGetById(anyLong())).thenThrow(SchedulerException.class);
        partitionPlanJob.execute(mock(JobExecutionContext.class));
    }

    @Test
    public void testExecuteWhenPartitionPlanJobThrowsAnException() throws Exception {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(partitionPlanService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());

        doThrow(new RuntimeException()).when(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());

        partitionPlanJob.execute(mock(JobExecutionContext.class));
    }
}
