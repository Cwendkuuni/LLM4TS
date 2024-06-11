
package com.oceanbase.odc.service.schedule.job;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {
    @Mock
    private JobExecutionContext context;

    @Mock
    private PartitionPlanTaskService taskService;

    @Mock
    private PartitionPlanService planService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private PartitionPlanJob job;

    @Test
    public void testExecute() {
        Long scheduleId = 1L;

        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters params = new PartitionPlanJobParameters();
        params.setDatabasePartitionPlanId(1L);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(params));

        DatabasePartitionPlanEntity databasePartitionPlan = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(planService.getDatabasePartitionPlanById(params.getDatabasePartitionPlanId())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        TablePartitionPlanEntity tableEntity = mock(TablePartitionPlanEntity.class);
        tablePartitionPlans.add(tableEntity);
        when(planService.getValidTablePlanByDatabasePartitionPlanId(params.getDatabasePartitionPlanId())).thenReturn(tablePartitionPlans);

        job.execute(context);
        verify(taskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(), tablePartitionPlans);
    }

    @Test(expected = RuntimeException.class)
    public void testInterrupt() {
        job.interrupt();
    }
}
