package com.oceanbase.odc.service.schedule.job;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;

import static org.mockito.Mockito.*;

import java.util.Arrays;

public class PartitionPlanJobTest {

    private PartitionPlanJob job;
    private JobExecutionContext context;
    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private ScheduleEntity scheduleEntity;
    private DatabasePartitionPlanEntity databasePartitionPlanEntity;

    @Before
    public void setUp() {
        job = new PartitionPlanJob();
        context = mock(JobExecutionContext.class);
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        scheduleEntity = mock(ScheduleEntity.class);
        databasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        job.interrupt();
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(Arrays.asList());
        job.execute(context);
    }

    @Test
    public void testExecuteWithException() {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenThrow(RuntimeException.class);
        job.execute(context);
    }
    
    @Test
    public void testExecuteWithSchedulerException() {
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(false);
        doThrow(SchedulerException.class).when(scheduleService).terminate(scheduleEntity);
        job.execute(context);
    }

}