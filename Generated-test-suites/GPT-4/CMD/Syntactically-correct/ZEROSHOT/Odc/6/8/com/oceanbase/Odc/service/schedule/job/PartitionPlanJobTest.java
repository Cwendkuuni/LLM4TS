package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.*;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.job.OdcJob;
import com.oceanbase.odc.service.schedule.job.PartitionPlanJob;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;
    
    @Mock
    private JobExecutionContext jobExecutionContext;


    private PartitionPlanJobParameters jobParameters;

    private List<TablePartitionPlanEntity> tablePartitionPlans;
    
    @Before
    public void setUp() throws Exception {
        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setId(1L);
        databasePartitionPlanEntity.setConfigEnabled(true);
        databasePartitionPlanEntity.setFlowInstanceId(1L);

        TablePartitionPlanEntity tablePartitionPlanEntity = new TablePartitionPlanEntity();
        tablePartitionPlanEntity.setId(1L);

        tablePartitionPlans = Collections.singletonList(tablePartitionPlanEntity);

        jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(databasePartitionPlanEntity.getId());

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        when(jobExecutionContext.getJobDetail()).thenReturn(mock(JobDetail.class));
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleEntity.getId());
        when(scheduleService.nullSafeGetById(scheduleEntity.getId())).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(databasePartitionPlanEntity.getId())).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlanEntity.getId())).thenReturn(tablePartitionPlans);

    }
    
    @Test
    public void testExecute() {
        //Test the normal case
        partitionPlanJob.execute(jobExecutionContext);
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());

        //Test the case where databasePartitionPlan is not enabled
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(new DatabasePartitionPlanEntity());
        partitionPlanJob.execute(jobExecutionContext);
        verify(scheduleService).terminate(any(ScheduleEntity.class));

        //Test the case where tablePartitionPlans is empty
        tablePartitionPlans.clear();
        partitionPlanJob.execute(jobExecutionContext);
        verify(partitionPlanService, times(2)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
    }

    @Test
    public void testInterrupt() {
        try {
            partitionPlanJob.interrupt();
        } catch (Exception e) {
            //Expected
        }
    }
}
