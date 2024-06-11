import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
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

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class PartitionPlanJobTest {

    @InjectMocks
    private PartitionPlanJob partitionPlanJob;

    private PartitionPlanTaskService partitionPlanTaskServiceMock;

    private PartitionPlanService partitionPlanServiceMock;

    private ScheduleService scheduleServiceMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        partitionPlanTaskServiceMock = mock(PartitionPlanTaskService.class);
        partitionPlanServiceMock = mock(PartitionPlanService.class);
        scheduleServiceMock = mock(ScheduleService.class);
        SpringContextUtil.setBean(PartitionPlanTaskService.class, partitionPlanTaskServiceMock);
        SpringContextUtil.setBean(PartitionPlanService.class, partitionPlanServiceMock);
        SpringContextUtil.setBean(ScheduleService.class, scheduleServiceMock);
    }

    @Test
    public void testExecute_ValidScheduleAndPartitionPlan() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        verify(partitionPlanTaskServiceMock).executePartitionPlan(databasePartitionPlan.getFlowInstanceId(),
                tablePartitionPlans);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_DisabledDatabasePartitionPlan() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(false);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of(new TablePartitionPlanEntity());

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        verifyNoInteractions(partitionPlanTaskServiceMock);
    }

    @Test(expected = JobExecutionException.class)
    public void testExecute_EmptyTablePartitionPlans() throws Exception {
        // Given
        Long scheduleId = 1L;
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setId(scheduleId);

        PartitionPlanJobParameters jobParameters = new PartitionPlanJobParameters();
        DatabasePartitionPlanEntity databasePartitionPlan = new DatabasePartitionPlanEntity();
        databasePartitionPlan.setId(1L);
        databasePartitionPlan.setConfigEnabled(true);

        List<TablePartitionPlanEntity> tablePartitionPlans = List.of();

        when(scheduleServiceMock.nullSafeGetById(scheduleId)).thenReturn(scheduleEntity);
        when(partitionPlanServiceMock.getDatabasePartitionPlanById(jobParameters.getDatabasePartitionPlanId()))
                .thenReturn(databasePartitionPlan);
        when(partitionPlanServiceMock.getValidTablePlanByDatabasePartitionPlanId(databasePartitionPlan.getId()))
                .thenReturn(tablePartitionPlans);

        // When
        partitionPlanJob.execute(mock(JobExecutionContext.class));

        // Then
        verifyNoInteractions(partitionPlanTaskServiceMock);
    }
}