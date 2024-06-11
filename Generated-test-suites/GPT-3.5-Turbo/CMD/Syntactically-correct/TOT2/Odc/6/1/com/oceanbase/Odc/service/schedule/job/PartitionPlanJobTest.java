import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.metadb.partitionplan.DatabasePartitionPlanEntity;
import com.oceanbase.odc.metadb.partitionplan.TablePartitionPlanEntity;
import com.oceanbase.odc.metadb.schedule.ScheduleEntity;
import com.oceanbase.odc.service.common.util.SpringContextUtil;
import com.oceanbase.odc.service.partitionplan.PartitionPlanService;
import com.oceanbase.odc.service.partitionplan.PartitionPlanTaskService;
import com.oceanbase.odc.service.quartz.util.ScheduleTaskUtils;
import com.oceanbase.odc.service.schedule.ScheduleService;
import com.oceanbase.odc.service.schedule.model.PartitionPlanJobParameters;
import com.oceanbase.tools.migrator.common.exception.UnExpectedException;

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;
    private PartitionPlanJob partitionPlanJob;

    @Before
    public void setup() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecute() throws SchedulerException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        // Test when database partition plan is disabled
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);
        partitionPlanJob.execute(context);
        verify(scheduleService, times(1)).terminate(scheduleEntity);

        // Test when no tables need to be processed
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(new ArrayList<>());
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, never()).executePartitionPlan(anyLong(), anyList());

        // Test execute partition plan
        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);
        partitionPlanJob.execute(context);
        verify(partitionPlanTaskService, times(1)).executePartitionPlan(anyLong(), eq(tablePartitionPlans));

        // Test exception case
        doThrow(new RuntimeException("Test Exception")).when(scheduleService).terminate(scheduleEntity);
        assertThrows(RuntimeException.class, () -> partitionPlanJob.execute(context));
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}