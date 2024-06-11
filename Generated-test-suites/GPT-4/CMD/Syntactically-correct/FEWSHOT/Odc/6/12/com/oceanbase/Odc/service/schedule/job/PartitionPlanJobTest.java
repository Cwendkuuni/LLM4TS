
package com.oceanbase.odc.service.schedule.job;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.quartz.JobExecutionContext;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

import static org.junit.Assert.*;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private JobExecutionContext context;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setUp() {
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        context = Mockito.mock(JobExecutionContext.class);
        partitionPlanJob = new PartitionPlanJob();
    }

    @Test
    public void testExecute() {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setJobParametersJson("{\"databasePartitionPlanId\":1}");

        List<TablePartitionPlanEntity> tablePartitionPlanEntities = new ArrayList<>();
        tablePartitionPlanEntities.add(new TablePartitionPlanEntity());

        DatabasePartitionPlanEntity databasePartitionPlanEntity = new DatabasePartitionPlanEntity();
        databasePartitionPlanEntity.setConfigEnabled(true);
        databasePartitionPlanEntity.setFlowInstanceId(1L);

        when(scheduleService.nullSafeGetById(1L)).thenReturn(scheduleEntity);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlanEntities);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlanEntity.getFlowInstanceId(), tablePartitionPlanEntities);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
