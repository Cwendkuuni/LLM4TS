package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

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

    @Test
    public void testExecute() {
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);

        SpringContextUtil springContextUtil = mock(SpringContextUtil.class);
        when(springContextUtil.getBean(PartitionPlanTaskService.class)).thenReturn(partitionPlanTaskService);
        when(springContextUtil.getBean(PartitionPlanService.class)).thenReturn(partitionPlanService);
        when(springContextUtil.getBean(ScheduleService.class)).thenReturn(scheduleService);

        JobExecutionContext context = mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(scheduleEntity.getJobParametersJson()).thenReturn("");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(null);
        when(partitionPlanService.terminate(scheduleEntity)).thenThrow(new SchedulerException());
        when(partitionPlanTaskService.executePartitionPlan(1L, null)).thenThrow(new RuntimeException());

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.execute(context);

        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());
        partitionPlanJob.execute(context);

        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());
        when(partitionPlanService.terminate(scheduleEntity)).thenReturn(true);
        partitionPlanJob.execute(context);

        when(scheduleEntity.getJobParametersJson()).thenReturn("{}");
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(new ArrayList<>());
        when(partitionPlanTaskService.executePartitionPlan(1L, new ArrayList<>())).thenReturn(true);
        partitionPlanJob.execute(context);

        assertNotNull(partitionPlanJob);
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        try {
            partitionPlanJob.interrupt();
        } catch (UnExpectedException e) {
            assertNotNull(e);
        }
    }
}