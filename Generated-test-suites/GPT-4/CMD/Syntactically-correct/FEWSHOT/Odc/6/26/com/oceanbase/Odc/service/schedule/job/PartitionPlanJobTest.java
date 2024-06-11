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
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Test
    public void testExecuteJob() throws Exception {

        // mockup dependencies
        JobExecutionContext context = mock(JobExecutionContext.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = mock(PartitionPlanJobParameters.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        UnExpectedException unExpectedException = mock(UnExpectedException.class);


        // stubbing
        when(context.getMergedJobDataMap()).thenReturn(new JobDataMap());
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        tablePartitionPlans.add(tablePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);


        // instantiate class under test
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob(partitionPlanTaskService,
                partitionPlanService, scheduleService);

        // test method
        partitionPlanJob.execute(context);

        // verify behaviors
        verify(scheduleEntity, times(1)).getJobParametersJson();
        verify(partitionPlanService, times(1)).getDatabasePartitionPlanById(anyLong());
        verify(partitionPlanService, times(1)).getValidTablePlanByDatabasePartitionPlanId(anyLong());
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterruptJob() {

        // instantiate class under test
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob();

        // test method
        partitionPlanJob.interrupt();
    }
}