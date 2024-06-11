import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
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

public class PartitionPlanJobTest {

    private PartitionPlanTaskService partitionPlanTaskService;
    private PartitionPlanService partitionPlanService;
    private ScheduleService scheduleService;

    private PartitionPlanJob partitionPlanJob;
    private JobExecutionContext jobExecutionContext;

    @Before
    public void setUp() {
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;

        jobExecutionContext = mock(JobExecutionContext.class);
    }

    @Test
    public void testExecute() throws Exception {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);

        verify(partitionPlanTaskService).executePartitionPlan(anyLong(), eq(tablePartitionPlans));
        verify(scheduleService, never()).terminate(any(ScheduleEntity.class));
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_Error() throws Exception {
        Long scheduleId = 1L;
        when(ScheduleTaskUtils.getScheduleId(jobExecutionContext)).thenReturn(scheduleId);

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        jobParameters.setDatabasePartitionPlanId(1L);
        scheduleEntity.setJobParametersJson(JsonUtils.toJson(jobParameters));

        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        when(partitionPlanService.getDatabasePartitionPlanById(1L)).thenReturn(databasePartitionPlan);

        List<TablePartitionPlanEntity> tablePartitionPlans = Collections.emptyList();
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(1L)).thenReturn(tablePartitionPlans);

        partitionPlanJob.execute(jobExecutionContext);
    }

    @Test
    public void testInterrupt() {
        assertThrows(UnExpectedException.class, () -> partitionPlanJob.interrupt());
    }
}