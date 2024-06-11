package com.oceanbase.odc.service.schedule.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

@RunWith(MockitoJUnitRunner.class)
public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;

    @Mock
    private PartitionPlanService partitionPlanService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    JobExecutionContext context;

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }

    @Test
    public void testExecute() {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = mock(ScheduleEntity.class);
        DatabasePartitionPlanEntity databasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);

        when(scheduleService.nullSafeGetById(any())).thenReturn(scheduleEntity);

        PartitionPlanJobParameters parameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
                .thenReturn(parameters);

        when(partitionPlanService.getDatabasePartitionPlanById(parameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlanEntity);

        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        tablePartitionPlanEntities.add(mock(TablePartitionPlanEntity.class));
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(parameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlanEntities);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1))
                .executePartitionPlan(databasePartitionPlanEntity.getFlowInstanceId(), tablePartitionPlanEntities);
    }
}