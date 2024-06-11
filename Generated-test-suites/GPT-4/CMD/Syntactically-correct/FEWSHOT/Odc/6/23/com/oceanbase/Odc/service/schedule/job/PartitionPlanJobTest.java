package com.oceanbase.odc.service.schedule.job;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.common.json.JsonUtils;
import java.util.ArrayList;
import java.util.List;

public class PartitionPlanJobTest {
    @Test
    public void testExecute() {

        //Mocking all the dependencies
        JobExecutionContext context = mock(JobExecutionContext.class);
        PartitionPlanTaskService partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        PartitionPlanService partitionPlanService = mock(PartitionPlanService.class);
        ScheduleService scheduleService = mock(ScheduleService.class);
        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);

        long scheduleId = 123;
        ScheduleTaskUtils.setScheduleId(context, scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        tablePartitionPlanEntities.add(tablePartitionPlanEntity);

        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlanEntities);

        PartitionPlanJob partitionPlanJob = new PartitionPlanJob(
                partitionPlanTaskService,
                partitionPlanService,
                scheduleService
        );

        partitionPlanJob.execute(context);

        //Verifying that the execute method was called once
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        PartitionPlanJob partitionPlanJob = new PartitionPlanJob(
                mock(PartitionPlanTaskService.class),
                mock(PartitionPlanService.class),
                mock(ScheduleService.class)
        );
        partitionPlanJob.interrupt();
    }
}