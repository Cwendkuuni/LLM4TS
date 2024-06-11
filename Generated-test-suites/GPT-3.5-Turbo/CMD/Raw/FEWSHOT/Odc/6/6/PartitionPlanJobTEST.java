package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.quartz.JobExecutionContext;

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
    public void testConstructor() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        assertNotNull(partitionPlanJob.partitionPlanTaskService);
        assertNotNull(partitionPlanJob.partitionPlanService);
        assertNotNull(partitionPlanJob.scheduleService);
    }

    @Test
    public void testExecute() throws Exception {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        JobExecutionContext context = mock(JobExecutionContext.class);
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(1L);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");
        ScheduleService scheduleService = mock(ScheduleService.class);
        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        partitionPlanJob.scheduleService = scheduleService;

        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(new DatabasePartitionPlanEntity());
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(List.of(new TablePartitionPlanEntity()));
        partitionPlanJob.partitionPlanService = partitionPlanService;

        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;

        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.interrupt();
    }
}