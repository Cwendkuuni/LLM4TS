package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import static org.mockito.Mockito.*;

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

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute(){
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        when(scheduleService.nullSafeGetById(any())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(any())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any())).thenReturn(Arrays.asList(tablePartitionPlan));
        partitionPlanJob.execute(context);
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithSchedulerException(){
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();
        when(scheduleService.nullSafeGetById(any())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(any())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(any())).thenReturn(Arrays.asList(tablePartitionPlan));
        doThrow(new SchedulerException()).when(scheduleService).terminate(any());
        partitionPlanJob.execute(context);
    }
    
    @Test(expected = UnExpectedException.class)
    public void testInterrupt(){
        partitionPlanJob.interrupt();
    }
    
}