
package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import org.quartz.JobExecutionContext;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private JobExecutionContext context;

    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute() {

        ScheduleEntity mockScheduleEntity = new ScheduleEntity();
        mockScheduleEntity.setJobParametersJson("{'databasePartitionPlanId':112233}");
        
        DatabasePartitionPlanEntity mockDatabasePartitionPlan = new DatabasePartitionPlanEntity();
        mockDatabasePartitionPlan.setConfigEnabled(true);
        mockDatabasePartitionPlan.setFlowInstanceId("flowId");

        List<TablePartitionPlanEntity> mockTablePartitionPlans = new ArrayList<>();
        mockTablePartitionPlans.add(new TablePartitionPlanEntity());

        when(scheduleService.nullSafeGetById(anyLong())).thenReturn(mockScheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(mockDatabasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(mockTablePartitionPlans);

        try {
            partitionPlanJob.execute(context);
        } catch (JobExecutionException e) {
            fail("Exception should not have been thrown.");
        }

        verify(scheduleService).nullSafeGetById(anyLong());
        verify(partitionPlanService).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService).executePartitionPlan(anyString(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

}
