package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
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

import lombok.extern.slf4j.Slf4j;

public class PartitionPlanJobTest {

    private final PartitionPlanTaskService partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
    private final PartitionPlanService partitionPlanService = Mockito.mock(PartitionPlanService.class);
    private final ScheduleService scheduleService = Mockito.mock(ScheduleService.class);

    @Test
    public void testExecute() throws Exception {
        PartitionPlanJob job = new PartitionPlanJob();

        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{}");
        
        Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(new ArrayList<TablePartitionPlanEntity>());
        
        job.execute(context);

        // Asserts
        Mockito.verify(scheduleService, Mockito.times(1)).nullSafeGetById(Mockito.anyLong());
        Mockito.verify(partitionPlanService, Mockito.times(1)).getDatabasePartitionPlanById(Mockito.anyLong());
        Mockito.verify(partitionPlanService, Mockito.times(1)).getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong());
        Mockito.verify(partitionPlanTaskService, Mockito.never()).executePartitionPlan(Mockito.anyLong(), Mockito.anyList());
        Mockito.verify(scheduleService, Mockito.times(1)).terminate(scheduleEntity);
    }

    @Test
    public void testInterrupt() {
        PartitionPlanJob job = new PartitionPlanJob();

        try {
            job.interrupt();
        } catch (UnExpectedException e) {
            // Expected exception
        }
    }
}