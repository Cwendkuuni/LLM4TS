package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

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

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute() throws Exception {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);

        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(context);

        verify(scheduleService, times(1)).terminate(scheduleEntity);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
            fail("Should throw UnExpectedException");
        } catch (UnExpectedException e) {
            // Expected
        }
    }
}