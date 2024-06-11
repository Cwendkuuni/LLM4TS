package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private JobExecutionContext context;

    @Mock
    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    @Mock
    private TablePartitionPlanEntity tablePartitionPlanEntity;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.setPartitionPlanTaskService(partitionPlanTaskService);
        partitionPlanJob.setPartitionPlanService(partitionPlanService);
        partitionPlanJob.setScheduleService(scheduleService);
    }

    @Test
    public void testExecute() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong()))
                .thenReturn(Collections.singletonList(tablePartitionPlanEntity));

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
        verify(scheduleService, never()).terminate(any());
    }

    @Test
    public void testExecuteWhenDatabasePartitionPlanIsDisabledOrEmptyTable() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(false);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(Collections.emptyList());

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test(expected = SchedulerException.class)
    public void testExecuteWhenTerminateThrowsException() {
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(new ScheduleEntity());
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(false);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(Collections.emptyList());

        doThrow(SchedulerException.class).when(scheduleService).terminate(any());

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
        verify(scheduleService, times(1)).terminate(any());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
