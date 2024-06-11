import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@Slf4j
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    @Captor
    ArgumentCaptor<DatabasePartitionPlanEntity> databasePartitionPlanCaptor;

    @Captor
    ArgumentCaptor<List<TablePartitionPlanEntity>> tablePartitionPlansCaptor;

    private PartitionPlanService partitionPlanService;

    private PartitionPlanTaskService partitionPlanTaskService;

    private ScheduleService scheduleService;

    @Before
    public void setup() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanService = Mockito.mock(PartitionPlanService.class);
        partitionPlanTaskService = Mockito.mock(PartitionPlanTaskService.class);
        scheduleService = Mockito.mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanService);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskService);
        SpringContextUtil.setBean(ScheduleService.class, scheduleService);
    }

    @Test
    public void testExecute_whenScheduleFoundAndDatabasePartitionPlanEnabled_thenPartitionPlanJobExecuted() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test
    public void testExecute_whenScheduleNotFound_thenDoNothing() {
        // Given
        Long scheduleId = 1L;

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        // Nothing should happen
    }

    @Test(expected = RuntimeException.class)
    public void testExecute_whenScheduleNotFoundAndPartitionPlanJobInterrupted_thenThrowException() {
        // Given
        Long scheduleId = 1L;

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));
        partitionPlanJob.interrupt();

        // Then
        throw new RuntimeException();
    }

    @Test
    public void testExecute_whenDatabasePartitionPlanDisabled_thenDoNothing() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setConfigEnabled(false);
        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        // Nothing should happen
    }

    @Test(expected = SchedulerException.class)
    public void testExecute_whenPartitionPlanTaskServiceFails_thenThrowException() {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());
        when(scheduleService.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        when(partitionPlanService.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        when(partitionPlanTaskService.executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans)).thenThrow(new SchedulerException());

        // When
        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        // Then
        throw new SchedulerException();
    }

    @Test
    public void testInterrupt_whenInterrupted_thenNothing() {
        // Given

        // When
        partitionPlanJob.interrupt();

        // Then
        // Nothing should happen
    }
}