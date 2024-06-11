package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.quartz.SchedulerException;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {
    
    PartitionPlanJob partitionPlanJob;
    PartitionPlanService partitionPlanService;
    PartitionPlanTaskService partitionPlanTaskService;
    ScheduleService scheduleService;

    @Before
    public void setup() {
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob(partitionPlanService, partitionPlanTaskService, scheduleService);
    }

    @Test 
    public void testExecute() throws UnExpectedException, SchedulerException {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        when(context.getTrigger()).thenReturn(trigger);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(any())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tabPartitionPlans = new ArrayList<TablePartitionPlanEntity>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any())).thenReturn(tabPartitionPlans);

        partitionPlanJob.execute(context);

        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(any());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(any());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}