
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import java.util.Collections;

import static org.junit.Assert.*;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob(partitionPlanTaskService, partitionPlanService, scheduleService);
    }

    @Test
    public void testExecute() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        when(context.getJobDetail().getJobDataMap().get(ScheduleTaskUtils.JOB_DATA_KEY)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(jsonUtility.fromJson(anyString(), eq(PartitionPlanJobParameters.class))).thenReturn(new PartitionPlanJobParameters());
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any())).thenReturn(Collections.singletonList(tablePartitionPlanEntity));
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(any(), any());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
    
    @Test
    public void testInvalidSchedule() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        when(context.getJobDetail().getJobDataMap().get(ScheduleTaskUtils.JOB_DATA_KEY)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(null);
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
    }

    @Test
    public void testDisabledDatabaseConfig() {
        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(context.getJobDetail().getJobDataMap().get(ScheduleTaskUtils.JOB_DATA_KEY)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(jsonUtility.fromJson(anyString(), eq(PartitionPlanJobParameters.class))).thenReturn(new PartitionPlanJobParameters());
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, never()).executePartitionPlan(any(), any());
    }

}
