import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void setup() {
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskServiceMock;
        partitionPlanJob.partitionPlanService = partitionPlanServiceMock;
        partitionPlanJob.scheduleService = scheduleServiceMock;
    }

    @Test
    public void execute_validScheduleEntity_shouldExecutePartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
        List<TablePartitionPlanEntity> tablePartitionPlans = Mockito.mock(List.class);

        Mockito.when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        Mockito.when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(tablePartitionPlans);
        Mockito.when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        ArgumentCaptor<DatabasePartitionPlanEntity> argDatabasePartitionPlanCaptor = ArgumentCaptor.forClass(DatabasePartitionPlanEntity.class);
        ArgumentCaptor<List<TablePartitionPlanEntity>> argTablePartitionPlansCaptor = ArgumentCaptor.forClass(List.class);

        verify(partitionPlanTaskServiceMock).executePartitionPlan(argDatabasePartitionPlanCaptor.capture(), argTablePartitionPlansCaptor.capture());

        DatabasePartitionPlanEntity capturedDatabasePartitionPlan = argDatabasePartitionPlanCaptor.getValue();
        List<TablePartitionPlanEntity> capturedTablePartitionPlans = argTablePartitionPlansCaptor.getValue();

        assertEquals(jobParameters.getDatabasePartitionPlanId(), capturedDatabasePartitionPlan.getId());
        assertEquals(tablePartitionPlans, capturedTablePartitionPlans);
    }

    @Test
    public void execute_invalidScheduleEntity_shouldNotExecutePartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);

        Mockito.when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        verify(scheduleServiceMock, Mockito.never()).terminate(scheduleEntity);
    }

    @Test
    public void execute_disableDatabasePartitionPlan_shouldNotExecutePartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);

        Mockito.when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        Mockito.when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        Mockito.when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        verify(scheduleServiceMock, Mockito.times(1)).terminate(scheduleEntity);
    }

    @Test
    public void execute_emptyTablePartitionPlans_shouldNotExecutePartitionPlan() {
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);

        Mockito.when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        Mockito.when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);

        Mockito.when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);
        Mockito.when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(Mockito.Collections.emptyList());

        partitionPlanJob.execute(Mockito.mock(JobExecutionContext.class));

        verify(scheduleServiceMock, Mockito.times(1)).terminate(scheduleEntity);
    }

    @Test
    public void interrupt_shouldThrowUnExpectedException() {
        partitionPlanJob.interrupt();

        verify(partitionPlanJob, Mockito.times(1)).interrupt();
        Mockito.verifyNoMoreInteractions(partitionPlanJob);
    }

    @Test
    public void testInitialize() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = null;
        partitionPlanJob.partitionPlanService = null;
        partitionPlanJob.scheduleService = null;

        partitionPlanJob = new PartitionPlanJob();
        partitionPlanJob.partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanJob.partitionPlanService = mock(PartitionPlanService.class);
        partitionPlanJob.scheduleService = mock(ScheduleService.class);

        assertEquals(PartitionPlanTaskService.class, partitionPlanJob.partitionPlanTaskService.getClass());
        assertEquals(PartitionPlanService.class, partitionPlanJob.partitionPlanService.getClass());
        assertEquals(ScheduleService.class, partitionPlanJob.scheduleService.getClass());
    }
}