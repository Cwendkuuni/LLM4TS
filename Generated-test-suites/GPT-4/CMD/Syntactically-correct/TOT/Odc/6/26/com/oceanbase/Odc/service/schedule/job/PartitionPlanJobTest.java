package com.oceanbase.odc.service.schedule.job;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private JobExecutionContext context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

   @Test(expected = UnExpectedException.class)
    public void testInterrupt() throws Exception {
        partitionPlanJob.interrupt();
    }


    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);
        
        ArrayList<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);
        
        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }
}