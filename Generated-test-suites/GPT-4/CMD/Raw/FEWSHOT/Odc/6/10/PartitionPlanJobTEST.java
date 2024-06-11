package com.oceanbase.odc.service.schedule.job;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;
  
    @Mock
    private JobExecutionContext jobExecutionContext;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();

        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
      
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        TablePartitionPlanEntity tablePartitionPlan = new TablePartitionPlanEntity();

        Mockito.when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);
        Mockito.when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId())).thenReturn(Collections.singletonList(tablePartitionPlan));

        partitionPlanJob.execute(jobExecutionContext);

        Mockito.verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                Collections.singletonList(tablePartitionPlan));
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}