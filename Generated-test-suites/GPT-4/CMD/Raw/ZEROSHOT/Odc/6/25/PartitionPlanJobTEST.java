
package com.oceanbase.odc.service.schedule.job;

import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class PartitionPlanJobTest {

    @Mock
    private PartitionPlanService partitionPlanService;
    @Mock
    private PartitionPlanTaskService partitionPlanTaskService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private JobExecutionContext context;

    private PartitionPlanJob partitionPlanJob;
    private DatabasePartitionPlanEntity databasePartitionPlanEntity;
    private List<TablePartitionPlanEntity> tablePartitionPlans;
    private PartitionPlanJobParameters jobParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        partitionPlanJob = new PartitionPlanJob();
        databasePartitionPlanEntity = mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlanEntity = mock(TablePartitionPlanEntity.class);
        tablePartitionPlans = Arrays.asList(tablePartitionPlanEntity);
        jobParameters = mock(PartitionPlanJobParameters.class);
    }

    @Test
    public void testExecute() {
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlanEntity);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        when(databasePartitionPlanEntity.isConfigEnabled()).thenReturn(true);

        partitionPlanJob.execute(context);

        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), anyList());
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWhenException() {
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenThrow(RuntimeException.class);

        partitionPlanJob.execute(context);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        partitionPlanJob.interrupt();
    }
}
