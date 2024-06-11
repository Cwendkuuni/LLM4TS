import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.oceanbase.odc.common.json.JsonUtils;
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

    private PartitionPlanJob job;
    private JobExecutionContext context;
    private ScheduleEntity scheduleEntity;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        job = new PartitionPlanJob();
        job.partitionPlanTaskService = partitionPlanTaskService;
        job.partitionPlanService = partitionPlanService;
        job.scheduleService = scheduleService;

        context = mock(JobExecutionContext.class);
        scheduleEntity = new ScheduleEntity();
    }

    @Test
    public void testPartitionPlanJobExecution() throws SchedulerException {
        // Setup
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
            .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        // Execute
        job.execute(context);

        // Verify
        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), anyList());
    }

    @Test
    public void testPartitionPlanJobExecutionWithEmptyTablePlans() throws SchedulerException {
        // Setup
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(context)).thenReturn(scheduleId);
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        when(JsonUtils.fromJson(scheduleEntity.getJobParametersJson(), PartitionPlanJobParameters.class))
            .thenReturn(jobParameters);

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(true);
        when(partitionPlanService.getDatabasePartitionPlanById(anyLong())).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = new ArrayList<>();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(anyLong())).thenReturn(tablePartitionPlans);

        // Execute
        job.execute(context);

        // Verify
        verify(scheduleService).terminate(scheduleEntity);
    }

    @Test(expected = UnExpectedException.class)
    public void testInterrupt() {
        job.interrupt();
    }
}