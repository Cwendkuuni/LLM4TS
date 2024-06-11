import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PartitionPlanJobTest {

    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskService;

    private PartitionPlanService partitionPlanService;

    private ScheduleService scheduleService;

    @Before
    public void setUp() {
        partitionPlanJob = new PartitionPlanJob();
        partitionPlanTaskService = mock(PartitionPlanTaskService.class);
        partitionPlanService = mock(PartitionPlanService.class);
        scheduleService = mock(ScheduleService.class);

        partitionPlanJob.partitionPlanTaskService = partitionPlanTaskService;
        partitionPlanJob.partitionPlanService = partitionPlanService;
        partitionPlanJob.scheduleService = scheduleService;
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);
        TablePartitionPlanEntity tablePartitionPlan = Mockito.mock(TablePartitionPlanEntity.class);

        Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlan);
        Mockito.when(partitionPlanService.getValidTablePlanByDatabasePartitionPlanId(Mockito.anyLong())).thenReturn(
                List.of(tablePartitionPlan));
        Mockito.when(databasePartitionPlan.isConfigEnabled()).thenReturn(true);

        partitionPlanJob.execute(context);

        ArgumentCaptor<Long> scheduleIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PartitionPlanJobParameters> jobParametersArgumentCaptor = ArgumentCaptor.forClass(
                PartitionPlanJobParameters.class);

        verify(scheduleService).nullSafeGetById(scheduleIdArgumentCaptor.capture());
        Long scheduleId = scheduleIdArgumentCaptor.getValue();
        verify(partitionPlanService).getDatabasePartitionPlanById(jobParametersArgumentCaptor.capture()
                .getDatabasePartitionPlanId());
        PartitionPlanJobParameters testJobParameters = jobParametersArgumentCaptor.getValue();
        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                List.of(tablePartitionPlan));
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteFailure() throws Exception {
        JobExecutionContext context = Mockito.mock(JobExecutionContext.class);
        ScheduleEntity scheduleEntity = Mockito.mock(ScheduleEntity.class);
        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = Mockito.mock(DatabasePartitionPlanEntity.class);

        Mockito.when(scheduleService.nullSafeGetById(Mockito.anyLong())).thenReturn(scheduleEntity);
        Mockito.when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJsonString(jobParameters));
        Mockito.when(partitionPlanService.getDatabasePartitionPlanById(Mockito.anyLong())).thenReturn(databasePartitionPlan);
        Mockito.when(databasePartitionPlan.isConfigEnabled()).thenReturn(false);

        partitionPlanJob.execute(context);

        ArgumentCaptor<Long> scheduleIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<PartitionPlanJobParameters> jobParametersArgumentCaptor = ArgumentCaptor.forClass(
                PartitionPlanJobParameters.class);

        verify(scheduleService).nullSafeGetById(scheduleIdArgumentCaptor.capture());
        Long scheduleId = scheduleIdArgumentCaptor.getValue();
        verify(partitionPlanTaskService).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                List.of(Mockito.mock(TablePartitionPlanEntity.class)));

        Mockito.verify(scheduleService, Mockito.times(1)).terminate(scheduleEntity);
    }
}